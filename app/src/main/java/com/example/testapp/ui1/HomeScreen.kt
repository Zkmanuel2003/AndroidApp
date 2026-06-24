package com.example.testapp.ui1

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.viewmodel.LoginViewModel
import com.example.testapp.viewmodel.RegisterViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    var screen by remember {
        mutableStateOf("home")
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(tween(300)) + slideInHorizontally { fullWidth -> fullWidth } togetherWith
                    fadeOut(tween(200)) + slideOutHorizontally { fullWidth -> -fullWidth }
        },
        label = "ScreenAnimation"
    ) { currentScreen ->

        when (currentScreen) {
            "info" -> InfoScreen(
                onBackClick = {
                    screen = "home"
                }
            )

            "login" -> LoginScreen(
                loginViewModel = loginViewModel,
                onBackClick = {
                    screen = "home"
                },
                onLoginSuccess = {
                    screen = "main"
                }
            )

            "register" -> RegisterScreen(
                viewModel = registerViewModel,
                onBackClick = {
                    screen = "home"
                },
                onRegisterSuccess = {
                    screen = "home"
                }
            )

            "main" -> MainScreen(
                onBackClick = {
                    screen = "home"
                }
            )

            else -> HomeContent(
                onLoginClick = {
                    screen = "login"
                },
                onRegisterClick = {
                    screen = "register"
                },
                onInfoClick = {
                    screen = "info"
                }
            )
        }
    }
}@Composable
fun HomeContent(
    onLoginClick: () -> Unit,
    onInfoClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Willkommen bei")
        Text("FirstAPP", fontSize = 46.sp, fontWeight = FontWeight.Bold)
        Text("Die erste moderne Android App")

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Konto anlegen")
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Anmelden")
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onInfoClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Info")
        }
    }
}