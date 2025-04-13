package com.example.doggoslist.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggoslist.Dog
import com.example.doggoslist.DogImageUiState
import dogApi
import kotlinx.coroutines.launch

class AddDogViewModel() : ViewModel() {

    var imageState = androidx.compose.runtime.mutableStateOf<DogImageUiState>(DogImageUiState.Loading)
        private set

    init {
        fetchRandomImage()
    }



    fun fetchRandomImage() {
        imageState.value = DogImageUiState.Loading
        viewModelScope.launch {
            try {
                val result = dogApi.getRandomDogImage().message
                imageState.value = DogImageUiState.Success(result)
            } catch (e: Exception) {
                imageState.value = DogImageUiState.Error
            }
        }
    }

    fun getCurrentUrl(): String? {
        return (imageState.value as? DogImageUiState.Success)?.url
    }
}
