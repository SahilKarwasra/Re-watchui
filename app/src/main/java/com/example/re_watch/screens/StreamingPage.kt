package com.example.re_watch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.re_watch.R

@Composable
fun StreamingPage() {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ) {

        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profilepng),
                contentDescription = "Profile Pic",
                modifier = Modifier
                    .padding(5.dp)
                    .size(70.dp),
                tint = Color.Unspecified
            )
            Column(
                modifier = Modifier.padding(start = 7.dp, top = 10.dp),
            ) {
                Text(
                    text = "Valorant Best Kills, highlights from my Stream",
                    modifier = Modifier.padding(bottom = 5.dp),
                    maxLines = 2,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp
                )
                Text(
                    text = "Shreya", maxLines = 1, fontWeight = FontWeight(300), fontSize = 16.sp
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .width(100.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Like icon",
                        modifier = Modifier.size(30.dp)
                            .clickable {

                            },
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "50",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .width(100.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Dislike Icon",
                        modifier = Modifier.size(30.dp)
                            .clickable {

                            },
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "50",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }


}

@Preview(showSystemUi = true)
@Composable
fun StreamingPagePreview() {
    StreamingPage()
}