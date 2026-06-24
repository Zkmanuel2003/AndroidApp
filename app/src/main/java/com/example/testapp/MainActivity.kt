package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.example.testapp.data.AppDatabase
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.ui1.HomeScreen
import com.example.testapp.viewmodel.LoginViewModel
import com.example.testapp.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "testapp_database"
        ).build()

        val userDao = database.userDao()

        val loginViewModel = LoginViewModel(userDao)
        val registerViewModel = RegisterViewModel(userDao)

        enableEdgeToEdge()

        setContent {
            TestAppTheme {
                HomeScreen(
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel
                )
            }
        }
    }
}