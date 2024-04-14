package com.example.re_watch.data

data class SignUpUIState(
    var profileUsername: String = "",
    var username : String = "",
    var email : String = "",
    var password: String = "",
    var usernameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var profileUsernameError: Boolean = true
)