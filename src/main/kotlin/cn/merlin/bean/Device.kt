package cn.merlin.bean

import java.io.Serializable

data class Device(
    var deviceId: Int = -1,
    var deviceName: String = "",
    var deviceIpAddress: String = "",
    var deviceMacAddress: String = "",
    var deviceNickName: String = "",
    var deviceType: String = ""
): Serializable