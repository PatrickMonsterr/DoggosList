package com.example.doggoslist.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggoslist.DogImageUiState
import dogApi
import kotlinx.coroutines.launch

class AddDogViewModel() : ViewModel() {

    var imageState = mutableStateOf<DogImageUiState>(DogImageUiState.Loading)
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
            } catch (_: Exception) {
                imageState.value = DogImageUiState.Error
            }
        }
    }

}
