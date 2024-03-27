package com.example.re_watch.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChangePasswordPopUp() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            OutlinedTextField(
                value = "",
                onValueChange = {

                },
                label = {
                    Text(text = "Old Password")
                },
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = "",
                onValueChange = {

                },
                label = {
                    Text(text = "New Password")
                },
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )
            OutlinedTextField(
                value = "",
                onValueChange = {

                },
                label = {
                    Text(text = "New Password")
                },
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

            Button(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4372F1)
                ),
                modifier = Modifier
                    .padding(bottom = 30.dp, top = 20.dp, start = 85.dp)
                    .width(100.dp)
                    .height(52.dp)
            ) {
                Text(text = "Change")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChangePasswordPreview() {
    ChangePasswordPopUp()
}