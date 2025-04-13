package com.example.doggoslist.model

import androidx.lifecycle.ViewModel
import com.example.doggoslist.Dog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DogDetailViewModel : ViewModel() {

    private val _dogState = MutableStateFlow<Dog?>(null)
    val dogState: StateFlow<Dog?> = _dogState

    fun setDog(dog: Dog) {
        _dogState.value = dog
    }


}
