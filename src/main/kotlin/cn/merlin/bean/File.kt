package cn.merlin.bean

import java.io.Serializable

data class File(
    var fileName: String = "",
    var dataSize: Int = -1,
    var totalPackets: Int = -1
): Serializable