package com.example.re_watch.data

import android.app.Application
import android.content.Context

data class SignUpUIState(
    var username : String = "",
    var email : String = "",
    var password: String = "",
    var usernameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
)