package com.example.re_watch.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.re_watch.R
import com.example.re_watch.Validator
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.database


@Composable
fun SettingsScreen(navController: NavHostController) {

    Scaffold(
        topBar = { ProfileSettingsTopBar() }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            ProfileSettingsContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsTopBar() {
    TopAppBar(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(CornerSize(20.dp))),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                Alignment.Center
            ) {
                Text(
                    text = "Settings",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier
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
}
@Composable
fun ProfileSettingsContent() {
    val firebaseAuth = FirebaseAuth.getInstance().currentUser

    var username by remember { mutableStateOf(firebaseAuth?.displayName) }
    var fetchedDisplayName by remember {
        mutableStateOf(firebaseAuth?.displayName)
    }
    var email by remember { mutableStateOf(firebaseAuth?.email) }
    var showDialog by remember { mutableStateOf(false) }
    var isEditingUsername by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfilePhotoSection(onClick = { showDialog = true })
        if (isEditingUsername) {
            fetchedDisplayName =
                EditableProfileInfoItem(
                    label = "Username:",
                    value = username.toString(),
                    onValueChange = { username = it },
                    onSave = {
                        isEditingUsername = false
                    },
                    onCancel = { isEditingUsername = false }
                )
        } else {
            fetchedDisplayName?.let {
                ProfileInfoItem(
                    label = "Username:",
                    value = it,
                    isEditable = true,
                    onClick = {
                        isEditingUsername = true
                    }
                )
            }
        }
        ProfileInfoItem(label = "Email:", value = email.toString(), isEditable = false) {
            // Handle email update
        }
        ChangePasswordDialog()
        LogoutSection()
    }

    if (showDialog) {
        ProfileImagePickerDialog(onDismiss = { showDialog = false }) {
            // Handle profile image update
        }
    }
}


@Composable
fun ProfilePhotoSection(onClick: () -> Unit) {
    // Placeholder for profile photo
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.profilepng),
            contentDescription = "Profile Photo",
            modifier = Modifier.size(100.dp),
        )
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit Profile Photo",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String,isEditable: Boolean,onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 18.sp, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
            if(isEditable){
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable(onClick = onClick),
                    tint = Color.Gray
                )
            }
        }
    }
}
@Composable
fun EditableProfileInfoItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
): String {
    val context = LocalContext.current
    var newValue by remember { mutableStateOf(value) }
    var valueUpdated by remember { mutableStateOf(false) }
    Column {
        Text(text = label, fontSize = 18.sp, color = Color.Gray)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            TextField(
                value = newValue,
                onValueChange = {
                    newValue = it
                    onValueChange(it)
                },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            IconButton(onClick = {
                if(newValue.isNotEmpty()){
                    updateDisplayName(newValue)
                    valueUpdated = true
                }
                else{
                    Toast.makeText(context,"Username can't be empty",Toast.LENGTH_LONG).show()
                }
                onSave()
            }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Clear, contentDescription = "Cancel")
            }
        }
    }
    if(!valueUpdated){
        newValue = value
    }

    return newValue
}

@Composable
fun ChangePasswordDialog() {
    val context = LocalContext.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Change Password") },
            text = {
                Column {
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        label = { Text("Confirm New Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {

                        if(Validator.validatePassword(newPassword) && newPassword == confirmNewPassword){
                            changePassword(currentPassword = currentPassword,newPassword = newPassword, context = context)
                            showDialog = false
                        }
                        if(newPassword != confirmNewPassword){
                            Toast.makeText(context,"Confirm pass didn't match",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(context,"Please enter a valid new Password",Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Password")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        )
    } else {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }
    }
}

@Composable
fun ProfileImagePickerDialog(onDismiss: () -> Unit, onImageSelected: () -> Unit) {
    // Placeholder for profile image picker dialog
}

@Composable
fun LogoutSection() {
    val context = LocalContext.current

    Button(
        onClick = { /* Handle logout */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Logout")
    }
}

fun updateDisplayName(displayName: String) {
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid
    val database = Firebase.database
    val usersRef = database.getReference("users")

    val profileUpdates = UserProfileChangeRequest.Builder()
        .setDisplayName(displayName)
        .build()

    user?.updateProfile(profileUpdates)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update successful.
            } else {
                // Handle error
            }
        }

    // Create a map to hold the updated displayName
    val updatedUserData = hashMapOf<String, Any>(
        "displayName" to displayName
    )

    // Update the child node with the new displayName
    usersRef.child(userId!!).updateChildren(updatedUserData)
        .addOnSuccessListener {
            // Data successfully updated
            println("User displayName updated successfully")
            Log.d("Update", "User displayName updated successfully")
        }
        .addOnFailureListener { exception ->
            // Handle error
            Log.d("Update", "Error updating user displayName: $exception")
        }
}

private fun changePassword(currentPassword: String, newPassword: String,context:Context) {
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(context,"Password Changed Successfully",Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context,"Password update failed",Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(context,"Incorrect Password",Toast.LENGTH_LONG).show()
                }
            }
    }
}