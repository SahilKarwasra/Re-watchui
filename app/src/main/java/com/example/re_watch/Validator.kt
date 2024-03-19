package com.example.re_watch

class Validator {
    companion object {

        fun validateUsername(username: String): Boolean {

            return (!username.isNullOrEmpty()&& username.length >=2)
        }
        fun validateEmail(email: String): Boolean {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun validatePassword(password: String): Boolean {

            return (!password.isNullOrEmpty() && password.length >= 8)
        }
    }
}

