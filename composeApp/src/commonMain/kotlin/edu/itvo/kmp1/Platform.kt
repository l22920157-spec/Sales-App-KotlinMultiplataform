package edu.itvo.kmp1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform