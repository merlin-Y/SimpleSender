package cn.merlin.ui.pages

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
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.tools.savedDeviceIdentifierSet


@Composable
fun detect(
    width: Dp,
    height: Dp,
    savedDeviceList: SnapshotStateList<DeviceViewModel>,
    detectedDeviceList: SnapshotStateList<DeviceViewModel>
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
                        DeviceCard(device,savedDeviceList,detectedDeviceList)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: DeviceViewModel, savedDeviceList: SnapshotStateList<DeviceViewModel>,detectedDeviceList: SnapshotStateList<DeviceViewModel>) {
    Button(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.size(180.dp)
            .padding(top = 20.dp, start = 20.dp),
        onClick = {
            if(!savedDeviceIdentifierSet.contains(device.deviceIdentifier.value)) {
                device.inListType.value = false
                savedDeviceList.add(device)
            }

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