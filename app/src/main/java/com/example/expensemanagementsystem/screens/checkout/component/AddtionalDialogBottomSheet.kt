package com.example.expensemanagementsystem.screens.checkout.component

import android.view.Gravity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.screens.checkout.CheckoutViewModel
import com.example.expensemanagementsystem.screens.common.DeleteButtonWithIconTile
import com.example.expensemanagementsystem.screens.common.GradientButtonWithIcon1
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.screens.common.OutlinedSimpleTextFiled
import com.example.expensemanagementsystem.screens.common.SpacerHorizontal
import com.example.expensemanagementsystem.screens.common.SpacerVertical
import com.example.expensemanagementsystem.screens.common.getDatePicker
import com.example.expensemanagementsystem.ui.theme.fontRegular
import com.example.expensemanagementsystem.ui.theme.fontSemiBold
import com.example.expensemanagementsystem.ui.theme.loginBgColor
import com.example.expensemanagementsystem.utils.ValidationConstants
import com.example.expensemanagementsystem.utils.isEmpty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AdditionalDialogBottomSheet(
    coroutineScope: CoroutineScope,
    viewModel: CheckoutViewModel,
    positiveButtonText: String = stringResource(R.string.add),
    negativeButtonText: String = stringResource(R.string.cancel),
    positiveButtonTextStyle: TextStyle = fontRegular.copy(
        color = Color.White, fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    negativeButtonTextStyle: TextStyle = fontRegular.copy(
        color = Color.Black, fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.CENTER)
        Card(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NormalText(
                    label = stringResource(R.string.add_your_expense),
                    style = fontSemiBold.copy(
                        color = loginBgColor,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                SpacerVertical(15.dp)
                OutlinedSimpleTextFiled(
                    name = viewModel.expenseDate,
                    placeHolder =  stringResource(R.string.dd_mm_yyyy),
                    trailingIcon = R.drawable.ic_calendar, readOnly = true,
                    errorMsg = viewModel.expenseDateError.value, showSmallLabel = true
                ) {
                    coroutineScope.launch {
                        getDatePicker(startDate = "",isWeekMaxDate = false,context, viewModel.expenseDate).show()
                    }
                }

                SpacerVertical(10.dp)
                OutlinedSimpleTextFiled(
                    name = viewModel.description,
                    placeHolder = stringResource(R.string.description),
                    isPasswordField = false,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = viewModel.descriptionErrMsg.value,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                ) {

                }
                SpacerVertical(10.dp)
                OutlinedSimpleTextFiled(
                    name = viewModel.expense,
                    placeHolder = stringResource(R.string.expenses),
                    isPasswordField = false,
                    isNumbersOnly = true,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = viewModel.expenseErrMsg.value,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                ) {

                }
                SpacerVertical(10.dp)

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (negativeButtonText.isNotEmpty()) {
                            DeleteButtonWithIconTile(
                                style = negativeButtonTextStyle,
                                height = 50.dp,
                                borderRadious = 12.dp,
                                label = negativeButtonText,
                                modifier = Modifier
                                    .weight(0.1f)
                                    .border(
                                        width = 1.dp,
                                        color = loginBgColor,
                                        shape = RoundedCornerShape(12.dp)
                                    ), icon = null
                            ) {
                                onDismiss()
                            }
                            SpacerHorizontal(width = 12.dp)
                        }
                        GradientButtonWithIcon1(
                            style = positiveButtonTextStyle,
                            height = 50.dp,
                            icon = null,
                            label = positiveButtonText,
                            iconTint = Color.White,
                            borderRadious = 12.dp,
                            modifier = Modifier.weight(0.1f),
                        ) {
                            //Validation with Add Expenses.
                            validationForAddAdditionalExpenses(viewModel) {
                                onAccept()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun validationForAddAdditionalExpenses(viewModel: CheckoutViewModel, onSuccess: () -> Unit) {
    if (isEmpty(viewModel.expenseDate.value)) {
        viewModel.expenseDateError.value = ValidationConstants.ExpensesDateValidation
    }else if (isEmpty(viewModel.description.value)) {
        viewModel.descriptionErrMsg.value = ValidationConstants.ExpensesTitleValidation
    } else if (isEmpty(viewModel.expense.value)) {
        viewModel.expenseErrMsg.value = ValidationConstants.TotalExpensesValidation
    } else {
        onSuccess()
    }
}