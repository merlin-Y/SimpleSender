package cn.merlin.bean

class File (
    var fileId: Int = -1,
    var fileName: String = "",
    var filePath: String = "",
    var isSendFinished: Boolean = false,
    var totalPackets: Int = -1,
)