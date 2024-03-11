package com.example.expensemanagementsystem.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.expensemanagementsystem.navigations.RootNavGraph
import com.example.expensemanagementsystem.ui.theme.ExpenseManagementSystemTheme
import com.example.expensemanagementsystem.utils.SessionManagerClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var sessionManagerClass: SessionManagerClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManagerClass = SessionManagerClass(applicationContext)

        setContent {
            ExpenseManagementSystemTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavGraph(sessionManagerClass!!)
                }
            }
        }
    }
}