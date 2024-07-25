package cn.merlin.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cn.merlin.tools.currentDevice
import cn.merlin.tools.updateSettings

@Composable
fun ChangeDeviceNameDialog(isDialogShow: MutableState<Boolean>) {
    val localDeviceName = mutableStateOf("")
    Dialog(
        onDismissRequest = {
            isDialogShow.value = false
        })
    {
        Surface(
            modifier = Modifier.size(300.dp),
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        ){
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("修改当前设备名（当前${if(cn.merlin.tools.localDeviceName.value == "-1") currentDevice.value.deviceName.value else cn.merlin.tools.localDeviceName.value})")
                TextField(
                    localDeviceName.value,
                    onValueChange = { localDeviceName.value = it },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    label = {
                        Text("在此输入", color = MaterialTheme.colorScheme.onPrimary)
                    })
                Button(onClick = {
                    if(localDeviceName.value.isNotEmpty()) {
                        updateSettings("localDeviceName",localDeviceName.value)
                        cn.merlin.tools.localDeviceName.value = localDeviceName.value
                        isDialogShow.value = false
                    }
                }) {
                    Text("确定")
                }
            }
        }
    }
}