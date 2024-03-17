package com.example.re_watch.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color(0xFFBEC2C2)
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedTextColor = Color(0xFFBEC2C2),
            unfocusedTextColor = Color(0xFFBEC2C2),
            focusedIndicatorColor = Color(0xFFBEC2C2),
            unfocusedIndicatorColor = Color(0xFFBEC2C2)
        )
    )
}