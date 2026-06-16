package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.ui1.HomeScreen

// einstiegspunkt des programms
class MainActivity : ComponentActivity() {
    // beim erstellen von MainActivity objekt und ruft dann OnCreate auf
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                HomeScreen()
                }
            }
        }
    }