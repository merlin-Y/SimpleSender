package cn.merlin.layout.mainWindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.model.DeviceModel


@Composable
fun detect(
    width: Dp,
    height: Dp,
    detectedDeviceList: SnapshotStateList<DeviceModel>,
    localDeviceList: SnapshotStateList<DeviceModel>
) {

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
                for (device in detectedDeviceList) {
                    item {
                        DeviceCard(device,localDeviceList)
                    }
                }
            }
        }
    }

}

@Composable
fun DeviceCard(device: DeviceModel,localDeviceList: SnapshotStateList<DeviceModel>) {
    Button(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.size(if (device.deviceIpAddress.value == "192.168.31.216") 180.dp else if (device.deviceIpAddress.value == "192.168.31.10") 160.dp else 120.dp)
            .padding(top = 20.dp, start = 20.dp),
        onClick = {
            device.inListType.value = false
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