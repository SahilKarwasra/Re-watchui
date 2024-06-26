package com.example.re_watch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.CommentViewModel
import com.example.re_watch.data.Comment
import com.example.re_watch.data.CommentUIEvent
import com.example.re_watch.data.RememberWindowInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CommentSection(commentViewModel: CommentViewModel) {
    val user = FirebaseAuth.getInstance().currentUser
    var expanded by remember { mutableStateOf(false) }
    val firstComment = fetchFirstComment(commentViewModel.commentUIState.value.comments)
    val scope = rememberCoroutineScope()
    val sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val windowInfo = RememberWindowInfo()
    val heightOfBottomSheet = windowInfo.screenHeight.value - 220
    val profileImage = firstComment?.userProfileImage

    val comments = commentViewModel.commentUIState.value.comments
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        onClick = {
            expanded = !expanded
        }
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
        )
        {
            Row(modifier = Modifier.padding(start = 8.dp, top = 5.dp)) {

                Text(
                    text = "Comments",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = comments.size.toString(),
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .align(Alignment.CenterVertically),
                    fontSize = 14.sp,
                    color = Color.Gray
                )


            }
            Row {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp, top = 10.dp)
                ) {

                    GlideImage(
                        model = profileImage,
                        contentDescription = "ProfilePic",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(30.dp)
                            .clip(CircleShape),

                        )
                    (if (firstComment?.comment.isNullOrEmpty()) "No Comments Yet" else firstComment?.comment)?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(start = 7.dp, end = 10.dp)
                                .align(Alignment.CenterVertically),
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                        )
                    }
                }

            }



            if (expanded) {
                ModalBottomSheet(
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.height(heightOfBottomSheet.dp),
                    sheetState = sheetState,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Text(
                            text = "Comments",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black
                        )
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Comments Window"
                                )
                            },
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(30.dp),
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        expanded = false
                                    }
                                }
                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                            .height(1.dp)
                    )

                    user?.let { CommentSectionPopUp(commentViewModel, comments, user = it) }
                }

            }
        }
    }
}


@Composable
fun CommentSectionPopUp(
    commentViewModel: CommentViewModel,
    comments: MutableList<Comment>,
    user: FirebaseUser
) {


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        LazyColumn(modifier = Modifier.padding(bottom = 90.dp, top = 10.dp)) {
            items(comments) { comment ->

                CommentItemRow(comment)
            }
        }

        InputCommentTextField(modifier = Modifier, commentViewModel, user = user)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun InputCommentTextField(
    modifier: Modifier,
    viewModel: CommentViewModel,
    user: FirebaseUser
) {
    val profileImage = user.photoUrl

    Row(modifier = modifier) {
        GlideImage(
            model = profileImage,
            contentDescription = "ProfilePic",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(10.dp)
                .size(55.dp)
                .clip(CircleShape),

            )
        Box(modifier = Modifier, contentAlignment = Alignment.TopEnd) {
            OutlinedTextField(
                value = viewModel.commentUIState.value.comment,
                onValueChange = { viewModel.onEvent(CommentUIEvent.CommentChanged(it)) },
                modifier = Modifier
                    .padding(top = 5.dp, end = 35.dp)
                    .width(270.dp),
                placeholder = {
                    Text(
                        text = "Comment",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color(0xFF262727)
                        )
                    )
                },
            )
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Post Comment",
                modifier = Modifier
                    .padding(start = 5.dp, top = 18.dp)
                    .size(30.dp)
                    .clickable {
                        viewModel.onEvent(CommentUIEvent.CommentButtonClicked)

                    }
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommentItemRow(commentItem: Comment) {
    val commentId = commentItem.userId
    val profileImage = commentItem.userProfileImage
    Row {
        GlideImage(
            model = profileImage,
            contentDescription = "ProfilePic",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 10.dp, top = 15.dp)
                .size(30.dp)
                .clip(CircleShape),

            )
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "@${commentItem.userName}",
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Edit Comment",
                        modifier = Modifier
                            .padding(top = 15.dp, end = 20.dp)
                            .size(25.dp)
                            .clickable {

                            }

                    )
                }
            }
            Text(
                text = commentItem.comment,
                modifier = Modifier.padding(start = 10.dp, end = 50.dp, bottom = 10.dp),
                color = Color.Black
            )
//            Row(
//                modifier = Modifier.padding(bottom = 20.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.ThumbUp,
//                    contentDescription = "Like Comment",
//                    modifier = Modifier
//                        .padding(top = 10.dp, start = 10.dp)
//                )
//                Text(
//                    text = "1",
//                    modifier = Modifier.padding(start = 6.dp, top = 12.dp, end = 6.dp)
//                )
//                Icon(
//                    imageVector = Icons.Outlined.Delete,
//                    contentDescription = "Dislike Comment",
//                    modifier = Modifier
//                        .padding(top = 10.dp)
//                )
//                Text(
//                    text = "2",
//                    modifier = Modifier.padding(start = 6.dp, top = 12.dp)
//                )
//            }
        }
    }
}


// return latest comment in user

fun fetchFirstComment(commentList: MutableList<Comment>): Comment? {

    return commentList.firstOrNull()
}

