package cn.merlin.database.model

import org.jetbrains.exposed.sql.Table

object FileModel : Table(){
    val fileId = integer("fileId").autoIncrement()
    val fileName = varchar("fileName",50)
    var filePath = varchar("filePath",150)
    var isSendFinished = bool("isSendFinished")
    var totalPackets = integer("totalPackets")
    var receiveFinished = integer("receiveFinished")

    override val primaryKey = PrimaryKey(fileId,name = "fileId")
}