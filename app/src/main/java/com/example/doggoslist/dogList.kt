package com.example.doggoslist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

public class addDogViewModel
{
    public val _dogs = mutableStateListOf<Dog>()
    val dogs: List<Dog> get() = _dogs
}