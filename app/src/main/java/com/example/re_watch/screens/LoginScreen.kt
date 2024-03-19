package com.example.re_watch.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.LoginViewModel
import com.example.re_watch.R
import com.example.re_watch.components.CButton
import com.example.re_watch.components.CTextField
import com.example.re_watch.components.DontHaveAccountRow
import com.example.re_watch.data.LoginUIEvent
import com.example.re_watch.navigation.AppScreens

@Composable
fun LoginScreen(navController: NavHostController,) {
    val loginViewModel: LoginViewModel = viewModel()
    loginViewModel.setNavController(navController, LocalContext.current)
    Surface(
        color = Color(0xFF253334),
        modifier = Modifier.fillMaxSize()
    ) {


        Box(modifier = Modifier.fillMaxSize()) {
            /// Background Image
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .align(alignment = Alignment.BottomCenter)
            )

            /// Content

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.pheonix),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 54.dp)
                        .height(100.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                )

                Text(
                    text = "Sign In",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )

                Text(
                    "Sign In now to access all the features of Re-watch.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xB2FFFFFF)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )


                // Text Field
                CTextField(
                    hint = "Email Address",
                    onValueChange = {loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))},
                    value = loginViewModel.loginUIState.value.email,
                    keyboardtype = KeyboardType.Email
                )


                CTextField(
                    hint = "Password",
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))},
                    value = loginViewModel.loginUIState.value.password,
                    keyboardtype = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(24.dp))

                CButton(text = "Sign In", onClick = {
                    if(loginViewModel.allValidationPassed.value){
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    }
                    else{
                        if(!loginViewModel.loginUIState.value.emailError){
                            Log.d("error","check email")
                        }
                        if(!loginViewModel.loginUIState.value.passwordError){
                            Log.d("error","check password")
                        }
                    }
                    })

                DontHaveAccountRow(
                    onSignupTap = {
                        navController.navigate(route = AppScreens.SignUpScreen.route)
                    }
                )
            }
        }
    }
}
