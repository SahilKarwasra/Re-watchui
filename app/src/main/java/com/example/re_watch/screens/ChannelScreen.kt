package com.example.re_watch.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.R
import com.example.re_watch.UserDetailViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ChannelScreen(navController: NavHostController, userId: String, userSlug: String) {
    val viewModel: UserDetailViewModel = viewModel()
    val userDetails by viewModel.userDetails.observeAsState(initial = null)
    val userName = userDetails?.userDisplayName
    val userProfileImage = userDetails?.userProfileImage
    val userProfileUrl = userDetails?.userProfileUrl
    Log.d("user",userSlug)

    LaunchedEffect(Unit) {
        viewModel.fetchUserDetails(userId,userSlug)
    }

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
                            navController.popBackStack()
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

            GlideImage(
                model = userProfileImage,
                contentDescription = "Profile Pic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 20.dp, top = 30.dp)
                    .size(110.dp).clip(CircleShape)
            )
            Column {
                userName?.let {
                    Text(
                        text = it,
                        fontSize = 28.sp,
                        color = Color(0xFF999BAD),
                        modifier = Modifier.padding(start = 20.dp, top = 40.dp)
                    )
                }
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

}