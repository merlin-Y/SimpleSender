import cn.merlin.network.Scanner
import cn.merlin.network.ScannerServer
import kotlinx.coroutines.*

fun main() {
    runBlocking {
        val scannerServer = ScannerServer()
        val scanner = Scanner()
        val coroutineScope = CoroutineScope(Dispatchers.Default)

//        val job1 = coroutineScope.launch {
//            scannerServer.startServer()
//        }

        val job2 = coroutineScope.launch {
            scanner.detectDevices()
        }

//        job1.join()
        job2.join()

        coroutineScope.cancel()
    }
}