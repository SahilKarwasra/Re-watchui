package com.example.re_watch.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.re_watch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveVideoScreen() {
    Column {
        TopAppBar(
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(CornerSize(20.dp))),
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

        LazyColumn {
            items(5) { index ->
                RemoveVideoListItem(index + 1)
            }
        }
    }
}

@Composable
fun RemoveVideoListItem(index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        Column {
            Icon(
                painter = painterResource(id = R.drawable.dumythumb),
                contentDescription = "Null",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(width = 250.dp, height = 150.dp)
            )
            Text(
                text = "Winner Winner Chicken Dinner $index",
                modifier = Modifier.size(width = 250.dp, height = 20.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
        OutlinedButton(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier.padding(start = 8.dp) // Add some spacing between the Column and the button
        ) {
            Text(text = "Remove Video", textAlign = TextAlign.Center)
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun RemoveVideoScreenPreview() {
    RemoveVideoScreen()
}