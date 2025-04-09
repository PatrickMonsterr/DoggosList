package com.example.doggoslist

sealed class DogImageUiState {
    object Loading : DogImageUiState()
    object Error : DogImageUiState()
    data class Success(val url: String) : DogImageUiState()
}