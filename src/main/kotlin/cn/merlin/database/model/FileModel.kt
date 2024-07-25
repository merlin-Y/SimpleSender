package cn.merlin.database.model

import cn.merlin.database.model.DeviceModel.autoIncrement
import org.jetbrains.exposed.sql.Table
import java.io.Serializable

object FileModel : Table(){
    val fileId = integer("fileId").autoIncrement()
    val fileName = varchar("fileName",50)
    var filePath = varchar("filePath",150)

    override val primaryKey = PrimaryKey(fileId,name = "fileId")
}