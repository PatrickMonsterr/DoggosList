package com.example.doggoslist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doggoslist.ui.theme.DoggosListTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doggoslist.model.DogViewModel


data class Dog(
    val name: String,
    val breed: String,
    var isLiked: Boolean = false
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
                            DogDetailScreen(
                                dog = Dog(name, breed),
                                onBackClick = { navController.popBackStack() },
                                onDeleteClick = {
                                    val dogToDelete = dogViewModel.dogs.find { it.name == name && it.breed == breed }
                                    if (dogToDelete != null) {
                                        dogViewModel.deleteDog(dogToDelete)
                                    }

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = { Text("Doggos") },
        navigationIcon = {
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.Settings, contentDescription = "settings")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "profile")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.light_pink),
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

@Composable
fun SearchBar(
    name: String,
    onNameChange: (String) -> Unit,
    onAddClick: () -> Unit,
    isDuplicate: Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text("Dodaj pieska 🐶") },
                modifier = Modifier.weight(1f),
                isError = isDuplicate,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (isDuplicate) Color(0xFFFFE6E6) else Color.White,
                    unfocusedContainerColor = if (isDuplicate) Color(0xFFFFE6E6) else Color.White,
                    focusedBorderColor = if (isDuplicate) Color.Red else Color.Gray,
                    unfocusedBorderColor = if (isDuplicate) Color.Red else Color.LightGray,
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    errorBorderColor = Color.Red,
                    errorCursorColor = Color.Red,
                    errorContainerColor = Color(0xFFFFE6E6)
                )

            )
            IconButton(onClick = onAddClick, enabled = name.isNotBlank()) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Magenta,
                    modifier = Modifier.size(32.dp)
                )


            }

        }
    }
    if (isDuplicate) {
        Text(
            text = "Piesek już istnieje!",
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
fun DogList(
    dogList: List<Dog>,
    onDeleteClick: (Dog) -> Unit,
    onDogClick: (Dog) -> Unit,
    onHeartClick: (Dog) -> Unit
) {
    LazyColumn {
        items(dogList) { dog ->
            Column(modifier = Modifier.clickable { onDogClick(dog) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF6A5ACD), Color(0xFFFFC0CB))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🐕", fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = dog.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                text = dog.breed,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF6A5ACD), Color(0xFFFFC0CB))
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (dog.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Heart",
                                tint = if (dog.isLiked) Color.Red else Color.Gray,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onHeartClick(dog) }
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                            modifier = Modifier
                                .clickable { onDeleteClick(dog) }
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ustawienia") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.light_pink),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) {}
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.light_pink),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogDetailScreen(dog: Dog, onBackClick: () -> Unit, onDeleteClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detale") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.light_pink),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF6A5ACD), Color(0xFFFFC0CB))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐕", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = dog.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = dog.breed, fontSize = 16.sp, color = Color.Gray)
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
            onAddClick = viewModel::addDog,
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


