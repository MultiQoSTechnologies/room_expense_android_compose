package com.example.expensemanagementsystem.screens.authentication.signup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.expensemanagementsystem.utils.SessionManagerClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val sessionManagerClass: SessionManagerClass,
) : ViewModel() {
    val nameMsg: MutableState<String> = mutableStateOf("")
    val nameErrMsg: MutableState<String> = mutableStateOf("")
    val emailAddress: MutableState<String> = mutableStateOf("")
    val emailErrMsg: MutableState<String> = mutableStateOf("")
    val password: MutableState<String> = mutableStateOf("")
    val passwordErrMsg: MutableState<String> = mutableStateOf("")
}