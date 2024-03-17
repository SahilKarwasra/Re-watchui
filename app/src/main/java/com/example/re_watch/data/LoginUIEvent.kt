package com.example.re_watch.data

import androidx.compose.foundation.layout.ColumnScope

sealed class LoginUIEvent {
    data class EmailChanged(val email: String): LoginUIEvent()
    data class PasswordChanged(val password: String): LoginUIEvent()

    data object LoginButtonClicked : LoginUIEvent()

}