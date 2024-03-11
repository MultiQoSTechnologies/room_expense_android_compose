package com.example.expensemanagementsystem.screens.authentication.signup

import android.content.Context
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.utils.FirebaseKeyConstants
import com.example.expensemanagementsystem.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

fun signUpAuth(
    context: Context,
    auth: FirebaseAuth,
    viewModel: SignUpViewModel,
    onSignedIn: (FirebaseUser) -> Unit,
) {
    auth.createUserWithEmailAndPassword(viewModel.emailAddress.value, viewModel.password.value)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                // Create a user profile in Firestore
                val userProfile = hashMapOf(
                    FirebaseKeyConstants.Name to viewModel.nameMsg.value,
                    FirebaseKeyConstants.Email to viewModel.emailAddress.value,
                    FirebaseKeyConstants.USER_ID to user?.uid,
                )
                val fireStore = FirebaseFirestore.getInstance()
                fireStore.collection(FirebaseKeyConstants.USERS)
                    .document(user!!.uid)
                    .set(userProfile)
                    .addOnSuccessListener {
                        val keyName = userProfile.keys.elementAt(1)
                        val valueOfName = userProfile.getValue(keyName)

                        val data = viewModel.sessionManagerClass.loginUserData
                        data?.email = user.email.toString()
                        data?.name = (valueOfName?:"")
                        data?.userId = user.uid
                        viewModel.sessionManagerClass.loginUserData = data
                        onSignedIn(user)
                    }
                    .addOnFailureListener {
                        // handle exception
                        it.printStackTrace()
                    }
            } else {
                // Handle sign-up failure
                showToast(context, context.getString(R.string.failed_to_create_an_account))
            }
        }
}