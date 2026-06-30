package empire.digiprem.kmptemplate.server.common.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder : org.springframework.security.crypto.password.PasswordEncoder {
    private val delegate = BCryptPasswordEncoder()
    override fun encode(rawPassword: CharSequence): String = delegate.encode(rawPassword)
    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean =
        delegate.matches(rawPassword, encodedPassword)
}
