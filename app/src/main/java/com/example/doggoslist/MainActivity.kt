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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doggoslist.ui.theme.DoggosListTheme

data class Dog(
    val name: String,
    val breed: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DoggosListTheme {
                val navController = rememberNavController()
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
                            var dogName by remember { mutableStateOf("") }
                            val dogList = remember {
                                mutableStateListOf(
                                    Dog("Reksio", "Kundelek"),
                                    Dog("Burek", "Owczarek niemiecki"),
                                    Dog("Azor", "Labrador"),
                                    Dog("Fafik", "Beagle"),
                                    Dog("Luna", "Golden Retriever"),
                                    Dog("Max", "Husky"),
                                    Dog("Bella", "Pudel"),
                                    Dog("Rocky", "Bulldog"),
                                    Dog("Tosia", "Cocker Spaniel"),
                                    Dog("Kira", "Dalmatyńczyk"),
                                    Dog("Kira", "Dalmatyńczyk"),
                                    Dog("Kira", "Dalmatyńczyk"),
                                    Dog("Kira", "Dalmatyńczyk")
                                )
                            }

                            var filteredDogs by remember { mutableStateOf<List<Dog>>(dogList) }

                            Column(modifier = Modifier.background(Color.White)) {
                                TopBar(navController)
                                SearchBar(
                                    name = dogName,
                                    onNameChange = { dogName = it },
                                    onSearchClick = {
                                        filteredDogs = dogList.filter { it.name.contains(dogName, ignoreCase = true) }
                                    },
                                    onAddClick = {
                                        if (dogName.isNotBlank()) {
                                            dogList.add(0, Dog(name = dogName, breed = "Nieznana"))
                                            filteredDogs = dogList
                                            dogName = ""
                                        }
                                    }
                                )
                                DogList(
                                    dogList = filteredDogs,
                                    onDeleteClick = { dog ->
                                        dogList.remove(dog)
                                        //filteredDogs = dogList.toList()
                                    }
                                )


                            }
                        }
                        composable("settings") {
                            SettingsScreen { navController.popBackStack() }
                        }
                        composable("profile") {
                            ProfileScreen { navController.popBackStack() }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: androidx.navigation.NavHostController) {
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
    onSearchClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text("Dodaj pieska 🐶") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )
        IconButton(onClick = onSearchClick) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
        }
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


@Composable
fun DogList(dogList: List<Dog>, onDeleteClick: (Dog) -> Unit) {
    LazyColumn {
        items(dogList) { dog ->
            Column {
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
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Heart",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
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
