package com.example.testapp.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userDao: UserDao,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            _message.value = "Bitte alle Felder ausfüllen"
            return
        }

        viewModelScope.launch {
            val user = userDao.login(email, password)

            if (user != null) {
                prefs.edit().putString("logged_in_email", email).apply()
                _message.value = "Login erfolgreich"
                onSuccess()
            } else {
                _message.value = "E-Mail oder Passwort falsch"
            }
        }
    }
}