package cn.merlin

import cn.merlin.bean.Device
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket

fun main() {
    val socket = Socket()
    try{
        socket.connect(InetSocketAddress("192.168.31.33", 19999), 2000)
        println(socket.isConnected)
        val objectOutputStream = ObjectOutputStream(socket.getOutputStream())
        val objectInputStream = ObjectInputStream(socket.getInputStream())
        objectOutputStream.writeInt(1)
        objectOutputStream.flush()
        val device = objectInputStream.readObject() as Device
        println("${device.deviceName} ${device.deviceIpAddress}")
    }catch (_:Exception){
        println("Lost connect")
    }
}