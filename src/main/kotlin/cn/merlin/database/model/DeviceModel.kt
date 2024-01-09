package cn.merlin.database.model

import org.jetbrains.exposed.sql.Table

object DeviceModel : Table() {
    val deviceId = integer("id").autoIncrement()
    var deviceName = varchar("deviceName",50)
    var deviceIpAddress = varchar("deviceIpAddress",50)
    var deviceMacAddress = varchar("deviceMacAddress",50)
    var deviceNickName = varchar("deviceNickName",50)

    override val primaryKey = PrimaryKey(deviceId,name = "deviceId")
}