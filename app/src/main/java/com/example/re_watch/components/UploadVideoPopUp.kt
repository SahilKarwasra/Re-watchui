package com.example.re_watch.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.re_watch.R

@Composable
fun UploadVideoPopUp() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .height(433.dp)
                .width(340.dp),
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .padding(start = 120.dp, top = 140.dp)
                    .width(100.dp),
                colors = ButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(text = "Upload")
            }
            OutlinedTextField(
                value = "Title (required)",
                onValueChange = {

                },
                modifier = Modifier.padding(top = 25.dp, start = 30.dp)
            )
            OutlinedTextField(
                value = "Description",
                onValueChange = {

                },
                modifier = Modifier
                    .padding(top = 30.dp, start = 30.dp)
                    .height(120.dp)
            )
        }
    }
    Icon(
        painter = painterResource(id = R.drawable.profilepng),
        contentDescription = "null",
        modifier = Modifier
            .padding(start = 140.dp, top = 120.dp)
            .size(100.dp),
        tint = Color.Unspecified
    )
}


@Preview(showSystemUi = true)
@Composable
fun UploadVideoPopUpPreview() {
    UploadVideoPopUp()
}