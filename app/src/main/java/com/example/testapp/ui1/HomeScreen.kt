package com.example.testapp.ui1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*

// Homescreen
@Composable
fun HomeScreen() {
    var showInfo by remember {
        mutableStateOf(false)
    }
    if (showInfo) {
        InfoScreen(
            onBackClick = {
                showInfo = false
            }
        )
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Willkommen!", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Verbinden")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Sprache")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("Einstellungen")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                showInfo = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Info")
        }
    }
}