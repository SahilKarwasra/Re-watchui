package com.example.re_watch.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.ThumbUp
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

data class CommentItem(
    val username: String,
    val comment: String,
    val likesCount: Int,
    val dislikesCount: Int
)

@Composable
fun CommentSectionPopUp(comments: List<CommentItem>) {
    Column {
        InputCommentTextField()
        LazyColumn {
            items(comments) { commentItem ->
                CommentItemRow(commentItem)
            }
        }
    }
}

@Composable
fun InputCommentTextField() {
    Row {
        Icon(
            painter = painterResource(id = R.drawable.profilepng),
            contentDescription = "ProfilePic",
            Modifier
                .padding(10.dp)
                .size(50.dp),
            tint = Color.Unspecified,
        )
        OutlinedTextField(
            value = "Comment",
            onValueChange = {},
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

@Composable
fun CommentItemRow(commentItem: CommentItem) {

    Row {
        Icon(
            painter = painterResource(id = R.drawable.profilepng),
            contentDescription = "ProfilePic",
            Modifier
                .padding(10.dp)
                .size(50.dp),
            tint = Color.Unspecified,
        )
        Column{
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "@${commentItem.username}",
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                    color = Color.Gray
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Edit Comment",
                        modifier = Modifier
                            .padding(top = 7.dp, end = 20.dp)
                            .size(25.dp)
                    )
                }
            }
            Text(
                text = commentItem.comment,
                modifier = Modifier.padding( start = 10.dp, end = 50.dp),
                color = Color.Black
            )
            Row(
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = "Like Comment",
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp)
                )
                Text(
                    text = commentItem.likesCount.toString(),
                    modifier = Modifier.padding(start = 6.dp, top = 12.dp, end = 6.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Dislike Comment",
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Text(
                    text = commentItem.dislikesCount.toString(),
                    modifier = Modifier.padding(start = 6.dp, top = 12.dp)
                )
            }
        }
    }
}

val dummyComments = listOf(
    CommentItem(
        username = "user1",
        comment = "This is the first comment. Lorem ipsum dolor sit amet.",
        likesCount = 10,
        dislikesCount = 5
    ),
    CommentItem(
        username = "user2",
        comment = "Second comment here. consectetur adipiscing elit.",
        likesCount = 8,
        dislikesCount = 3
    ),
    CommentItem(
        username = "user3",
        comment = "Third comment. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        likesCount = 15,
        dislikesCount = 2
    ),
    CommentItem(
        username = "user4",
        comment = "This is the first comment. Lorem ipsum dolor sit amet.",
        likesCount = 10,
        dislikesCount = 5
    ),
    CommentItem(
        username = "user5",
        comment = "Second comment here. consectetur adipiscing elit.",
        likesCount = 8,
        dislikesCount = 3
    ),
    CommentItem(
        username = "user6",
        comment = "Third comment. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        likesCount = 15,
        dislikesCount = 2
    ),CommentItem(
        username = "user7",
        comment = "This is the first comment. Lorem ipsum dolor sit amet.",
        likesCount = 10,
        dislikesCount = 5
    ),
    CommentItem(
        username = "user8",
        comment = "Second comment here. consectetur adipiscing elit.",
        likesCount = 8,
        dislikesCount = 3
    ),
    CommentItem(
        username = "user9",
        comment = "Third comment. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        likesCount = 15,
        dislikesCount = 2
    ),
)

@Preview(showSystemUi = true)
@Composable
fun CommentSectionPreview() {
    CommentSectionPopUp(dummyComments)
}