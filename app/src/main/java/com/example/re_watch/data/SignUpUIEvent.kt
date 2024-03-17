package com.example.re_watch.data

sealed class SignUpUIEvent {

    data class UsernameChanged(val username: String ="user"): SignUpUIEvent()
    data class EmailChanged(val email: String): SignUpUIEvent()
    data class PasswordChanged(val password: String): SignUpUIEvent()

    data object SignUpButtonClicked : SignUpUIEvent()

}