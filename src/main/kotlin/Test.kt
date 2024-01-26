import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.network.Sender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() {
    val sender = Sender()
    val file = File("D:/file.zip")
    val job = sender.sendFileToSelectedDevice(DeviceModel(Device(deviceIpAddress = "127.0.0.1")),file)
    runBlocking {
        job.join()
    }

}