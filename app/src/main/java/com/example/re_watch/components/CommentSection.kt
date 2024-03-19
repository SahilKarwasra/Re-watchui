package com.example.re_watch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.re_watch.R

@Composable
fun CommentSection() {
    Card(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxWidth()
            .height(120.dp)
            .clickable {

            }
    ) {
        Row {
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 5.dp),
                text = " Comments",
                fontSize = 26.sp,
                color = Color.DarkGray
            )
            Text(
                text = "2",
                modifier = Modifier.padding(start = 6.dp, top = 7.dp),
                fontSize = 22.sp,
                color = Color.Gray
            )
        }
        Row {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profilepng),
                    contentDescription = "ProfilePic",
                    modifier = Modifier
                        .padding(start = 10.dp, top = 20.dp)
                        .size(50.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = "Nice Video, Most Recommended and useful",
                    modifier = Modifier.padding(top = 25.dp, start = 7.dp),
                    fontSize = 16.sp,
                    maxLines = 2
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = "See Comments List",
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(50.dp)
                )
            }
        }

    }
}

@Composable
fun CommentSectionPopUp() {

}

@Preview(showSystemUi = true)
@Composable
fun CommentSectionPreview() {
    CommentSectionPopUp()
}