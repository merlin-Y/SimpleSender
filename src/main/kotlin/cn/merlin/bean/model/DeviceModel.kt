package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Device

class DeviceModel(device: Device) {
    var deviceId = mutableStateOf(device.deviceId)
    var deviceName = mutableStateOf(device.deviceName)
    var deviceIpAddress = mutableStateOf(device.deviceIpAddress)
    var deviceMacAddress = mutableStateOf(device.deviceMacAddress)
    var deviceNickName = mutableStateOf(device.deviceNickName)
    var deviceType = mutableStateOf(device.deviceType)
    var inListType = mutableStateOf(true)
}