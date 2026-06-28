package com.example.testapp.ui1

import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.data.AuftragDao
import com.example.testapp.viewmodel.AuftragViewModel
import com.example.testapp.viewmodel.LoginViewModel
import com.example.testapp.viewmodel.RegisterViewModel

// Je höher die Zahl, desto "tiefer" im Stack — bestimmt die Swipe-Richtung
val screenDepth = mapOf(
    "home" to 0,
    "login" to 1,
    "register" to 1,
    "info" to 1,
    "main" to 2,
    "auftrag" to 3,
    "profil" to 3,
    "einstellungen" to 3,
)

// Wohin geht der Zurück-Button je nach Screen (null = App schließen)
val screenBack = mapOf(
    "login" to "home",
    "register" to "home",
    "info" to "home",
    "auftrag" to "main",
    "profil" to "main",
    "einstellungen" to "main",
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    startScreen: String,
    prefs: SharedPreferences,
    auftragDao: AuftragDao
) {
    val userEmail = prefs.getString("logged_in_email", "") ?: ""
    val auftragViewModel = remember { AuftragViewModel(auftragDao, userEmail) }
    var screen by remember { mutableStateOf(startScreen) }

    // Zurück-Button abfangen — nur aktiv wenn screenBack einen Eintrag hat
    // "home" und "main" sind nicht drin → dort schließt die App normal
    BackHandler(enabled = screenBack.containsKey(screen)) {
        screen = screenBack[screen] ?: "main"
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            val goingForward = (screenDepth[targetState] ?: 0) >= (screenDepth[initialState] ?: 0)
            if (goingForward) {
                fadeIn(tween(300)) + slideInHorizontally { it } togetherWith
                        fadeOut(tween(200)) + slideOutHorizontally { -it }
            } else {
                fadeIn(tween(300)) + slideInHorizontally { -it } togetherWith
                        fadeOut(tween(200)) + slideOutHorizontally { it }
            }
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

            "main" -> Dashboard(
                onAuftragClick = { screen = "auftrag" },
                onProfilClick = { screen = "profil" },
                onEinstellungenClick = { screen = "einstellungen" },
                onLogoutClick = {
                    prefs.edit().remove("logged_in_email").apply()
                    screen = "home"
                }
            )

            "auftrag" -> AuftragScreen(
                viewModel = auftragViewModel,
                onBackClick = { screen = "main" }
            )

            "profil" -> ProfilScreen(
                onBackClick = { screen = "main" },
                onLogout = {
                    prefs.edit().remove("logged_in_email").apply()
                    screen = "home"
                }
            )

            "einstellungen" -> Einstellung(
                onBackClick = { screen = "main" }
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
}

@Composable
fun HomeContent(
    onLoginClick: () -> Unit,
    onInfoClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
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