package com.example.doggoslist.ui.screens.MainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    name: String,
    onNameChange: (String) -> Unit,
    onAddClick: () -> Unit,
    isDuplicate: Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text("Wyszukaj pieska 🐶") },
                modifier = Modifier.weight(1f),
                isError = isDuplicate,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (isDuplicate) Color(0xFFFFE6E6) else Color.White,
                    unfocusedContainerColor = if (isDuplicate) Color(0xFFFFE6E6) else Color.White,
                    focusedBorderColor = if (isDuplicate) Color.Red else Color.Gray,
                    unfocusedBorderColor = if (isDuplicate) Color.Red else Color.LightGray,
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    errorBorderColor = Color.Red,
                    errorCursorColor = Color.Red,
                    errorContainerColor = Color(0xFFFFE6E6)
                )

            )
            IconButton(onClick = onAddClick) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Magenta,
                    modifier = Modifier.size(32.dp)
                )


            }

        }
    }
    if (isDuplicate) {
        Text(
            text = "Piesek już istnieje!",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}