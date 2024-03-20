package com.example.re_watch.components


import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.re_watch.R
import com.example.re_watch.VideoPickerViewModel
import com.example.re_watch.data.UploadUIEvent
import com.example.re_watch.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UploadVideoPopUp(onDismiss: () -> Unit) {

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
                    .height(433.dp)
                    .width(340.dp),
            ) {
                Row {
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
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "File Uploaded",
                        modifier = Modifier
                            .padding(top = 102.dp)
                    )
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
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
            ){
            Icon(
                painter = painterResource(id = R.drawable.profilepng),
                contentDescription = "null",
                modifier = Modifier
                    .padding(top = 120.dp)
                    .size(100.dp)
                    .fillMaxSize(),
                tint = Color.Unspecified
            )
        }
        Button(
            onClick = {
                viewModelvideo.onEvent(UploadUIEvent.UploadButtonClicked)

            },
            modifier = Modifier
                .padding(start = 120.dp, top = 575.dp)
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


@Preview(showSystemUi = true)
@Composable
fun UploadVideoPopUpPreview() {
//    UploadVideoPopUp()
}