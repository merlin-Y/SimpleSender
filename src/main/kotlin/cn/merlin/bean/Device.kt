package cn.merlin.bean

import java.io.Serializable

data class Device(
    var deviceName: String = "",
    var deviceIpAddress: String = "",
    var deviceMacAddress: String = "",
    var deviceNickName: String = "",
    var deviceType: String = ""
): Serializable