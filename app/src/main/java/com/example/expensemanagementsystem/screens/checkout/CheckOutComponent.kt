package com.example.expensemanagementsystem.screens.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.response.CheckoutModel
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.screens.common.SpacerHorizontal
import com.example.expensemanagementsystem.ui.theme.fontMedium
import com.example.expensemanagementsystem.ui.theme.fontSemiBold
import com.example.expensemanagementsystem.ui.theme.greenColor
import com.example.expensemanagementsystem.ui.theme.loginBgColor

@Composable
fun CheckOutHeader(){
    Row(
        modifier = Modifier
            .offset(y = (-30).dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NormalText(
            label = stringResource(id = R.string.your_name),
            style = fontSemiBold.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .weight(0.5f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
        SpacerHorizontal(3.dp)
        NormalText(
            label = stringResource(R.string.spent),
            style = fontSemiBold.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .weight(0.4f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        SpacerHorizontal(3.dp)
        NormalText(
            label = stringResource(R.string.distribution),
            style = fontSemiBold.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .weight(0.5f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
        )
        SpacerHorizontal(3.dp)
        NormalText(
            label = stringResource(R.string.share),
            style = fontSemiBold.copy(
                color = Color.Black,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .weight(0.5f),
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun CheckOutFilterDataItem(userListData: CheckoutModel) {
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
                NormalText(
                    label = userListData.name,
                    style = fontMedium.copy(color = Color.Black, fontSize = 17.sp),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f)
                )
                SpacerHorizontal(3.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_rs),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            loginBgColor
                        ),
                        modifier = Modifier.size(18.dp)
                    )

                    NormalText(
                        label = userListData.paid,
                        style = fontMedium.copy(
                            color = loginBgColor,
                            fontSize = 18.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                SpacerHorizontal(3.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_rs),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            Color.Red
                        ),
                        modifier = Modifier.size(18.dp)
                    )

                    NormalText(
                        label = userListData.distribution,
                        style = fontMedium.copy(
                            color = Color.Red,
                            fontSize = 18.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
                SpacerHorizontal(3.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_rs),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            if (userListData.remaining < "0") Color.Red else loginBgColor
                        ),
                        modifier = Modifier.size(18.dp)
                    )

                    NormalText(
                        label = userListData.remaining,
                        style = fontMedium.copy(
                            color = if (userListData.remaining < "0") Color.Red else loginBgColor,
                            fontSize = 17.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}