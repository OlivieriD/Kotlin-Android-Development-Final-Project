package com.example.autocare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autocare.data.database.AutoCareDatabase
import com.example.autocare.data.entity.Mechanic
import com.example.autocare.data.repository.MechanicRepository
import com.example.autocare.ui.components.MechanicCard
import com.example.autocare.ui.navigation.Screen
import com.example.autocare.viewmodel.MechanicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicListScreen(
    navController: NavController,
    carId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = MechanicRepository(database.mechanicDao())
    val viewModel: MechanicViewModel = viewModel { MechanicViewModel(repository) }

    val mechanics by viewModel.mechanics.collectAsState()

    LaunchedEffect(carId) {
        viewModel.loadMechanicsByCarId(carId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Mechanics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditMechanic.createRoute(carId)) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add Mechanic")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (mechanics.isEmpty()) {
                EmptyMechanicState(
                    onAddMechanicClick = { navController.navigate(Screen.AddEditMechanic.createRoute(carId)) },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(mechanics) { mechanic ->
                        MechanicCard(
                            mechanic = mechanic,
                            onEditClick = {
                                navController.navigate(
                                    "${Screen.AddEditMechanic.createRoute(carId)}?mechanicId=${mechanic.id}"
                                )
                            },
                            onDeleteClick = { viewModel.deleteMechanic(mechanic) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMechanicState(
    onAddMechanicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No mechanics saved",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAddMechanicClick) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("Add First Mechanic")
        }
    }
}