package com.example.doggoslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
import androidx.compose.ui.platform.LocalContext
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
import coil.request.ImageRequest
import com.example.doggoslist.model.DogViewModel


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
                            val dog = dogViewModel.dogs.find { it.name == name && it.breed == breed } ?: Dog(name, breed)
                            DogDetailScreen(
                                dog = dog,
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
                        composable("addDog") {
                            AddDogScreen(
                                onBackClick = { navController.popBackStack() },
                                onAddDog = { name, breed ->
                                    dogViewModel.addDog(name, breed)
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
            IconButton(onClick = onAddClick) {
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
                                .clip(RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (dog.imageUrl != null) {
                                AsyncImage(
                                    model = dog.imageUrl,
                                    contentDescription = "Dog image",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Text(text = "🐕", fontSize = 24.sp)
                            }
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
                        Icon(
                            imageVector = if (dog.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Heart",
                            tint = if (dog.isLiked) Color.Red else Color.Gray,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onHeartClick(dog) }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(32.dp)
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
            if (dog.imageUrl != null) {
                DogImageScreen(imageUrl = dog.imageUrl!!)
            } else {
                Text(text = "🐕", fontSize = 48.sp)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDogScreen(
    onBackClick: () -> Unit,
    onAddDog: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var breed by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dodaj Psa") },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "🐶", fontSize = 120.sp)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Imię") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Rasa") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.text.isNotBlank() && breed.text.isNotBlank()) {
                        onAddDog(name.text.trim(), breed.text.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Dodaj")
            }
        }
    }
}
sealed class DogImageUiState {
    object Loading : DogImageUiState()
    object Error : DogImageUiState()
    data class Success(val url: String) : DogImageUiState()
}

@Composable
fun DogImageScreen(imageUrl: String, modifier: Modifier = Modifier) {
    var uiState by remember { mutableStateOf<DogImageUiState>(DogImageUiState.Loading) }

    Box(
        modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Dog Image",
            modifier = Modifier.matchParentSize(),
            onSuccess = {
                uiState = DogImageUiState.Success(imageUrl)
            },
            onLoading = {
                uiState = DogImageUiState.Loading
            },
            onError = {
                uiState = DogImageUiState.Error
            }
        )

        when (uiState) {
            is DogImageUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant)
            }

            is DogImageUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Warning, contentDescription = "Error", tint = Color.Red)
                    Text("Nie udało się załadować obrazka", color = Color.Red, fontSize = 14.sp)
                }
            }

            is DogImageUiState.Success -> Unit
        }
    }
}






