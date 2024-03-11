package com.example.expensemanagementsystem.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.screens.common.SpacerHorizontal
import com.example.expensemanagementsystem.screens.common.SpacerVertical
import com.example.expensemanagementsystem.ui.theme.darkLoginBgColor
import com.example.expensemanagementsystem.ui.theme.dividerColor
import com.example.expensemanagementsystem.ui.theme.fontSemiBold
import com.example.expensemanagementsystem.ui.theme.lightWhite
import com.example.expensemanagementsystem.ui.theme.loginBgColor
import com.example.expensemanagementsystem.utils.SessionManagerClass

@Composable
fun TabViewTile(
    title: String,
    style: TextStyle,
    backgroundColor: Color,
    borderColor: Color,
    modifier: Modifier,
) {
    Text(
        text = title,
        style = style,
        modifier = modifier
            .padding(end = 10.dp, start = 10.dp, top = 18.dp)
            .clip(shape = RoundedCornerShape(100.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(100.dp), color = borderColor)
            .background(backgroundColor)
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
    )
}


@Composable
fun HomeScreenBottomBar(openBottomSheetDialog: MutableState<Boolean>) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp), horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = CircleShape)
                .background(loginBgColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        openBottomSheetDialog.value = true
                    }
                )
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_icon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun HomeTopBar(
    sessionManagerClass: SessionManagerClass,
    totalExpense: String,
    isShowCheckOut: Boolean = false,
    onCheckOutScreen: () -> Unit,
    onClick: () -> Unit,
    clearExpenses: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(loginBgColor)
                .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(loginBgColor),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hey, ${sessionManagerClass.loginUserData?.name}",
                        fontWeight = FontWeight.W700,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.9f),
                    )
                    if (!isShowCheckOut) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.2f)
                                .size(25.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.power_off),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = {
                                        onClick()
                                    }
                                )
                                .fillMaxWidth()
                                .weight(0.2f)
                                .size(25.dp),
                            colorFilter = ColorFilter.tint(Color.White),
                        )
                    }

                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color.Cyan)
            )
            SpacerVertical(20.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (!isShowCheckOut) Arrangement.Center else Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(if (!isShowCheckOut) 0.1f else 0.7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = if (!isShowCheckOut) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    NormalText(
                        label = stringResource(R.string.total_expenses),
                        style = fontSemiBold.copy(fontSize = 21.sp),
                        textAlign = TextAlign.End,
                    )
                    SpacerVertical(15.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (!isShowCheckOut) Arrangement.End else Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f),
                            horizontalArrangement = if (!isShowCheckOut) Arrangement.Center else Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_rs),
                                contentDescription = "",
                                alignment = Alignment.TopStart,
                                modifier = Modifier
                                    .size(22.dp)
                            )
                            NormalText(
                                label = totalExpense,
                                style = fontSemiBold.copy(fontSize = 20.sp),
                                textAlign = TextAlign.End,
                                modifier = Modifier.offset(y = (-1).dp)
                            )
                        }
                        if (isShowCheckOut) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.8f),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row {
                                    if (isShowCheckOut) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(0.3f)
                                                .height(40.dp)
                                                .clip(shape = RoundedCornerShape(18.dp))
                                                .background(Color.Transparent)
                                                .border(
                                                    width = 1.dp,
                                                    color = lightWhite,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                                .padding(top = 4.dp, bottom = 5.dp)
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null,
                                                    onClick = {
                                                        onCheckOutScreen()
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            NormalText(
                                                label = stringResource(R.string.checkout),
                                                style = fontSemiBold.copy(
                                                    fontSize = 16.sp,
                                                    color = lightWhite
                                                ),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                            )
                                        }
                                        SpacerHorizontal(10.dp)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(0.3f)
                                                .height(40.dp)
                                                .clip(shape = RoundedCornerShape(18.dp))
                                                .background(Color.Transparent)
                                                .border(
                                                    width = 1.dp,
                                                    color = lightWhite,
                                                    shape = RoundedCornerShape(18.dp)
                                                )
                                                .padding(top = 4.dp, bottom = 5.dp)
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null,
                                                    onClick = {
                                                        clearExpenses()
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            NormalText(
                                                label = "Clear",
                                                style = fontSemiBold.copy(
                                                    fontSize = 16.sp,
                                                    color = lightWhite
                                                ),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CheckOutTopBar(
    totalExpense: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(loginBgColor)
                .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(loginBgColor),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(45.dp).clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick ={
                            onClick()
                        }
                    ), contentAlignment = Alignment.Center) {
                        Image(painter = painterResource(id = R.drawable.ic_white_arrow), contentDescription = "", modifier = Modifier
                            .rotate(275f)
                            .size(40.dp))
                    }

                    Text(
                        text = "Checkout",
                        fontWeight = FontWeight.W700,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.9f),
                        textAlign = TextAlign.Center
                    )
                    Box(modifier = Modifier.size(45.dp), contentAlignment = Alignment.Center) {

                    }
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(loginBgColor)
            )
            SpacerVertical(20.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    NormalText(
                        label = stringResource(R.string.total_expenses),
                        style = fontSemiBold.copy(fontSize = 21.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SpacerVertical(15.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_rs),
                                contentDescription = "",
                                alignment = Alignment.TopStart,
                                modifier = Modifier
                                    .size(22.dp)
                            )
                            NormalText(
                                label = totalExpense,
                                style = fontSemiBold.copy(fontSize = 20.sp),
                                textAlign = TextAlign.End,
                                modifier = Modifier.offset(y = (-1).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}