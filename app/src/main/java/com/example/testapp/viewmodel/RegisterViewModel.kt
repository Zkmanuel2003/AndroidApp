package com.example.testapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.User
import com.example.testapp.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun register(
        email: String,
        password: String,
        repeatPassword: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
            _message.value = "Bitte alle Felder ausfüllen"
            return
        }

        if (password != repeatPassword) {
            _message.value = "Passwörter stimmen nicht überein"
            return
        }

        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(email)

            if (existingUser != null) {
                _message.value = "Diese E-Mail existiert bereits"
            } else {
                userDao.insertUser(
                    User(
                        email = email,
                        password = password
                    )
                )
                _message.value = "Registrierung erfolgreich"
                onSuccess()
            }
        }
    }
}