package com.example.doggoslist.ui.screens.DogDetailsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.doggoslist.Dog
import com.example.doggoslist.DogImageUiState
import com.example.doggoslist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogDetailScreen(dog: Dog, onBackClick: () -> Unit, onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Detale") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.light_pink),
                titleContentColor = Color.Black,
                navigationIconContentColor = Color.Black,
                actionIconContentColor = Color.Black
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (dog.imageUrl != null) {
                DogImageScreen(imageUrl = dog.imageUrl!!)
            } else {
                Text(text = "🐕", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = dog.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = dog.breed, fontSize = 16.sp, color = Color.Gray)
        }
    }
}


@Composable
fun DogImageScreen(imageUrl: String, modifier: Modifier = Modifier) {
    var uiState by remember { mutableStateOf<DogImageUiState>(DogImageUiState.Loading) }

    Box(
        modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Dog Image",
            modifier = Modifier.matchParentSize(),
            onSuccess = {
                uiState = DogImageUiState.Success(imageUrl)
            },
            onLoading = {
                uiState = DogImageUiState.Loading
            },
            onError = {
                uiState = DogImageUiState.Error
            }
        )

        when (uiState) {
            is DogImageUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant)
            }

            is DogImageUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = "Error", tint = Color.Red)
                    Text("Nie udało się załadować obrazka", color = Color.Red, fontSize = 14.sp)
                }
            }

            is DogImageUiState.Success -> Unit
        }
    }
}