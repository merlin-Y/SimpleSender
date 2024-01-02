package cn.merlin.network

import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors

class CheckDevice(corePoolSize: Int) {
    val threadPool = Executors.newFixedThreadPool(corePoolSize)

    fun DeviceScanner(ip: String) {
        try{
            val address = InetAddress.getByName(ip)
            if(address.isReachable(3000)){
                val socket = Socket()
                socket.connect(InetSocketAddress(ip,19998),1000)
                if(socket.isConnected){
                    val deviceMode = DeviceModel(Device())
                }
            }
        }catch (_: Exception){

        }
    }
}