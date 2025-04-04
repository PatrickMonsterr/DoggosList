package com.example.doggoslist.model
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.doggoslist.Dog

class DogViewModel : ViewModel() {

    private val _dogs = mutableStateListOf(
        Dog("Reksio", "Kundelek"),
        Dog("Burek", "Owczarek niemiecki"),
        Dog("Azor", "Labrador"),
        Dog("Fafik", "Beagle"),
        Dog("Luna", "Golden Retriever"),
        Dog("Max", "Husky"),
        Dog("Bella", "Pudel"),
        Dog("Rocky", "Bulldog"),
        Dog("Tosia", "Cocker Spaniel"),
        Dog("Kira", "Dalmatyńczyk")
    )
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

    fun addDog() {
        val name = dogName.trim()
        if (name.isNotBlank()) {
            if (_dogs.any { it.name.equals(name, ignoreCase = true) }) {
                isDuplicate = true
            } else {
                _dogs.add(0, Dog(name, "Nieznana"))
                dogName = ""
                isDuplicate = false
            }
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
