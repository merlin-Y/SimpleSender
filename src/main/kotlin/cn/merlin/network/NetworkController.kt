package cn.merlin.network

import cn.merlin.bean.Device
import cn.merlin.tools.*
import cn.merlin.tools.DeviceConfiguration.detectedDeviceIdentifierSet
import cn.merlin.tools.DeviceConfiguration.getSavedList
import kotlinx.coroutines.*

class NetworkController {
    private val receiver = Receiver()
    private val sender = Sender()
    private val currentDevice = Device()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var networkJobs = mutableListOf<Job>()

    suspend fun startNetworkControl() {
        coroutineScope.launch {
            try{
                while (true) {
                    delay(2000)
                    if (getWifiAddress()) {
                        currentDevice.deviceIpAddress = cn.merlin.tools.currentDevice.value.deviceIpAddress.value
                        currentDevice.deviceName = cn.merlin.tools.currentDevice.value.deviceName.value
                        currentDevice.deviceType = "computer"
                        currentDevice.deviceIdentifier = cn.merlin.tools.currentDevice.value.deviceIdentifier.value
                        currentDevice.deviceNickName = cn.merlin.tools.currentDevice.value.deviceNickName.value
                        if (networkJobs.isEmpty() && currentDevice.deviceIpAddress != "") {
                            startJobs()
                        }
                    } else {
                        stopJobs()
                        getSavedList().clear()
                        detectedDeviceIdentifierSet.clear()
                    }
                }
            }catch (_: Exception){}
        }
    }

    private fun startJobs() {
        val sendCodeJob = coroutineScope.launch { sender.sendCodeRequest(currentDevice) }
        val receiveDetectCodeJob = coroutineScope.launch { receiver.startDetectCodeReceiver(currentDevice) }
        val receiveDeviceCodeJob = coroutineScope.launch { receiver.startDeviceCodeReceiver() }
        val receiveMessageCodeJob = coroutineScope.launch { receiver.startMessageCodeReceiver() }

        networkJobs.addAll(listOf(sendCodeJob, receiveDetectCodeJob, receiveDeviceCodeJob, receiveMessageCodeJob))
    }

    private fun stopJobs() {
        networkJobs.forEach { it.cancel() }
        networkJobs.clear()
    }
}