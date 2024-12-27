package cn.merlin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform