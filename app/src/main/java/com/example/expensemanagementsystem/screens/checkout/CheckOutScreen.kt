package com.example.expensemanagementsystem.screens.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.response.AddExpenseModel
import com.example.expensemanagementsystem.response.CheckoutModel
import com.example.expensemanagementsystem.response.MinFabItem
import com.example.expensemanagementsystem.screens.checkout.component.AdditionalDialogBottomSheet
import com.example.expensemanagementsystem.screens.checkout.component.MultiFloatingButton
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.screens.common.SpacerVertical
import com.example.expensemanagementsystem.screens.common.getTimeMils
import com.example.expensemanagementsystem.screens.home.CheckOutTopBar
import com.example.expensemanagementsystem.ui.theme.PurpleGrey40
import com.example.expensemanagementsystem.ui.theme.fontMedium
import com.example.expensemanagementsystem.ui.theme.fontRegular
import com.example.expensemanagementsystem.ui.theme.fontSemiBold
import com.example.expensemanagementsystem.utils.DateFormatter.DD_MM_YYYY
import com.example.expensemanagementsystem.utils.MultiFloatingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckOutScreen(
    navController: NavHostController,
    viewModel: CheckoutViewModel = hiltViewModel(),
) {

    var multiFloatingState by remember {
        mutableStateOf(MultiFloatingState.Collapsed)
    }
    val items = listOf(
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.ic_share),
            label = "Share",
            identifier = "ShareFab"
        ),
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.ic_notepad),
            label = "Add additional expenses",
            identifier = "AdditionalExpensesFab"
        ),
    )
    val coroutineScope = rememberCoroutineScope()

    val openBottomSheetDialog = remember {
        mutableStateOf(false)
    }

    fun onClickDismissDialog() {
        openBottomSheetDialog.value = false
    }

    if (openBottomSheetDialog.value) {
        AdditionalDialogBottomSheet(
            coroutineScope,
            viewModel = viewModel,
            onDismiss = {
                onClickDismissDialog()
            }
        ) {
            val getMilSec = getTimeMils(viewModel.expenseDate.value, DD_MM_YYYY)
            val addExpenses = listOf(
                AddExpenseModel(
                    amount = viewModel.expense.value,
                    desc = viewModel.description.value,
                    timestamp = getMilSec.toLong(),
                    userId = viewModel.sessionManagerClass.loginUserData?.userId.toString(),
                    name = viewModel.sessionManagerClass.loginUserData?.name.toString(),
                    additional = true
                ),
            )
            val checkoutList by mutableStateOf<MutableList<CheckoutModel>>(SnapshotStateList())

            viewModel.addExpensesDetails(addExpenses,checkoutList)
            openBottomSheetDialog.value = false

            viewModel.expense.value = ""
            viewModel.description.value = ""
            viewModel.expenseDate.value = ""
        }
    }
    val view = LocalView.current
    val context = LocalContext.current as Activity

    Scaffold(containerColor = Color.White, topBar = {
        CheckOutTopBar(
            totalExpense = viewModel.totalExpanses.value.toString(),
            onClick = {
                navController.popBackStack()
            },
        )
    }, content = {
        CheckoutScreenBody(viewModel)
    },
        floatingActionButton = {
            MultiFloatingButton(
                multiFloatingState = multiFloatingState,
                items = items,
                onMultiFebStateChange = {
                    multiFloatingState = it
                },
                onItemClickChange = {
                    if (it == "Share") {
                        multiFloatingState = MultiFloatingState.Collapsed
                        coroutineScope.launch {
                            delay(1500)
                            share(screenShot(view), context)
                        }
                    } else {
                        openBottomSheetDialog.value = true
                        multiFloatingState = MultiFloatingState.Collapsed
                    }
                }
            )
        }
    )
}

@Composable
fun CheckoutScreenBody(viewModel: CheckoutViewModel) {
    Column(
        modifier = Modifier
            .padding(top = 210.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 10.dp, start = 15.dp, end = 15.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-5).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalText(
                        label = stringResource(R.string.total_amount_except_additional),
                        style = fontRegular.copy(color = PurpleGrey40, fontSize = 16.sp),
                        modifier = Modifier
                    )
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rs),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(
                                Color.Black
                            ),
                            modifier = Modifier.size(18.dp)
                        )
                        NormalText(
                            label = if (viewModel.totalAddiExpense.toString() != "0") viewModel.totalAddiExpense.value.toString() else "0",
                            style = fontMedium.copy(color = Color.Black, fontSize = 17.sp),
                            modifier = Modifier.offset(y = (-1).dp)
                        )
                    }
                }
                SpacerVertical(5.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-5).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalText(
                        label = stringResource(R.string.additional_expenses),
                        style = fontRegular.copy(color = PurpleGrey40, fontSize = 16.sp),
                        modifier = Modifier
                    )
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rs),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(
                                Color.Black
                            ),
                            modifier = Modifier.size(18.dp)
                        )
                        NormalText(
                            label = if (viewModel.sumOfAdditionalExpense.value != 0) viewModel.sumOfAdditionalExpense.value.toString() else "0",
                            style = fontMedium.copy(color = Color.Black, fontSize = 17.sp),
                            modifier = Modifier.offset(y = (-1).dp)
                        )
                    }
                }

                SpacerVertical(7.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-5).dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NormalText(
                        label = stringResource(R.string.grand_total),
                        style = fontSemiBold.copy(color = Color.Black, fontSize = 18.sp),
                        modifier = Modifier
                    )
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rs),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(
                                Color.Black
                            ),
                            modifier = Modifier.size(19.dp)
                        )
                        NormalText(
                            label = viewModel.totalExpanses.value.toString(),
                            style = fontSemiBold.copy(color = Color.Black, fontSize = 18.sp),
                            modifier = Modifier.offset(y = (-2.5).dp)
                        )
                    }
                }
            }
            SpacerVertical(50.dp)
            CheckOutHeader()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-15).dp)
                    .padding(horizontal = 1.dp)
            ) {
                items(viewModel.checkoutList.size) { index ->
                    val userListData = viewModel.checkoutList[index]
                    CheckOutFilterDataItem(userListData)
                }
                items(1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {}
                }
            }
        }
    }
}

//Capture Screenshot
fun screenShot(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap

}

//Share Screenshot
fun share(bitmap: Bitmap, context: Activity) {
    val pathofBmp = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap, "title", null
    )
    val uri = Uri.parse(pathofBmp)
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.setType("image/*")
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Star App")
    shareIntent.putExtra(Intent.EXTRA_TEXT, "")
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    context.startActivity(Intent.createChooser(shareIntent, "hello hello"))
}