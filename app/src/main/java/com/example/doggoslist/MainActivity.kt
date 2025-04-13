package com.example.doggoslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doggoslist.ui.theme.DoggosListTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doggoslist.model.AddDogViewModel
import com.example.doggoslist.model.DogDetailViewModel
import com.example.doggoslist.model.DogViewModel
//import com.example.doggoslist.model.addDogViewModel
import com.example.doggoslist.ui.screens.MainScreen.TopBar
import com.example.doggoslist.ui.screens.MainScreen.SearchBar
import com.example.doggoslist.ui.screens.MainScreen.DogList
import com.example.doggoslist.ui.screens.SettingsScreen.SettingsScreen
import com.example.doggoslist.ui.screens.ProfileScreen.ProfileScreen
import com.example.doggoslist.ui.screens.DogDetailsScreen.DogDetailScreen
import com.example.doggoslist.ui.screens.AddDogScreen.AddDogScreen



data class Dog(
    val name: String,
    val breed: String,
    var isLiked: Boolean = false,
    var imageUrl: String? = null
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DoggosListTheme {
                val navController = rememberNavController()
                val dogViewModel: DogViewModel = viewModel()
                val addDogViewModel: AddDogViewModel = viewModel()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentWindowInsets = WindowInsets(0)
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("main") {
                            DogListScreen(navController = navController, viewModel = dogViewModel)
                        }

                        composable("settings") {
                            SettingsScreen { navController.popBackStack() }
                        }
                        composable("profile") {
                            ProfileScreen { navController.popBackStack() }
                        }
                        composable("details/{name}/{breed}") { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val breed = backStackEntry.arguments?.getString("breed") ?: ""

                            val dogDetailViewModel: DogDetailViewModel = viewModel()
                            val dog by dogDetailViewModel.dogState.collectAsState()

                            LaunchedEffect(Unit) {
                                if (dog == null) {
                                    val foundDog = dogViewModel.dogs.find { it.name == name && it.breed == breed }
                                    dogDetailViewModel.setDog(foundDog ?: Dog(name, breed))
                                }
                            }

                            DogDetailScreen(
                                dog = dog ?: Dog(name, breed),
                                onBackClick = { navController.popBackStack() },
                                onDeleteClick = {
                                    dog?.let {
                                        dogViewModel.deleteDog(it)
                                    }
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("addDog") {
                            AddDogScreen(
                                onBackClick = { navController.popBackStack() },
                                addDogViewModel = addDogViewModel,
                                onAddDog = { name, breed, imageUrl ->
                                    dogViewModel.addDog(name, breed, imageUrl)
                                    addDogViewModel.fetchRandomImage()
                                    navController.popBackStack()
                                }
                            )
                        }



                    }
                }
            }
        }
    }
}



@Composable
fun DogListScreen(navController: NavHostController, viewModel: DogViewModel) {
    Column(modifier = Modifier.background(Color.White)) {
        TopBar(navController)

        SearchBar(
            name = viewModel.dogName,
            onNameChange = viewModel::onNameChange,
            onAddClick = {
                navController.navigate("addDog")
            },
            isDuplicate = viewModel.isDuplicate
        )


        DogList(
            dogList = viewModel.filteredDogs,
            onDeleteClick = viewModel::deleteDog,
            onDogClick = { dog ->
                navController.navigate("details/${dog.name}/${dog.breed}")
            },
            onHeartClick = viewModel::toggleLike
        )
    }
}















