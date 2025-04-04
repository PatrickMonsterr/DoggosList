package com.example.doggoslist.model
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.doggoslist.Dog
import androidx.lifecycle.viewModelScope
import dogApi
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {

    private val _dogs = mutableStateListOf<Dog>()

    val dogs: List<Dog> get() = _dogs

    var dogName by mutableStateOf("")
        private set

    var isDuplicate by mutableStateOf(false)
        private set

    val filteredDogs: List<Dog>
        get() = if (dogName.isBlank()) {
            _dogs.sortedByDescending { it.isLiked }
        } else {
            _dogs.filter { it.name.contains(dogName, ignoreCase = true) }
                .sortedByDescending { it.isLiked }
        }

    fun onNameChange(newName: String) {
        dogName = newName
        isDuplicate = false
    }

    fun addDog(name: String, breed: String) {
        viewModelScope.launch {
            val imageUrl = try {
                dogApi.getRandomDogImage().message
            } catch (e: Exception) {
                null
            }
            _dogs.add(0, Dog(name = name, breed = breed, imageUrl = imageUrl))
        }
    }


    fun deleteDog(dog: Dog) {
        _dogs.remove(dog)
    }

    fun toggleLike(dog: Dog) {
        val index = _dogs.indexOf(dog)
        if (index != -1) {
            _dogs[index] = dog.copy(isLiked = !dog.isLiked)
        }
    }
}
