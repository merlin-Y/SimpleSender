package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Device

class DeviceViewModel(device: Device) {
    var deviceId = mutableStateOf(device.deviceId)
    var deviceName = mutableStateOf(device.deviceName)
    var deviceIpAddress = mutableStateOf(device.deviceIpAddress)
    var deviceNickName = mutableStateOf(device.deviceNickName)
    var deviceIdentifier = mutableStateOf(device.deviceIdentifier)
    var deviceType = mutableStateOf(device.deviceType)
    var inListType = mutableStateOf(true)
}