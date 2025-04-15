package com.example.doggoslist.model
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggoslist.Dog
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {

    val ddogs = mutableStateListOf<Dog>()
//    var isLoading by mutableStateOf(false)
//        private set
//
//    var isError by mutableStateOf(false)
//        private set
    fun addDog(name: String, breed: String, url: String?) {
        viewModelScope.launch {
//            isLoading = true
//            isError = false

            try {
                ddogs.add(0, Dog(name = name, breed = breed, imageUrl = url))
//                isLoading = false
            } catch (_: Exception) {
//                isError = true
//                isLoading = false
            }
        }
    }


    val dogs: List<Dog> get() = ddogs

    var dogName by mutableStateOf("")
        private set

    var isDuplicate by mutableStateOf(false)
        private set

    val filteredDogs: List<Dog>
        get() = if (dogName.isBlank()) {
            ddogs.sortedByDescending { it.isLiked }
        } else {
            ddogs.filter { it.name.contains(dogName, ignoreCase = true) }
                .sortedByDescending { it.isLiked }
        }

    fun onNameChange(newName: String) {
        dogName = newName
        isDuplicate = false
    }


    fun deleteDog(dog: Dog) {
        ddogs.remove(dog)
    }

    fun toggleLike(dog: Dog) {
        val index = ddogs.indexOf(dog)
        if (index != -1) {
            ddogs[index] = dog.copy(isLiked = !dog.isLiked)
        }
    }


}
