package com.example.re_watch.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.re_watch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        Alignment.Center
                    ) {
                        Text(
                            text = "Settings",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {

                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF20A6E2),
                    titleContentColor = Color(0xFFFFFFFF),
                    actionIconContentColor = Color(0xFFFCFCFC)
                ),
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column {
                Icon(
                    painter = painterResource(id = R.drawable.profilepng),
                    contentDescription = "ProfilePic",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(start = 130.dp, top = 30.dp)
                        .size(120.dp)
                )
                Text(
                    text = "User Info",
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 10.dp, start = 50.dp),
                    fontSize = 26.sp,
                    color = Color.Gray
                )
                OutlinedTextField(
                    value = "Sahil",
                    onValueChange = {},
                    modifier = Modifier.padding(bottom = 30.dp, top = 20.dp, start = 50.dp),
                    label = {
                        Text(text = "User Name")
                    }
                )
                OutlinedTextField(
                    value = "sahilkarwasra@gmail.com",
                    onValueChange = {},
                    modifier = Modifier.padding(bottom = 30.dp, top = 20.dp, start = 50.dp),
                    label = {
                        Text(text = "User Email")
                    }
                )
                Button(
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4372F1)
                    ),
                    modifier = Modifier
                        .padding(bottom = 30.dp, top = 20.dp, start = 130.dp)
                        .width(120.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = "Change Password"
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingScreenPreview() {
    SettingsScreen(navController = NavHostController(LocalContext.current))
}