package cn.merlin.network

import androidx.compose.runtime.MutableState
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.tools.*
import cn.merlin.tools.DeviceConfiguration.detectedDeviceIdentifierSet
import cn.merlin.tools.DeviceConfiguration.getDetectedList
import kotlinx.coroutines.*
import moe.tlaster.precompose.navigation.Navigator

class NetworkController(val navigator: Navigator) {
    private val receiver = Receiver()
    private val sender = Sender()
    private val currentDevice = Device()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var networkJobs = mutableListOf<Job>()


    suspend fun sendRequestCodeToSelectedDevice(device: DeviceViewModel): RequestCode{
        return sender.sendRequestCodeToSelectedDevice(currentDevice,device)
    }

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
                        getDetectedList().clear()
                        detectedDeviceIdentifierSet.clear()
                    }
                }
            }catch (_: Exception){}
        }
    }

    private fun startJobs() {
        val sendCodeJob = coroutineScope.launch { sender.sendCodeRequest(currentDevice) }
        val sendKeepJob = coroutineScope.launch { sender.deviceConnectionKeeper(currentDevice) }
        val receiveCommandCodeJob = coroutineScope.launch { receiver.startCommandCodeReceiver(currentDevice, navigator) }

        networkJobs.addAll(listOf(sendCodeJob, sendKeepJob, receiveCommandCodeJob))
    }

    private fun stopJobs() {
        networkJobs.forEach { it.cancel() }
        networkJobs.clear()
    }
}