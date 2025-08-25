package com.example.autocare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autocare.data.database.AutoCareDatabase
import com.example.autocare.data.repository.CarRepository
import com.example.autocare.ui.navigation.Screen
import com.example.autocare.viewmodel.CarViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(
    navController: NavController,
    carId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = CarRepository(database.carDao())
    val viewModel: CarViewModel = viewModel { CarViewModel(repository) }

    val selectedCar by viewModel.selectedCar.collectAsState()

    LaunchedEffect(carId) {
        viewModel.getCarById(carId)
    }

    selectedCar?.let { car ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("${car.make} ${car.model}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate("${Screen.AddEditCar.route}?carId=${car.id}")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Car"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Car Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "${car.make} ${car.model}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Year ${car.year}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Details Section
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Vehicle Details",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        DetailRow(label = "Registration", value = car.registration)
                        if (car.color.isNotBlank()) {
                            DetailRow(label = "Color", value = car.color)
                        }
                        if (car.mileage > 0) {
                            DetailRow(label = "Mileage", value = "${car.mileage} km")
                        }
                        DetailRow(
                            label = "Added on",
                            value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                .format(Date(car.createdAt))
                        )
                    }
                }

                // Quick Actions
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Quick Actions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(
                                        Screen.MaintenanceList.createRoute(car.id)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Maintenance")
                            }
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(Screen.MechanicList.createRoute(car.id))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Mechanics")
                            }
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(
                                        Screen.ExpenseList.createRoute(car.id)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Expenses")
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    } ?: run {
        // Loading state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}