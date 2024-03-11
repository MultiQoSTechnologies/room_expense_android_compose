package com.example.expensemanagementsystem.screens.checkout.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.expensemanagementsystem.response.MinFabItem
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.ui.theme.fontSemiBold
import com.example.expensemanagementsystem.ui.theme.loginBgColor
import com.example.expensemanagementsystem.utils.MultiFloatingState
import com.example.expensemanagementsystem.utils.showToast

@Composable
fun MultiFloatingButton(
    multiFloatingState: MultiFloatingState,
    items: List<MinFabItem>,
    onMultiFebStateChange: (MultiFloatingState) -> Unit,
    onItemClickChange: (String) -> Unit,
) {
    val transition = updateTransition(targetState = multiFloatingState, label = "transtion")
    val rotate by transition.animateFloat(label = "rotate") { if (it == MultiFloatingState.Expanded) 110f else 315f }
    val fabScale by transition.animateFloat(label = "FabScale") { if (it == MultiFloatingState.Expanded) 40f else 0f }
    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 50) }) { if (it == MultiFloatingState.Expanded) 1f else 0f }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (transition.currentState == MultiFloatingState.Expanded) {
            items.forEach { it ->
                MinFab(
                    item = it,
                    onMinFebItemClick = {
                        onItemClickChange(it.label)
                    },
                    alpha = alpha,
                    fabScale = fabScale
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
        FloatingActionButton(
            shape = CircleShape, containerColor = loginBgColor, onClick = {
                onMultiFebStateChange(
                    if (transition.currentState == MultiFloatingState.Expanded) {
                        MultiFloatingState.Collapsed
                    } else {
                        MultiFloatingState.Expanded
                    }
                )
            }, modifier = Modifier.rotate(rotate)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "s",
                modifier = Modifier.rotate(rotate).size(30.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun MinFab(
    item: MinFabItem,
    alpha: Float,
    fabScale: Float,
    showLabel: Boolean = true,
    onMinFebItemClick: (MinFabItem) -> Unit,
) {
    val buttonColor = loginBgColor.copy(alpha = 0.7f)
    val shadow = Color.Black.copy(alpha = 0.5f)
    Row() {
        if (showLabel) {
            NormalText(
                label = item.label,
                style = fontSemiBold.copy(color = Color.Black),
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(targetValue = alpha, animationSpec = tween(50)).value
                    )
                    .background(MaterialTheme.colors.surface)
                    .padding(start = 6.dp, end = 6.dp, top = 6.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
        Canvas(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(32.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    onClick = {
                        onMinFebItemClick.invoke(item)
                    },
                    indication = rememberRipple(
                        bounded = false, radius = 20.dp, color = Color.Transparent
                    ),
                )
                .padding(5.dp)
        ) {
            drawCircle(
                color = shadow, radius = fabScale, center = Offset(
                    center.x + 2f, center.y + 2f
                )
            )
            drawCircle(
                color = buttonColor, radius = fabScale
            )
            drawImage(
                image = item.icon, topLeft = Offset(
                    center.x - (item.icon.width / 2), center.y - (item.icon.width / 2)
                ), alpha = alpha,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}