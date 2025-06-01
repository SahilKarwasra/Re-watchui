package com.example.re_watch.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.R
import com.example.re_watch.SignUpViewModel
import com.example.re_watch.components.AuthCard
import com.example.re_watch.components.CButton
import com.example.re_watch.components.CTextField
import com.example.re_watch.data.SignUpUIEvent
import com.example.re_watch.navigation.AppScreens
import com.example.re_watch.ui.theme.DarkGradientEnd
import com.example.re_watch.ui.theme.DarkGradientStart
import com.example.re_watch.ui.theme.GradientEnd
import com.example.re_watch.ui.theme.GradientStart
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(navController: NavHostController) {
    val signUpViewModel: SignUpViewModel = viewModel()
    signUpViewModel.setNavController(navController)

    var isUsernameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isProfileUsernameError by remember { mutableStateOf(false) }
    var isPasswordSpaceError by remember { mutableStateOf(false) }
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
                            .size(100.dp)
                            .scale(logoScale)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Join Re-watch",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Create your account to get started",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Sign Up Form Card
                AuthCard(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.alpha(contentAlpha)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        // Profile Username Field
                        CTextField(
                            hint = "Profile Username",
                            onValueChange = {
                                val filteredText = it.replace(" ", "")
                                signUpViewModel.onEvent(SignUpUIEvent.ProfileUsernameChanged(filteredText))
                                isProfileUsernameError = false
                            },
                            value = signUpViewModel.signUpUIState.value.profileUsername,
                            keyboardtype = KeyboardType.Text,
                            isError = isProfileUsernameError || !signUpViewModel.signUpUIState.value.profileUsernameError,
                            errorMessage = when {
                                isProfileUsernameError && signUpViewModel.signUpUIState.value.profileUsername.isEmpty() -> 
                                    "Profile username cannot be empty"
                                !signUpViewModel.signUpUIState.value.profileUsernameError -> 
                                    "Profile username already exists"
                                else -> null
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile Username",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        // Username Field
                        CTextField(
                            hint = "Full Name",
                            onValueChange = {
                                signUpViewModel.onEvent(SignUpUIEvent.UsernameChanged(it))
                                isUsernameError = false
                            },
                            value = signUpViewModel.signUpUIState.value.username,
                            keyboardtype = KeyboardType.Text,
                            isError = isUsernameError && !signUpViewModel.signUpUIState.value.usernameError,
                            errorMessage = if (isUsernameError && !signUpViewModel.signUpUIState.value.usernameError) 
                                "Username is too short" else null,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Username",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        // Email Field
                        CTextField(
                            hint = "Email Address",
                            onValueChange = {
                                signUpViewModel.onEvent(SignUpUIEvent.EmailChanged(it))
                                isEmailError = false
                            },
                            value = signUpViewModel.signUpUIState.value.email,
                            keyboardtype = KeyboardType.Email,
                            isError = isEmailError && !signUpViewModel.signUpUIState.value.emailError,
                            errorMessage = if (isEmailError && !signUpViewModel.signUpUIState.value.emailError) 
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
                                isPasswordSpaceError = it.contains(" ")
                                signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))
                                isPasswordError = false
                            },
                            value = signUpViewModel.signUpUIState.value.password,
                            keyboardtype = KeyboardType.Password,
                            isError = isPasswordError || isPasswordSpaceError,
                            errorMessage = when {
                                isPasswordSpaceError -> "Password cannot contain spaces"
                                isPasswordError && !signUpViewModel.signUpUIState.value.passwordError -> 
                                    "Password must be at least 8 characters"
                                else -> null
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Password",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Sign Up Button
                        CButton(
                            text = "Create Account",
                            onClick = {
                                val isFormValid = signUpViewModel.allValidationPassed.value && 
                                                !isPasswordSpaceError && 
                                                signUpViewModel.signUpUIState.value.profileUsername.isNotEmpty()
                                
                                if (isFormValid) {
                                    signUpViewModel.onEvent(SignUpUIEvent.SignUpButtonClicked)
                                    Log.d("signup", "button clicked")
                                } else {
                                    if (!signUpViewModel.signUpUIState.value.usernameError) {
                                        Log.d("error", "check username")
                                        isUsernameError = true
                                    }
                                    if (!signUpViewModel.signUpUIState.value.emailError) {
                                        Log.d("error", "check email")
                                        isEmailError = true
                                    }
                                    if (!signUpViewModel.signUpUIState.value.passwordError) {
                                        Log.d("error", "check password")
                                        isPasswordError = true
                                    }
                                    if (signUpViewModel.signUpUIState.value.profileUsername.isEmpty()) {
                                        isProfileUsernameError = true
                                    }
                                }
                            },
                            enabled = !signUpViewModel.signUpInProgress.value
                        )

                        // Loading indicator
                        if (signUpViewModel.signUpInProgress.value) {
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
                                    text = "Creating account...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Already have account row
                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Already have an account? ",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            )

                            Text(
                                text = "Sign In",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.clickable {
                                    navController.navigate(route = AppScreens.LoginScreen.route)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

