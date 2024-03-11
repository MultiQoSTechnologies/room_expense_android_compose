package com.example.expensemanagementsystem.utils


object ValidationConstants {
    const val NameBlank = "Please enter your name."
    const val EmailBlank = "Please enter your email address."
    const val EmailValid = "Please enter valid email address."
    const val PasswordBlank = "Please enter your password."
    const val PasswordValid = "Password length must be at least 8 characters and must not contain spaces."
    const val ExpensesDateValidation = "Please select your expenses date"
    const val ExpensesTitleValidation = "Please enter your expenses title"
    const val TotalExpensesValidation = "Please enter your expense amount"
}

object FirebaseKeyConstants {
    const val Name = "name"
    const val Email = "email"
    const val USERS = "users"
    const val EXPENSES_KEY = "expenses"
    const val USER_ID = "userId"
}

enum class MultiFloatingState {
    Expanded, Collapsed
}