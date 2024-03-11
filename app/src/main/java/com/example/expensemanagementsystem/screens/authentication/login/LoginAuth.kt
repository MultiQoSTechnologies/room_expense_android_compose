package com.example.expensemanagementsystem.screens.authentication.login

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.expensemanagementsystem.response.UserResponse
import com.example.expensemanagementsystem.utils.FirebaseKeyConstants
import com.example.expensemanagementsystem.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

fun LoginAuth(
    context: Context,
    auth: FirebaseAuth,
    viewModel: LoginViewModel,
    onSignedIn: (FirebaseUser) -> Unit,
) {
    auth.signInWithEmailAndPassword(viewModel.emailAddress.value, viewModel.password.value)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val db = FirebaseFirestore.getInstance()
                db.collection(FirebaseKeyConstants.USERS)
                    .whereEqualTo(FirebaseKeyConstants.Email, "${user?.email}")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val dataSessionData: UserResponse = viewModel.sessionManagerClass.loginUserData ?: UserResponse()
                            dataSessionData.email = document.data.getValue(FirebaseKeyConstants.Email) as String
                            dataSessionData.name = document.data.getValue(FirebaseKeyConstants.Name) as String
                            dataSessionData.userId = document.id
                            viewModel.sessionManagerClass.loginUserData = dataSessionData
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error getting documents: ", exception)
                    }
                onSignedIn(user!!)
            } else {
                showToast(context, "Please create an account fist.")
            }
        }
}