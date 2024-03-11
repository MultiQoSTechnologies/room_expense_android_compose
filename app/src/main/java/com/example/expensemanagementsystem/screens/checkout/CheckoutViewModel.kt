package com.example.expensemanagementsystem.screens.checkout

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagementsystem.response.AddExpenseModel
import com.example.expensemanagementsystem.response.CheckoutModel
import com.example.expensemanagementsystem.response.UserResponse
import com.example.expensemanagementsystem.screens.common.getDataFromFirestore.getAllExpenses
import com.example.expensemanagementsystem.screens.common.getDataFromFirestore.getAllUsersFromFirebase
import com.example.expensemanagementsystem.screens.common.getDataFromFirestore.getCurrentUserId
import com.example.expensemanagementsystem.utils.FirebaseKeyConstants.EXPENSES_KEY
import com.example.expensemanagementsystem.utils.SessionManagerClass
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    val sessionManagerClass: SessionManagerClass,
) : ViewModel() {
    val expenseDate: MutableState<String> = mutableStateOf("")
    val expenseDateError: MutableState<String> = mutableStateOf("")
    val expense: MutableState<String> = mutableStateOf("")
    val expenseErrMsg: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val descriptionErrMsg: MutableState<String> = mutableStateOf("")

    var allExpenses by mutableStateOf<MutableList<AddExpenseModel>>(SnapshotStateList())

    var checkoutList by mutableStateOf<MutableList<CheckoutModel>>(SnapshotStateList())
    var sumOfAdditionalExpense = mutableStateOf(0)
    var totalAddiExpense = mutableStateOf(0)
    val intArray = arrayListOf<Int>(0)

    init {
        //gel data
        getAllUsers()
        viewModelScope.launch {
            val expenseList: MutableList<AddExpenseModel> = getAllExpenses()
            allExpenses = expenseList
            totalExpanses.value = allExpenses.sumOf { it.amount.toInt() }
            if (users.size != 0) {
                splitExpansesBetweenUsers.value = (totalExpanses.value / users.size).toString()
            }

            val expenseUserIds: Set<String> = allExpenses.map { it.userId }.toSet()
            val usersNotInExpenseList: List<UserResponse> =
                users.filter { !expenseUserIds.contains(it.userId) }

            usersNotInExpenseList.forEach {
                expenseList.add(
                    AddExpenseModel(
                        amount = "0",
                        name = it.name,
                        userId = it.userId
                    )
                )
            }

            val temp = expenseList.groupBy { it.userId }
            val totalExpanse = if (users.size != 0) {
                (totalExpanses.value / users.size)
            } else {
                totalExpanses.value
            }
            val tempCheckoutList = mutableListOf<CheckoutModel>()

            temp.entries.forEach { expense ->
                val paid = expense.value.map { it.amount.toInt() }.sumOf { it }
                val additional =
                    expense.value.map { if (it.additional) it.amount.toInt() else 0 }.sumOf { it }
                sumOfAdditionalExpense.value += additional
                intArray.add(additional)

                tempCheckoutList.add(
                    CheckoutModel(
                        name = expense.value[0].name,
                        paid = paid.toString(),
                        distribution = totalExpanse.toString(),
                        remaining = (paid - totalExpanse).toString(),
                    )
                )
            }
            totalAddiExpense.value = (totalExpanses.value - sumOfAdditionalExpense.value)
            checkoutList.addAll(tempCheckoutList)
        }
    }

    private fun getAllUsers() = viewModelScope.launch {
        val cUserId = getCurrentUserId()
        val allUsers = getAllUsersFromFirebase().toMutableList()
        val cUser = allUsers.first { it.userId == cUserId }
        allUsers.remove(cUser)
        allUsers.add(0, cUser)
        users = allUsers
    }

    val totalExpanses: MutableState<Int> = mutableStateOf(0)

    var splitExpansesBetweenUsers: MutableState<String> = mutableStateOf("")

    val db = FirebaseFirestore.getInstance()
    var users by mutableStateOf<List<UserResponse>>(emptyList())

    fun addExpensesDetails(
        items: List<AddExpenseModel>,
        checkoutListData: MutableList<CheckoutModel>,
    ) {

        //Update the data
        allExpenses.addAll(items)
        checkoutList.addAll(checkoutListData)

        allExpenses.let { amountData ->
            var totalAmount = 0
            amountData.forEach {
                totalAmount += it.amount.toInt()
                totalExpanses.value = it.amount.toInt() + it.amount.toInt()
            }
            totalExpanses.value = totalAmount
        }


        val temp = allExpenses.groupBy { it.userId }
        val totalExpanse = if (users.size != 0) {
            (totalExpanses.value / users.size)
        } else {
            totalExpanses.value
        }
        val tempCheckoutList = mutableListOf<CheckoutModel>()

        temp.entries.forEach { expense ->
            val paid = expense.value.map { it.amount.toInt() }.sumOf { it }
            val additional =
                expense.value.map { if (it.additional) it.amount.toInt() else 0 }.sumOf { it }
            sumOfAdditionalExpense.value += additional
            intArray.add(additional)

            tempCheckoutList.add(
                CheckoutModel(
                    name = expense.value[0].name,
                    paid = paid.toString(),
                    distribution = totalExpanse.toString(),
                    remaining = (paid - totalExpanse).toString(),
                )
            )
        }

        checkoutList = tempCheckoutList

        for (item in items) {
            db.collection(EXPENSES_KEY)
                .add(item)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
}
