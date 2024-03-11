package com.example.expensemanagementsystem.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.expensemanagementsystem.ui.theme.loginBgColor

@Composable
fun SpinningProgressBar(modifier: Modifier = Modifier, imageSize: Dp = 30.dp) {
    val count = 12

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = count.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(count * 80, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Canvas(modifier = modifier.size(imageSize)) {

            val canvasWidth = size.width
            val canvasHeight = size.height

            val width = size.width * .3f
            val height = size.height / 8

            val cornerRadius = width.coerceAtMost(height) / 2

            for (i in 0..360 step 360 / count) {
                rotate(i.toFloat()) {
                    drawRoundRect(
//                        color = appColor,
                        color = loginBgColor.copy(alpha = .7f),
                        topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                }
            }

            val coefficient = 360f / count

            for (i in 1..4) {
                rotate((angle.toInt() + i) * coefficient) {
                    drawRoundRect(
//                        color = buttonGradientColor1.copy(alpha = (0.2f + 0.2f * i).coerceIn(0f, 1f)),
                        color = loginBgColor.copy(alpha = (0.2f + 0.2f * i).coerceIn(0f, 1f)),
                        topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                        size = Size(width, height),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                }
            }
        }
    }
}