package com.example.expensemanagementsystem.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.navigations.AuthenticationScreens
import com.example.expensemanagementsystem.response.AddExpenseModel
import com.example.expensemanagementsystem.response.UserResponse
import com.example.expensemanagementsystem.screens.common.ConfirmationDialogBottomSheet
import com.example.expensemanagementsystem.screens.common.NormalText
import com.example.expensemanagementsystem.screens.common.deleteConfirmationDialog
import com.example.expensemanagementsystem.screens.common.getDataFromFirestore.getAllExpenses
import com.example.expensemanagementsystem.screens.common.getDataFromFirestore.getAllUsersFromFirebase
import com.example.expensemanagementsystem.screens.common.getDataFromFirestore.getCurrentUserId
import com.example.expensemanagementsystem.screens.common.getTimeMils
import com.example.expensemanagementsystem.ui.theme.fontMedium
import com.example.expensemanagementsystem.ui.theme.fontSemiBold
import com.example.expensemanagementsystem.ui.theme.lightGrey
import com.example.expensemanagementsystem.ui.theme.loginBgColor
import com.example.expensemanagementsystem.utils.AuthFirebase
import com.example.expensemanagementsystem.utils.DateFormatter
import com.example.expensemanagementsystem.utils.SessionManagerClass
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    userData: MutableState<FirebaseUser?>,
    sessionManagerClass: SessionManagerClass,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val coroutineScope = rememberCoroutineScope()
    val auth = AuthFirebase.auth
    //Get All Users with short by current user.
    LaunchedEffect(Unit) {
        viewModel.users = getAllUsersFromFirebase()
        viewModel.currentUser = getCurrentUserId()
        val sortedList = mutableListOf<UserResponse>()
        val currentUserIndex =
            viewModel.users.indexOfFirst { it.name == viewModel.currentUser }

        if (currentUserIndex != -1) {
            sortedList.add(viewModel.users[currentUserIndex])
            viewModel.users.filterIndexed { index, _ -> index != currentUserIndex }
                .sortedBy { it.name }
                .forEach { sortedList.add(it) }
        } else {
            sortedList.addAll(viewModel.users.sortedBy { it.name })
            viewModel.listOfUserIds.addAll(viewModel.users.sortedBy { it.userId })
        }
        sortedList.add(0, UserResponse(name = "All"))

        viewModel.users = sortedList
    }

    //Get All Expenses.
    LaunchedEffect(Unit) {
        viewModel.expenses = getAllExpenses()
        val sortedList = SnapshotStateList<AddExpenseModel>()
        val currentUserIndex =
            viewModel.expenses.indexOfFirst { it.userId == viewModel.currentUser }

        if (currentUserIndex != -1) {
            sortedList.add(viewModel.expenses[currentUserIndex])
            viewModel.expenses.filterIndexed { index, _ -> index != currentUserIndex }
                .sortedBy {
                    it.userId
                    viewModel.listOfUserIdsFromExpenses.add(it.userId)
                }
                .forEach { sortedList.add(it) }
        } else {
            sortedList.addAll(viewModel.expenses.sortedBy { it.userId })
        }
        viewModel.expenses = sortedList

        // Total of the  all amounts
        viewModel.expenses.let { amountData ->
            var totalAmount = 0
            amountData.forEach {
                totalAmount += it.amount.toInt()
                viewModel.totalExpanses.value = it.amount + it.amount
            }
            viewModel.totalExpanses.value = totalAmount.toString()
        }
    }
    val openBottomSheetDialog = remember {
        mutableStateOf(false)
    }

    fun onClickDismissDialog() {
        openBottomSheetDialog.value = false
    }

    if (openBottomSheetDialog.value) {
        ConfirmationDialogBottomSheet(
            coroutineScope,
            viewModel = viewModel,
            onDismiss = {
                onClickDismissDialog()
            }
        ) {
            val getMilSec = getTimeMils(viewModel.expenseDate.value, DateFormatter.DD_MM_YYYY)
            val items = listOf(
                AddExpenseModel(
                    amount = viewModel.expense.value,
                    desc = viewModel.description.value,
                    timestamp = getMilSec.toLong(),
                    userId = sessionManagerClass.loginUserData?.userId.toString(),
                    name = sessionManagerClass.loginUserData?.name.toString(),
                    additional = false
                ),
            )
            viewModel.addExpensesDetails(items)
            openBottomSheetDialog.value = false
            viewModel.expense.value = ""
            viewModel.description.value = ""
            viewModel.expenseDate.value = ""

        }
    }

    val openConfirmationDialog = remember {
        mutableStateOf(false)
    }

    fun onClickConfirmationDismissDialog() {
        openConfirmationDialog.value = false
    }

    if (openConfirmationDialog.value) {
        deleteConfirmationDialog(
            dialogTitle = "Are you sure you want to delete your expense.",
            onDismiss = { onClickConfirmationDismissDialog() },
            onAccept = {
                viewModel.removeAllExpensesDetails {
                    coroutineScope.launch {
                        viewModel.expenses = getAllExpenses()

                        viewModel.expenses.let { amountData ->
                            var totalAmount = 0
                            amountData.forEach {
                                totalAmount += it.amount.toInt()
                                viewModel.totalExpanses.value = it.amount + it.amount
                            }
                            viewModel.totalExpanses.value = totalAmount.toString()
                        }
                    }
                }

                openConfirmationDialog.value = false
            }
        )
    }

    val openConfirmationLogoutDialog = remember {
        mutableStateOf(false)
    }

    fun onClickConfirmationLogoutDismissDialog() {
        openConfirmationLogoutDialog.value = false
    }

    if (openConfirmationLogoutDialog.value) {
        deleteConfirmationDialog(
            dialogTitle = "Are you sure you want to Logout.",
            onDismiss = { onClickConfirmationLogoutDismissDialog() },
            onAccept = {
                auth.signOut()
                userData.value = null
                viewModel.sessionManagerClass.clearSessionData()
                navController.navigate(AuthenticationScreens.LoginScreen.route)

                openConfirmationDialog.value = false
            }
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            HomeTopBar(sessionManagerClass,
                totalExpense = viewModel.totalExpanses.value,
                isShowCheckOut = true,
                onCheckOutScreen = {
                    navController.navigate(AuthenticationScreens.CheckoutScreen.route)
                },
                onClick = {
                    openConfirmationLogoutDialog.value = true
                },
                clearExpenses = {
                    openConfirmationDialog.value = true
                }
            )
        },
        content = { HomeScreenBody(viewModel, coroutineScope) },
        bottomBar = {
            HomeScreenBottomBar(openBottomSheetDialog)
        }
    )
}

