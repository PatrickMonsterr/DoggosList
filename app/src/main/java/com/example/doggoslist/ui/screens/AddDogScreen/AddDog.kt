package com.example.doggoslist.ui.screens.AddDogScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.doggoslist.R
import com.example.doggoslist.ui.screens.DogDetailsScreen.DogImageScreen
import com.example.doggoslist.DogImageUiState
import com.example.doggoslist.model.AddDogViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogScreen(
    onBackClick: () -> Unit,
    addDogViewModel: AddDogViewModel,
    onAddDog: (String, String, String) -> Unit
) {

    val imageState = addDogViewModel.imageState.value

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var breed by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Dodaj Psa") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.light_pink),
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (imageState) {
                is DogImageUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is DogImageUiState.Error -> {
                    Text("Błąd ładowania obrazka 🐶", color = Color.Red)
                }

                is DogImageUiState.Success -> {
                    DogImageScreen(imageUrl = imageState.url)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                singleLine = true,
                value = name,
                onValueChange = { name = it },
                label = { Text("Imię") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                singleLine = true,
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Rasa") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val imageUrl = (imageState as? DogImageUiState.Success)?.url

            Button(
                onClick = {
                    if (name.text.isNotBlank() && breed.text.isNotBlank() && imageUrl != null) {
                        onAddDog(name.text.trim(), breed.text.trim(), imageUrl)
                    }
                },
                enabled = imageUrl != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Dodaj")
            }
        }
    }
}
