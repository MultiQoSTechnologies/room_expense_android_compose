package com.example.expensemanagementsystem.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.response.AddExpenseModel
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.screens.common.SpacerVertical
import com.example.expensemanagementsystem.ui.theme.fontMedium
import com.example.expensemanagementsystem.ui.theme.fontRegular
import com.example.expensemanagementsystem.ui.theme.lightGrey
import com.example.expensemanagementsystem.ui.theme.loginBgColor
import com.example.expensemanagementsystem.utils.DateFormatter
import com.example.expensemanagementsystem.utils.formatDate

@Composable
fun ExpensesDataItem(expensesListData: AddExpenseModel) {
    Card(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NormalText(
                        label = expensesListData.desc,
                        style = fontMedium.copy(color = Color.Black, fontSize = 17.sp),
                        overflow = TextOverflow.Ellipsis,
                        maxLine = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SpacerVertical(3.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if ((expensesListData.timestamp.toInt() != 0) && (expensesListData.timestamp.toString()
                                    .isNotEmpty()) && (expensesListData.timestamp.toString() != "0")
                            ) {
                                NormalText(
                                    label = formatDate(
                                        expensesListData.timestamp.toString(),
                                        defaultFormat = DateFormatter.DD_MM_YYYY
                                    ),
                                    style = fontRegular.copy(color = lightGrey, fontSize = 13.sp),
                                    overflow = TextOverflow.Ellipsis,
                                )
                            } else {
                                NormalText(
                                    label = "0",
                                    style = fontRegular.copy(color = lightGrey, fontSize = 13.sp),
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .size(6.dp)
                                    .clip(shape = CircleShape)
                                    .background(
                                        lightGrey
                                    )
                            ) {}
                            NormalText(
                                label = "by." + expensesListData.name,
                                style = fontRegular.copy(color = lightGrey, fontSize = 13.sp),
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_rs),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(
                                    loginBgColor
                                ),
                                modifier = Modifier.size(20.dp)
                            )

                            NormalText(
                                label = expensesListData.amount,
                                style = fontMedium.copy(color = loginBgColor, fontSize = 18.sp),
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}