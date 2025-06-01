package com.example.re_watch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.re_watch.navigation.AppNavigation
import com.example.re_watch.ui.theme.RewatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RewatchTheme(
                darkTheme = isSystemInDarkTheme()
            ) {
                AppNavigation()
            }
        }
    }
}

