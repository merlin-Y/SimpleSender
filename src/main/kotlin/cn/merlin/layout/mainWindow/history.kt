package cn.merlin.layout.mainWindow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun history(
    width: Dp,
    height: Dp
){
    Surface(
        modifier = Modifier.size(width, height),
        color = MaterialTheme.colorScheme.secondary
    ){
        Column {
            Text(
                modifier = Modifier.size(width, 40.dp).padding(start = 20.dp),
                text = "发送历史",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                maxLines = 1,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.background(MaterialTheme.colorScheme.tertiary).height(2.dp).width(width))
            Surface {

            }
        }
    }
}