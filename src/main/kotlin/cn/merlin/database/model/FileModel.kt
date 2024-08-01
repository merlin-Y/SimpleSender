package cn.merlin.database.model

import org.jetbrains.exposed.sql.Table

object FileModel : Table(){
    val fileId = integer("fileId").autoIncrement()
    val fileName = varchar("fileName",50)
    var filePath = varchar("filePath",150)

    override val primaryKey = PrimaryKey(fileId,name = "fileId")
}