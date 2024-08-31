package cn.merlin.ui.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.merlin.bean.model.DeviceViewModel
import cn.merlin.tools.*
import cn.merlin.tools.DeviceConfiguration.getDetectedList
import cn.merlin.tools.DeviceConfiguration.getSavedList
import cn.merlin.tools.DeviceConfiguration.savedDeviceIdentifierSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun detect(
    width: Dp,
    height: Dp
) {
    val detectedDeviceListView: SnapshotStateList<SnapshotStateList<DeviceViewModel>> =
        remember { mutableStateListOf(mutableStateListOf()) }
    val detectedDeviceListViewIdentifier: MutableList<String> = remember { mutableListOf() }
    val infiniteTransition = rememberInfiniteTransition()

    LaunchedEffect(isDeviceFlushed.value) {
        CoroutineScope(Dispatchers.Default).launch {
            for (device in getDetectedList()) {
                if (!detectedDeviceListViewIdentifier.contains(device.deviceIdentifier.value)) {
                    detectedDeviceListViewIdentifier.add(device.deviceIdentifier.value)
                    if (device.isConnected.value) setOnAvailablePosition(width, device, detectedDeviceListView)
                } else {
                    if (device.isConnected.value) {
                        // 防止重复添加
                        if (!detectedDeviceListView.any { it.any { d -> d.deviceIdentifier.value == device.deviceIdentifier.value } }) {
                            setOnAvailablePosition(width, device, detectedDeviceListView)
                        }
                    } else {
                        // 移除断开连接的设备
                        detectedDeviceListView.forEach { list ->
                            list.removeAll { d -> d.deviceIdentifier.value == device.deviceIdentifier.value }
                        }
                    }
                }
            }
        }
    }

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
            DeviceListViewGrid(detectedDeviceListView, infiniteTransition)
        }
    }
}

@Composable
fun DeviceListViewGrid(detectedDeviceListView: SnapshotStateList<SnapshotStateList<DeviceViewModel>>, infiniteTransition: InfiniteTransition) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(detectedDeviceListView) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                it.forEach {
                    val initValue = (0L..360L).random()
                    val angle1 = infiniteTransition.animateFloat(
                        initialValue = initValue.toFloat(),
                        targetValue = initValue.toFloat() + 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(30000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    val angle2 = infiniteTransition.animateFloat(
                        initialValue = initValue.toFloat() + 180f,
                        targetValue = initValue.toFloat() + 180f + 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(30000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    DeviceCard(it, angle1, angle2)
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: DeviceViewModel, angle1: State<Float>, angle2: State<Float>) {
    val color = MaterialTheme.colorScheme.onSecondaryContainer
    Button(
        shape = CircleShape,
        modifier = when (device.deviceType.value) {
            "desktop" -> Modifier.padding(30.dp).size(200.dp)
            "laptop" -> Modifier.padding(20.dp).size(150.dp)
            "phone" -> Modifier.padding(10.dp).size(100.dp)
            else -> Modifier.padding(10.dp).size(100.dp)
        },
        onClick = {
            if (!savedDeviceIdentifierSet.contains(device.deviceIdentifier.value)) {
                device.inListType.value = false
                getSavedList().add(device)
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val planetRadius = 3.dp.toPx()
                val radians1 = Math.toRadians(angle1.value.toDouble())
                val x1 = (70f * cos(radians1)).toFloat() + size.width / 2
                val y1 = (70f * sin(radians1)).toFloat() + size.height / 2
                val radians2 = Math.toRadians(angle2.value.toDouble())
                val x2 = (70f * cos(radians2)).toFloat() + size.width / 2
                val y2 = (70f * sin(radians2)).toFloat() + size.height / 2
                drawCircle(
                    color = color,
                    radius = planetRadius,
                    center = Offset(x1, y1)
                )
                drawCircle(
                    color = color,
                    radius = planetRadius,
                    center = Offset(x2, y2)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(device.deviceName.value, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
                Text(device.deviceIpAddress.value, color = MaterialTheme.colorScheme.onSecondaryContainer, maxLines = 1)
            }
        }
    }
}

fun setOnAvailablePosition(
    screenWidth: Dp,
    device: DeviceViewModel,
    list: SnapshotStateList<SnapshotStateList<DeviceViewModel>>
) {
    val deviceWidth = getDeviceWidth(device)
    var finished = false
    list.forEach {
        var widthSum = 0.dp
        it.forEach { deviceViewModel ->
            widthSum += getDeviceWidth(deviceViewModel)
        }
        if (widthSum + deviceWidth <= screenWidth) {
            it.add(device)
            finished = true
        }
    }
    if (!finished) {
        val tempList: SnapshotStateList<DeviceViewModel> = mutableStateListOf(device)
        list.add(tempList)
    }
}