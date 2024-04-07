package com.example.re_watch.components


import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.re_watch.VideoPickerViewModel
import com.example.re_watch.data.UploadUIEvent
import com.example.re_watch.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun UploadVideoPopUp(onDismiss: () -> Unit) {

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )

    val userProfilePhoto = FirebaseAuth.getInstance().currentUser?.photoUrl

    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_VIDEO)
    } else {
        TODO("VERSION.SDK_INT < TIRAMISU")
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver{_, event ->
                if(event == Lifecycle.Event.ON_START){
                    permissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )


    val context = LocalContext.current
    val viewModelvideo: VideoPickerViewModel = viewModel()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModelvideo.onVideoSelect(context)
                viewModelvideo.onEvent(UploadUIEvent.VideoUriChanged(it.toString()))
            }
        }
    )

    Dialog(onDismissRequest = onDismiss) {


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier
                    .width(340.dp),
            ){
                if (viewModelvideo.uploadInProgress.value) {

                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth().height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Black,
                                        Color.Gray
                                    )
                                ),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Button(
                            contentPadding = PaddingValues(1.dp),
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth(viewModelvideo.uploadProgress.value.toFloat() / 100)
                                .background(brush = gradient),
                            enabled = false,
                            elevation = null,
                            colors = buttonColors(
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )) {

                            Text(text = "${viewModelvideo.uploadProgress.value.toString()}%",
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(23.dp))
                                    .fillMaxHeight(0.87f)
                                    .fillMaxWidth()
                                    .padding(7.dp),
                                color= Color.White,
                                textAlign = TextAlign.Center)
                        }
                    }
                } else {

                    Column {
                        Button(
                            onClick = {
                                if (permissionState.status.isGranted) {
                                    launcher.launch("video/*")
                                } else if (permissionState.isPermanentlyDenied()) {
                                    Toast.makeText(
                                        context,
                                        "Grant Permission in App Settings",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        " Permission Denied",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }


                            },
                            modifier = Modifier
                                .padding(start = 90.dp, top = 90.dp)
                                .width(140.dp),
                            colors = ButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            )
                        ) {
                            Text(text = "Select Video")
                        }


                        OutlinedTextField(
                            value = viewModelvideo.uploadUIState.value.title,
                            onValueChange = {
                                viewModelvideo.onEvent(UploadUIEvent.TitleChanged(it))
                            },
                            modifier = Modifier.padding(top = 25.dp, start = 30.dp),
                            placeholder = {
                                Text(
                                    text = "Title (required)",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color(0xFF262727)
                                    )
                                )
                            },

                            )
                        OutlinedTextField(
                            value = viewModelvideo.uploadUIState.value.description,
                            onValueChange = {
                                viewModelvideo.onEvent(UploadUIEvent.DescriptionChanged(it))
                            },
                            modifier = Modifier
                                .padding(top = 30.dp, start = 30.dp)
                                .height(120.dp),
                            placeholder = {
                                Text(
                                    text = "Description",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color(0xFF262727)
                                    )
                                )
                            },
                        )
                        Text(
                            text = "Must Every Tag Is Separated With Comma (,)",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(top = 25.dp, start = 30.dp),
                        )
                        OutlinedTextField(
                            value = viewModelvideo.uploadUIState.value.videoTags,
                            onValueChange = {
                                viewModelvideo.onEvent(UploadUIEvent.videoTagsChanged(it.replace(" ",",")))
                            },
                            modifier = Modifier.padding(top = 25.dp, start = 30.dp),
                            placeholder = {
                                Text(
                                    text = "Tags",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color(0xFF262727)
                                    )
                                )
                            },
                        )
                        Button(
                            onClick = {
                                viewModelvideo.onEvent(UploadUIEvent.UploadButtonClicked)
                            },
                            modifier = Modifier
                                .padding(start = 120.dp, top = 30.dp, bottom = 30.dp)
                                .width(100.dp),
                            colors = ButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.Black
                            )
                        ) {
                            Text(text = "Upload")
                        }

                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
            ){

            GlideImage(
                model = userProfilePhoto,
                contentDescription = "ProfilePic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(100.dp)
                    .clip(CircleShape),
            )
        }


    }
}


@Preview(showSystemUi = true)
@Composable
fun UploadVideoPopUpPreview() {
//    UploadVideoPopUp()
}