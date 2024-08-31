import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import cn.merlin.bean.model.MessageVIewModel
import cn.merlin.bean.model.RequestViewModel
import cn.merlin.tools.receiveRequestList

@Composable
fun AccessFileDialog(showAccessFileDialog: MutableState<Boolean>){
    Dialog(
        onDismissRequest = {
            showAccessFileDialog.value = false
        }
    ){
        Column{
            Text("信息")
            LazyColumn {
                items(receiveRequestList) {
                    AccessMessageCard(it)
                }
            }
        }
    }
}

@Composable
fun AccessMessageCard(request: RequestViewModel){
    Column {
        Text("${request.requestSenderIpAddress}发送消息")
        Row {
            Button(onClick = {
                receiveRequestList.remove(request)
            }){
                Text("取消")
            }
            Button(onClick = {
                receiveRequestList.remove(request)
            }){
                Text("接收")
            }
        }
    }
}