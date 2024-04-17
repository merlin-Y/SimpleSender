package cn.merlin.database.model

import org.jetbrains.exposed.sql.Table

object DeviceModel : Table() {
    val deviceId = integer("id").autoIncrement()
    val deviceName = varchar("deviceName",50)
    val deviceIpAddress = varchar("deviceIpAddress",50)
    val deviceNickName = varchar("deviceNickName",50)
    val deviceIdentifier = varchar("deviceIdentifier",50)
    val deviceType = varchar("deviceType",50)

    override val primaryKey = PrimaryKey(deviceId,name = "deviceId")
}