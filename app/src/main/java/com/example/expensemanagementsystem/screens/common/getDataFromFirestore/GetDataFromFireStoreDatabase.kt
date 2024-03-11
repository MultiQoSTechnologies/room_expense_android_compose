package com.example.expensemanagementsystem.screens.common.getDataFromFirestore

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.expensemanagementsystem.response.AddExpenseModel
import com.example.expensemanagementsystem.response.UserResponse
import com.example.expensemanagementsystem.utils.FirebaseKeyConstants
import com.example.expensemanagementsystem.utils.FirebaseKeyConstants.EXPENSES_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

suspend fun getAllUsersFromFirebase(): List<UserResponse> {
    val users = mutableListOf<UserResponse>()
    val fireStore = Firebase.firestore
    val usersCollection = fireStore.collection(FirebaseKeyConstants.USERS)
    val querySnapshot = usersCollection.get().await()

    for (document in querySnapshot.documents) {
        val user = document?.toObject<UserResponse>()
        user?.let { users.add(it) }
    }
    return users
}

fun getCurrentUserId(): String {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    return currentUser?.uid ?: ""
}


suspend fun getAllExpenses(): SnapshotStateList<AddExpenseModel> {
    val expenses = SnapshotStateList<AddExpenseModel>()
    val fireStore = Firebase.firestore
    val expenseCollection = fireStore.collection(EXPENSES_KEY)
    val querySnapshot = expenseCollection.get().await()

    for (document in querySnapshot.documents) {
        val expens = document?.toObject<AddExpenseModel>()
        expens?.let { expenses.add(it) }
    }
    return expenses
}