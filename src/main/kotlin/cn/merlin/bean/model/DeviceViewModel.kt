package cn.merlin.bean.model

import androidx.compose.runtime.mutableStateOf
import cn.merlin.bean.Device

class DeviceViewModel(device: Device) {
    val deviceId = mutableStateOf(device.deviceId)
    val deviceName = mutableStateOf(device.deviceName)
    val deviceIpAddress = mutableStateOf(device.deviceIpAddress)
    val deviceNickName = mutableStateOf(device.deviceNickName)
    val deviceIdentifier = mutableStateOf(device.deviceIdentifier)
    val deviceType = mutableStateOf(device.deviceType)
    val inListType = mutableStateOf(true)
    val isConnected = mutableStateOf(true)
}