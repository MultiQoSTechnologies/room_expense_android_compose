package com.example.expensemanagementsystem.navigations

import com.example.expensemanagementsystem.utils.ConstantAppScreenName

sealed class AuthenticationScreens(val route: String) {

    object LoginScreen : AuthenticationScreens(ConstantAppScreenName.LOGIN_SCREEN)

    object RegisterScreen : AuthenticationScreens(ConstantAppScreenName.REGISTER_SCREEN)
    object CheckoutScreen : AuthenticationScreens(ConstantAppScreenName.CHECKOUT_SCREEN)
}