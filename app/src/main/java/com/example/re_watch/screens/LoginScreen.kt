package com.example.re_watch.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.LoginViewModel
import com.example.re_watch.R
import com.example.re_watch.components.AuthCard
import com.example.re_watch.components.CButton
import com.example.re_watch.components.CTextField
import com.example.re_watch.components.DontHaveAccountRow
import com.example.re_watch.data.LoginUIEvent
import com.example.re_watch.navigation.AppScreens
import com.example.re_watch.ui.theme.DarkGradientEnd
import com.example.re_watch.ui.theme.DarkGradientStart
import com.example.re_watch.ui.theme.GradientEnd
import com.example.re_watch.ui.theme.GradientStart
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }
    val isDarkTheme = isSystemInDarkTheme()

    val logoScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(800),
        label = "logo_scale"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(1000),
        label = "content_alpha"
    )

    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

    loginViewModel.setNavController(navController, LocalContext.current)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isDarkTheme) {
                            listOf(DarkGradientStart, DarkGradientEnd)
                        } else {
                            listOf(GradientStart, GradientEnd)
                        }
                    )
                )
        ) {
            // Background Image with overlay
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isDarkTheme) 0.15f else 0.2f)
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Logo Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.alpha(contentAlpha)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pheonix),
                        contentDescription = "Re-watch Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .scale(logoScale)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Welcome Back!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Sign in to continue your journey",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Login Form Card
                AuthCard(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.alpha(contentAlpha)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Sign In",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        // Email Field
                        CTextField(
                            hint = "Email Address",
                            onValueChange = { 
                                loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                                isEmailError = false
                            },
                            value = loginViewModel.loginUIState.value.email,
                            keyboardtype = KeyboardType.Email,
                            isError = isEmailError && !loginViewModel.loginUIState.value.emailError,
                            errorMessage = if (isEmailError && !loginViewModel.loginUIState.value.emailError) 
                                "Please enter a valid email address" else null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        // Password Field
                        CTextField(
                            hint = "Password",
                            onValueChange = { 
                                loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                                isPasswordError = false
                            },
                            value = loginViewModel.loginUIState.value.password,
                            keyboardtype = KeyboardType.Password,
                            isError = isPasswordError && !loginViewModel.loginUIState.value.passwordError,
                            errorMessage = if (isPasswordError && !loginViewModel.loginUIState.value.passwordError) 
                                "Password cannot be empty" else null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Password",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Sign In Button
                        CButton(
                            text = "Sign In",
                            onClick = {
                                if (loginViewModel.allValidationPassed.value) {
                                    loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                                } else {
                                    if (!loginViewModel.loginUIState.value.emailError) {
                                        Log.d("error", "check email")
                                        isEmailError = true
                                    }
                                    if (!loginViewModel.loginUIState.value.passwordError || 
                                        loginViewModel.loginUIState.value.password.isEmpty()) {
                                        Log.d("error", "check password")
                                        isPasswordError = true
                                    }
                                }
                            },
                            enabled = !loginViewModel.loginInProgress.value
                        )

                        // Loading indicator
                        if (loginViewModel.loginInProgress.value) {
                            Row(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Text(
                                    text = "Signing in...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        DontHaveAccountRow(
                            onSignupTap = {
                                navController.navigate(route = AppScreens.SignUpScreen.route)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

