package com.example.expensemanagementsystem.screens.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.expensemanagementsystem.response.AddExpenseModel
import com.example.expensemanagementsystem.response.UserResponse
import com.example.expensemanagementsystem.utils.FirebaseKeyConstants.EXPENSES_KEY
import com.example.expensemanagementsystem.utils.SessionManagerClass
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val sessionManagerClass: SessionManagerClass,
) : ViewModel() {

    val expenseDate: MutableState<String> = mutableStateOf("")
    val expenseDateError: MutableState<String> = mutableStateOf("")
    val expense: MutableState<String> = mutableStateOf("")
    val expenseErrMsg: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val descriptionErrMsg: MutableState<String> = mutableStateOf("")

    val totalExpanses: MutableState<String> = mutableStateOf("")

    val listOfUserIds = mutableListOf<UserResponse>()
    val listOfUserIdsFromExpenses = SnapshotStateList<String>()

    val index: MutableState<Int> =
        mutableStateOf(0)

    val db = FirebaseFirestore.getInstance()
    var users by mutableStateOf<List<UserResponse>>(emptyList())
    var currentUser by mutableStateOf("")

    var expenses by mutableStateOf<MutableList<AddExpenseModel>>(SnapshotStateList())

    fun addExpensesDetails(items: List<AddExpenseModel>) {
        expenses.addAll(items)


        expenses.let { amountData ->
            var totalAmount = 0
            amountData.forEach {
                totalAmount += it.amount.toInt()
                totalExpanses.value = it.amount + it.amount
            }
            totalExpanses.value = totalAmount.toString()
        }

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
    fun removeAllExpensesDetails(onComplete: () -> Unit) {
        db.collection(EXPENSES_KEY).get().addOnSuccessListener {
            it.documents.forEach { document ->
                db.collection(EXPENSES_KEY).document(document.id).delete()
            }
            onComplete()
        }
    }
}
