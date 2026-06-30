package empire.digiprem.kmptemplate.server.common.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
class JwtService(
    @param:Value("\${app.jwt.secret}") private val secretBase64: String,
    @param:Value("\${app.jwt.access-token-expiry-ms}") private val accessTokenExpiryMs: Long,
    @param:Value("\${app.jwt.refresh-token-expiry-ms}") private val refreshTokenExpiryMs: Long,
) {

    @OptIn(ExperimentalEncodingApi::class)
    private val secretKey = Keys.hmacShaKeyFor(Base64.Default.decode(secretBase64))

    fun generateAccessToken(userId: String): String =
        generateToken(userId = userId, type = "access", expiryMs = accessTokenExpiryMs)

    fun generateRefreshToken(userId: String): String =
        generateToken(userId = userId, type = "refresh", expiryMs = refreshTokenExpiryMs)

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return claims["type"] as? String == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return claims["type"] as? String == "refresh"
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token)
            ?: throw empire.digiprem.kmptemplate.server.common.exception.InvalidTokenException("The JWT token is invalid or expired")
        return claims.subject
    }

    val refreshTokenExpiryMsValue: Long get() = refreshTokenExpiryMs

    private fun generateToken(userId: String, type: String, expiryMs: Long): String {
        val now = Date()
        val expiry = Date(now.time + expiryMs)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }
}
