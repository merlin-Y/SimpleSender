package cn.merlin.network

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.tools.detectedDeviceIdentifierSet
import cn.merlin.tools.getWifiAddress
import cn.merlin.tools.isWifiConnected
import kotlinx.coroutines.*
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class NetworkController {
    val receiver = Receiver()
    val sender = Sender()
    val currentDevice = Device()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var networkJobs = mutableListOf<Job>()

    suspend fun startNetworkControl(detectedDeviceList: SnapshotStateList<DeviceViewModel>) {
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
                            startJobs(detectedDeviceList)
                        }
                    } else {
                        stopJobs()
                        detectedDeviceList.clear()
                        detectedDeviceIdentifierSet.clear()
                    }
                }
            }catch (_: Exception){}
        }
    }

    private fun startJobs(detectedDeviceList: SnapshotStateList<DeviceViewModel>) {
        val sendCodeJob = coroutineScope.launch { sender.sendCodeRequest(currentDevice) }
        val receiveDetectCodeJob = coroutineScope.launch { receiver.startDetectCodeReceiver(currentDevice) }
        val receiveDeviceCodeJob = coroutineScope.launch { receiver.startDeviceCodeReceiver(detectedDeviceList) }
        val receiveMessageCodeJob = coroutineScope.launch { receiver.startMessageCodeReceiver() }

        networkJobs.addAll(listOf(sendCodeJob, receiveDetectCodeJob, receiveDeviceCodeJob, receiveMessageCodeJob))
    }

    private fun stopJobs() {
        networkJobs.forEach { it.cancel() }
        networkJobs.clear()
    }
}