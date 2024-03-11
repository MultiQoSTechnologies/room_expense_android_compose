package com.example.expensemanagementsystem.screens.authentication.login

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.expensemanagementsystem.R
import com.example.expensemanagementsystem.navigations.AuthenticationScreens
import com.example.expensemanagementsystem.screens.common.ChangeStatusBarColor
import com.example.expensemanagementsystem.screens.common.GradientButton
import com.example.expensemanagementsystem.screens.common.OutlinedSimpleTextFiled
import com.example.expensemanagementsystem.screens.common.SpacerVertical
import com.example.expensemanagementsystem.ui.theme.fontMedium
import com.example.expensemanagementsystem.ui.theme.lightGrey
import com.example.expensemanagementsystem.ui.theme.loginBgColor
import com.example.expensemanagementsystem.utils.AuthFirebase
import com.example.expensemanagementsystem.utils.RootGraph
import com.example.expensemanagementsystem.utils.ValidationConstants
import com.example.expensemanagementsystem.utils.isEmpty
import com.example.expensemanagementsystem.utils.isValidEmail
import com.example.expensemanagementsystem.utils.isValidPassword

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    ChangeStatusBarColor()
    val context = LocalContext.current
    val auth = AuthFirebase.auth

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(loginBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontWeight = FontWeight.W700,
                    fontSize = 25.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
                    .background(loginBgColor), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(70.dp)
                        .clip(shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
                        .background(loginBgColor)
                        .offset(x = (-50).dp)
                ) {
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .height(70.dp)
                        .clip(shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
                        .background(loginBgColor)
                        .offset(x = 50.dp)
                ) {
                }
            }
        }

        Card(
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(3.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier
                .padding(top = 150.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SpacerVertical(10.dp)
                OutlinedSimpleTextFiled(
                    name = loginViewModel.emailAddress,
                    placeHolder = stringResource(R.string.email),
                    isPasswordField = false,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = loginViewModel.emailErrMsg.value
                ) {

                }
                SpacerVertical(10.dp)
                OutlinedSimpleTextFiled(
                    name = loginViewModel.password,
                    placeHolder = stringResource(R.string.password),
                    isPasswordField = true,
                    horizontal = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    errorMsg = loginViewModel.passwordErrMsg.value
                ) {

                }
                SpacerVertical(50.dp)

                GradientButton(
                    style = fontMedium.copy(
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        color = Color.White
                    ),
                    label = stringResource(R.string.login),
                    modifier = Modifier.fillMaxWidth(),
                    borderRadious = 100.dp
                ) {
                    //Validation with Login Success.
                    validationForLogin(loginViewModel) {
                        LoginAuth(
                            context, auth, loginViewModel
                        ) { signedInUser ->
                            navigateToHomePage(navController)
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.don_t_have_an_account),
                        fontWeight = FontWeight.W400,
                        fontSize = 15.sp,
                        color = lightGrey,
                        fontFamily = FontFamily(Font(R.font.urbanist_regular)),
                    )
                    Text(
                        text = stringResource(R.string.createAccount),
                        fontWeight = FontWeight.W700,
                        fontSize = 15.sp,
                        color = loginBgColor,
                        fontFamily = FontFamily(Font(R.font.urbanist_semibold)),
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                navController.navigate(AuthenticationScreens.RegisterScreen.route)
                            }
                        )
                    )
                }
                SpacerVertical(10.dp)
            }
        }
    }
}

fun validationForLogin(signViewModel: LoginViewModel, onSuccess: () -> Unit) {
    if (isEmpty(signViewModel.emailAddress.value)) {
        signViewModel.emailErrMsg.value = ValidationConstants.EmailBlank
    } else if (!isValidEmail(signViewModel.emailAddress.value)) {
        signViewModel.emailErrMsg.value = ValidationConstants.EmailValid
    } else if (isEmpty(signViewModel.password.value)) {
        signViewModel.emailErrMsg.value = ""
        signViewModel.passwordErrMsg.value = ValidationConstants.PasswordBlank
    } else if (!isValidPassword(signViewModel.password.value)) {
        signViewModel.emailErrMsg.value = ""
        signViewModel.passwordErrMsg.value = ValidationConstants.PasswordValid
    } else {
        signViewModel.passwordErrMsg.value = ""
        onSuccess()
    }
}

fun navigateToHomePage(navController: NavController) {
    navController.navigate(RootGraph.DASHBOARD) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }
}