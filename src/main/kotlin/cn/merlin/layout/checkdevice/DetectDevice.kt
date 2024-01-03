package cn.merlin.layout.checkdevice

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cn.merlin.bean.model.DeviceModel
import cn.merlin.network.DetectDevice

@Composable
fun DetectDevice(
    width: Dp
){
    val deviceList = remember { (mutableStateListOf<DeviceModel> ()) }
    Surface(
        modifier = Modifier.size(width,700.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        for (device in deviceList){
            DeviceCard(device)
        }
    }

}

@Composable
fun DeviceCard(device: DeviceModel){
    Card {
        Row {
            Text(device.deviceName.value)
            Text(device.deviceIpAddress.value)
        }
    }
}