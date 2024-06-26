package com.example.re_watch.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.re_watch.R
import com.example.re_watch.SignUpViewModel
import com.example.re_watch.components.CButton
import com.example.re_watch.components.CTextField
import com.example.re_watch.data.SignUpUIEvent
import com.example.re_watch.navigation.AppScreens


@Composable
fun SignUpScreen(navController: NavHostController) {

    val signUpViewModel: SignUpViewModel = viewModel()
    signUpViewModel.setNavController(navController)

    var isError = remember { mutableStateOf(false) }
    var usernameShort = remember { mutableStateOf(false) }
    var isEmailCheck = remember { mutableStateOf(false) }
    var passwordLength = remember { mutableStateOf(false) }
    var isprofileusernameCheck = remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF253334),
        modifier = Modifier.fillMaxSize()
    ) {

        Box(modifier = Modifier.fillMaxSize()){
            // Background Image
            Image(painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .align(alignment = Alignment.BottomCenter)
            )

            // Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                // Logo
                Image(painter = painterResource(id = R.drawable.pheonix),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 54.dp)
                        .height(100.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                )

                Text(text = "Sign Up",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight(500),
                        color = Color.White
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )

                Text("Sign up now and get access to all the Features of Re-watch.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xB2FFFFFF)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )


                CTextField(
                    hint = "Profile User Name ",
                    onValueChange = {
                        val filteredText = it.replace(" ", "")
                        signUpViewModel.onEvent(SignUpUIEvent.ProfileUsernameChanged(filteredText))},
                    value = signUpViewModel.signUpUIState.value.profileUsername,
                    keyboardtype = KeyboardType.Text
                )
                if(!signUpViewModel.signUpUIState.value.profileUsernameError || ( isprofileusernameCheck.value && signUpViewModel.signUpUIState.value.profileUsername.isEmpty() )){
                    if(!signUpViewModel.signUpUIState.value.profileUsernameError){
                        Text(
                            text = "Profile username already exit",
                            color = Color.Red
                        )
                    }
                    else{
                        Text(
                            text = "Profile user name can't be null",
                            color = Color.Red
                        )
                    }
                }
                CTextField(
                    hint = "User Name",
                    onValueChange = {signUpViewModel.onEvent(SignUpUIEvent.UsernameChanged(it))},
                    value = signUpViewModel.signUpUIState.value.username,
                    keyboardtype = KeyboardType.Text
                )
                if(usernameShort.value && !signUpViewModel.signUpUIState.value.usernameError){
                    Text(
                        text = "User name is too short",
                        color = Color.Red
                    )

                }

                CTextField(
                    hint = "Email Address",
                    onValueChange = {signUpViewModel.onEvent(SignUpUIEvent.EmailChanged(it))},
                    value = signUpViewModel.signUpUIState.value.email,
                    keyboardtype = KeyboardType.Email
                )
                if(isEmailCheck.value && !signUpViewModel.signUpUIState.value.emailError){
                    Text(
                        text = "recheck email",
                        color = Color.Red
                    )
                }

                CTextField(
                    hint = "Password",
                    onValueChange = {
                        isError.value = it.contains(" ")
                        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))},
                    value = signUpViewModel.signUpUIState.value.password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardtype = KeyboardType.Password
                )
                if((passwordLength.value && !signUpViewModel.signUpUIState.value.passwordError) || isError.value){

                    if(isError.value){
                        Text(
                            text = "you can't use spaces in password",
                            color = Color.Red
                        )
                    }
                    else{
                        Text(
                            text = "password must be of 8 char",
                            color = Color.Red
                        )
                    }
                }



                Spacer(modifier = Modifier.height(24.dp))

                CButton(text = "Sign Up", onClick = {
                    if(signUpViewModel.allValidationPassed.value && !isError.value && !signUpViewModel.signUpUIState.value.profileUsername.isEmpty()){
                        signUpViewModel.onEvent(SignUpUIEvent.SignUpButtonClicked)
                        Log.d("error","button clicked")
                    }
                    else{
                        if(!signUpViewModel.signUpUIState.value.usernameError){
                            Log.d("error","check username")
                            usernameShort.value = true
                        }
                        if(!signUpViewModel.signUpUIState.value.emailError){
                            Log.d("error","check email")
                            isEmailCheck.value = true
                        }
                        if(!signUpViewModel.signUpUIState.value.passwordError){
                            Log.d("error","check password")
                            passwordLength.value = true
                        }
                        if(signUpViewModel.signUpUIState.value.profileUsername.isEmpty()){
                            isprofileusernameCheck.value = true
                        }
                    }

                })

                Row(
                    modifier = Modifier.padding(top=12.dp, bottom = 52.dp)
                ){
                    Text("Already have an account? ",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    )

                    Text("Sign In",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(800),
                            color = Color.White
                        ),
                        modifier = Modifier.clickable {
                            navController.navigate(route = AppScreens.LoginScreen.route)
                        }
                    )
                }
            }
            if(signUpViewModel.signUpInProgress.value){
                CircularProgressIndicator()
            }
        }

    }

}

