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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen() {
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
                onBackClick = {
                    screen = "home"
                },
                onLoginSuccess = {
                    screen = "home"
                }
            )

            else -> HomeContent(
                onLoginClick = {
                    screen = "login"
                },
                onInfoClick = {
                    screen = "info"
                }
            )
        }
    }
}

@Composable
fun HomeContent(
    onLoginClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    var loaded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        loaded = true
    }

    val titleScale by animateFloatAsState(
        targetValue = if (loaded) 1f else 0.85f,
        animationSpec = tween(700),
        label = "TitleScale"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Willkommen bei",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "FirstAPP",
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(titleScale)
            )

            Text(
                text = "Deine erste moderne Android App",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Konto anlegen")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Anmelden")
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Info")
            }
        }
    }
}