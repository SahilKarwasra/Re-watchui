package com.example.re_watch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.data.RememberWindowInfo

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoCard(username: String, extraText: String, videoUrl: String, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(12.dp)
            .height(250.dp)
            .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                 defaultElevation = 6.dp
            ),
        onClick = onClick
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                GlideImage(
                    model = videoUrl,
                    contentDescription = "video",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 7.dp),
                    text = username,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp
                )
                Text(
                    text = extraText,
                   Modifier.padding(end = 7.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


//@Preview(showSystemUi = true)
//@Composable
//fun VideoCardPreview() {
//    AdvancedVideoCard("Apex Legend","Shroud", videoUrl = "https://firebasestorage.googleapis.com/v0/b/re-watch.appspot.com/o/videos%2F144edeec-0be4-464e-9133-212d481723ea?alt=media&token=633ad954-56e4-4a8d-baaf-c9652151d81b",{})
//}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AdvancedVideoCard(
    username: String,
    title: String,
    videoUrl: String,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit,
    remove: Boolean,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val windowInfo = RememberWindowInfo()
    val position = windowInfo.screenWidth.value.dp - 160.dp
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onClick },
        verticalAlignment = Alignment.CenterVertically
    ) {

        GlideImage(
            model = videoUrl,
            contentDescription = "video",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(9.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 15.dp)
            )
            Text(
                text = username,
                fontSize = 10.sp
            )
        }

        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options"
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Column {
                    if(remove){
//                    DropdownMenuItem(
//                        text = {
//                            Row {
//                                Icon(Icons.Default.Edit, contentDescription = "Delete")
//                                Text("Edit",Modifier.padding(start = 3.dp))
//                            }
//                        },
//                        onClick = {
//                            onEditClicked()
//                            expanded = false
//                        }
//                    )
                        DropdownMenuItem(
                            text = {
                                Row {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    Text("Delete",Modifier.padding(start = 3.dp))
                                }
                            },
                            onClick = {
                                onDeleteClicked()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

    }


}

