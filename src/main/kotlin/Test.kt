import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import cn.merlin.network.Sender
import cn.merlin.test.SenderTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() {
    val senderTest = SenderTest()
    val file = File("C:\\Users\\merlin\\Documents\\SimpleSender\\src\\main\\resources\\file.zip")
    senderTest.sendFileToSelectedDevice(file)
}