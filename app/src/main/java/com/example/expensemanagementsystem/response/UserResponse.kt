package com.example.expensemanagementsystem.response

import androidx.compose.ui.graphics.ImageBitmap

data class UserResponse(
    var email: String = "",
    var name: String = "",
    var profile_pic: String = "",
    var userId: String = "",
)

data class AddExpenseModel(
    var amount: String = "",
    var desc: String = "",
    var timestamp: Long = 0,
    var userId: String = "",
    var name: String = "",
    var additional: Boolean = false,
)

data class CheckoutModel(
    val name: String = "",
    val paid: String = "",
    val distribution: String = "",
    val remaining: String = "",
)

class MinFabItem(
    val icon: ImageBitmap,
    val label: String,
    val identifier: String,
)