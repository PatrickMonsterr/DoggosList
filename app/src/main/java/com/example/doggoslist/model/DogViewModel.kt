package com.example.doggoslist.model
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggoslist.Dog
import com.verticalcoding.mystudentlist.data.local.database.DogEntityDao
import kotlinx.coroutines.launch

class DogViewModel(private val dogDao: DogEntityDao) : ViewModel() {

    init {
        loadDogsFromDb()
    }

    val ddogs = mutableStateListOf<Dog>()
fun addDog(name: String, breed: String, url: String?) {
    viewModelScope.launch {
        val dog = Dog(name = name, breed = breed, imageUrl = url)
        ddogs.add(0, dog)

        val dogEntity = com.verticalcoding.mystudentlist.data.local.database.DogEntity(
            name = name,
            breed = breed,
            isLiked = false,
            imageUrl = url
        )
        dogDao.insertDog(dogEntity)
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
    private fun loadDogsFromDb() {
        viewModelScope.launch {
            dogDao.getAllDogs().collect { dogEntities ->
                ddogs.clear()
                ddogs.addAll(dogEntities.map { entity ->
                    Dog(
                        name = entity.name,
                        breed = entity.breed,
                        imageUrl = entity.imageUrl,
                        isLiked = entity.isLiked
                    )
                })
            }
        }
    }



}
