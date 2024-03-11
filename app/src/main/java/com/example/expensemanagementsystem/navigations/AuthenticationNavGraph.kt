package com.example.expensemanagementsystem.navigations

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.expensemanagementsystem.screens.authentication.login.LoginScreen
import com.example.expensemanagementsystem.utils.RootGraph
import com.example.expensemanagementsystem.screens.authentication.signup.SignUpScreen
import com.example.expensemanagementsystem.screens.checkout.CheckOutScreen
import com.google.firebase.auth.FirebaseUser

fun NavGraphBuilder.authenticationNavigationGraph(
    navController: NavHostController,
    userData: MutableState<FirebaseUser?>,
) {
    navigation(
        route = RootGraph.AUTHENTICATION, startDestination = AuthenticationScreens.LoginScreen.route
    ) {
        composable(
            route = AuthenticationScreens.LoginScreen.route,
        ) {
            LoginScreen(navController = navController)
        }

        composable(route = AuthenticationScreens.RegisterScreen.route) {
            SignUpScreen(
                navController = navController, userData = userData
            )
        }

        composable(route = AuthenticationScreens.CheckoutScreen.route) {
            CheckOutScreen(
                navController = navController,
            )
        }
    }
}