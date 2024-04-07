package com.example.re_watch.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.re_watch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelScreen() {
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
        Row {
            Icon(
                painter = painterResource(id = R.drawable.defaultprofilepic),
                contentDescription = "Profile Pic",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 20.dp, top = 30.dp)
                    .size(110.dp)
            )
            Column {
                Text(
                    text = "Username",
                    fontSize = 28.sp,
                    color = Color(0xFF999BAD),
                    modifier = Modifier.padding(start = 20.dp, top = 40.dp)
                )
                Text(
                    text = "12 Videos",
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                    fontSize = 16.sp,
                    color = Color(0xFF87979E),
                )
            }
        }
        Text(
            text = "Videos",
            fontSize = 28.sp,
            modifier = Modifier
                .padding(start = 20.dp, top = 40.dp),
            color = Color(0xFF6B7E8D),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dumythumb),
                contentDescription = "Thumbnail",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 9.dp, top = 30.dp)
                    .size(width = 200.dp, height = 100.dp)
                    .aspectRatio(2f)
            )
            Column {
                Text(
                    text = "Pubg Chicken Dinner",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 5.dp, top = 30.dp)
                        .fillMaxWidth(),
                    color = Color(0xFF6B7E8D),
                    maxLines = 4,
                )
                Text(
                    text = "Upload date",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .align(Alignment.End),
                    color = Color(0xFF6B7E8D)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    ChannelScreen()
}