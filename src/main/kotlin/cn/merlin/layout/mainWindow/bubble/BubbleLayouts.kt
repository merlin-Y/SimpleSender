package cn.merlin.layout.mainWindow.bubble

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cn.merlin.layout.mainWindow.bubble.BubbleShadow
import cn.merlin.layout.mainWindow.bubble.BubbleState
import cn.merlin.layout.mainWindow.bubble.bubble

@Composable
fun BubbleLayout(
    modifier: Modifier = Modifier,
    bubbleState: BubbleState,
    backgroundColor: Color = Color.White,
    shadow: BubbleShadow? = null,
    borderStroke: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier.bubble(
            bubbleState = bubbleState,
            color = backgroundColor,
            shadow = shadow,
            borderStroke = borderStroke
        )
    ) {
        content()
    }
}