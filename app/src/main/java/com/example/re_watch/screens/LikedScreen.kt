package com.example.re_watch.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.re_watch.R
import com.example.re_watch.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(CornerSize(20.dp))),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                actions = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Liked Button.",
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {

                            }
                    )
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        Modifier
                            .padding(end = 20.dp , start = 20.dp)
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
        },
        floatingActionButton = {
            FloatingProfileButton {
                navController.navigate(route = AppScreens.ProfileScreen.route)
            }
        }



    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxHeight()
            .fillMaxWidth()) {

        }
    }
}

@Preview
@Composable
fun LikedScreenPreview() {
    LikedScreen(navController = rememberNavController())
}