@Composable
fun HomeScreenBody(
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Column(
        modifier = Modifier
            .padding(top = 220.dp)
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .offset(y = (-40).dp)
                    .fillMaxWidth()
                    .height(75.dp)
            ) {
                items(viewModel.users.size) { index ->
                    val userListData = viewModel.users[index]
                    Column {
                        TabViewTile(
                            title = userListData.name,
                            backgroundColor = Color.Transparent,
                            borderColor = if (viewModel.index.value == index) loginBgColor else lightGrey,
                            modifier = Modifier.selectable(
                                interactionSource = interactionSource,
                                indication = null,
                                selected = viewModel.index.value == index,
                                onClick = {
                                    viewModel.index.value = index
                                    viewModel.expenses = viewModel.expenses.filter {
                                        it.userId == userListData.userId
                                    }.toMutableList()
                                    if (viewModel.index.value == index) {
                                        coroutineScope.launch {
                                            viewModel.expenses = getAllExpenses()
                                            viewModel.expenses = viewModel.expenses.filter {
                                                it.userId == userListData.userId
                                            }.toMutableList()
                                        }
                                    }
                                    if (userListData.name == "All") {
                                        coroutineScope.launch {
                                            viewModel.expenses = getAllExpenses()
                                        }
                                    }
                                }
                            ),
                            style = fontMedium.copy(color = if (viewModel.index.value == index) loginBgColor else lightGrey)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .offset(y = (-45).dp)
                    .padding(bottom = 10.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.expenses.isNotEmpty()) {
                    NormalText(
                        label = stringResource(id = R.string.expenses),
                        style = fontSemiBold.copy(
                            color = Color.Black,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .weight(0.5f),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
            }

            if (viewModel.expenses.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-40).dp)
                        .padding(horizontal = 15.dp)
                ) {
                    items(viewModel.expenses.size) { index ->
                        val dataListData = viewModel.expenses[index]
                        ExpensesDataItem(dataListData)
                    }
                    items(1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {}
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp), contentAlignment = Alignment.Center
                ) {
                    NormalText(
                        label = stringResource(R.string.no_data_found),
                        style = fontMedium.copy(color = lightGrey, fontSize = 16.sp)
                    )
                }
            }
        }
    }
}