package com.example.re_watch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RemoveCommentDialogBox() {
    Surface(
        modifier = Modifier
            .size(height = 100.dp , width = 270.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {

                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Remove Comment",
                    modifier = Modifier
                        .size(40.dp),
                )
                Text(
                    text = "Delete Comment",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {

                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Report Comment",
                    modifier = Modifier
                        .size(40.dp),
                )
                Text(
                    text = "Report Comment",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    color = Color.DarkGray
                )
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun removecommentprev() {
    RemoveCommentDialogBox()
}