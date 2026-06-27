package com.example.testapp.ui1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.viewmodel.RegisterViewModel


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    // verschiedene FocusRequest-Objekte
    val passwordFocusRequester = remember { FocusRequester() } // konstruktor aufruf
    val repeatPasswordFocusRequester = remember { FocusRequester() }


    Column(
        modifier = Modifier
            .fillMaxSize()  // nimm den gesamten verfügbaren Platz
            .verticalScroll(rememberScrollState()) // mach es scrollbar (bei langer Liste / Tastatur)
            .imePadding() // wenn Tastatur aufgeht → schieb Inhalt nach oben
            .windowInsetsPadding(WindowInsets.navigationBars) // fügt den abstand so hin wie groß die navihationsleiste ist
            .padding(24.dp), // 24dp Innenabstand auf allen Seiten
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text =  "Bitte Registrieren Sie sich",
            fontSize =  20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = email,
            onValueChange = { newtext -> email = newtext },
            label = { Text("E-Mail") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFocusRequester.requestFocus()
                }
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { newText -> password = newText },
            label = { Text("Passwort") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    repeatPasswordFocusRequester.requestFocus()
                }
            )
        )

        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Passwort wiederholen") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(repeatPasswordFocusRequester),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )

        if (password.isNotEmpty() && repeatPassword.isNotEmpty() && password != repeatPassword) {
            Text(text = "Passwörter stimmen nicht überein", color = Color.Red)
        }


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register(
                    email = email,
                    password = password,
                    repeatPassword = repeatPassword,
                    onSuccess = onRegisterSuccess
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrieren")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zurück")
        }
    }
}