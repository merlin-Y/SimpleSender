package cn.merlin.layout.mainWindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.Device
import cn.merlin.bean.model.DeviceModel

@Composable
fun detect(
    localDeviceList: SnapshotStateList<DeviceModel>,
    width: Dp,
    height: Dp
) {
    val deviceList = SnapshotStateList<DeviceModel>()
    deviceList.add(DeviceModel(Device(deviceName = "mac book", deviceIpAddress = "192.168.31.216")))
    deviceList.add(DeviceModel(Device(deviceName = "DESKTOP-MERLIN", deviceIpAddress = "192.168.31.24")))
    deviceList.add(DeviceModel(Device(deviceName = "Redmi k50", deviceIpAddress = "192.168.31.36")))
    deviceList.add(DeviceModel(Device(deviceName = "Laptop", deviceIpAddress = "192.168.31.10")))
    deviceList.add(DeviceModel(Device(deviceName = "mac book", deviceIpAddress = "192.168.31.216")))
    deviceList.add(DeviceModel(Device(deviceName = "mac book", deviceIpAddress = "192.168.31.216")))

    Surface(
        modifier = Modifier.size(width, height),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column {
            Text(
                modifier = Modifier.size(width, 40.dp).padding(start = 20.dp),
                text = "发现设备",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                maxLines = 1,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            LazyColumn(
                modifier = Modifier.padding(top = 20.dp, start = 20.dp)
            ) {
                for (device in deviceList) {
                    item {
                        DeviceCard(localDeviceList,device)
                    }
                }
            }
        }
    }

}

@Composable
fun DeviceCard(localDeviceList: MutableList<DeviceModel>, device: DeviceModel) {
    Button(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.size(if (device.deviceIpAddress.value == "192.168.31.216") 180.dp else if (device.deviceIpAddress.value == "192.168.31.10") 160.dp else 120.dp)
            .padding(top = 20.dp, start = 20.dp),
        onClick = {
            device.inListType.value = 1
            localDeviceList.add(device)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(device.deviceName.value, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
            Text(device.deviceIpAddress.value, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
        }

    }
}