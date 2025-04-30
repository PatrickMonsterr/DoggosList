package com.example.doggoslist.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.verticalcoding.mystudentlist.data.local.database.DogEntityDao

class DogViewModelFactory(private val dogDao: DogEntityDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DogViewModel::class.java)) {
            return DogViewModel(dogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
