package empire.digiprem.kmp_project_tamplate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform