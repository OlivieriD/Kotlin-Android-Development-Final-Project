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
import com.example.autocare.data.repository.MaintenanceRepository
import com.example.autocare.ui.components.MaintenanceCard
import com.example.autocare.ui.navigation.Screen
import com.example.autocare.viewmodel.MaintenanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceListScreen(
    navController: NavController,
    carId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = MaintenanceRepository(database.maintenanceDao())
    val viewModel: MaintenanceViewModel = viewModel { MaintenanceViewModel(repository) }

    val maintenanceList by viewModel.maintenanceList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(carId) {
        viewModel.loadMaintenanceByCarId(carId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maintenance Records") },
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
                onClick = {
                    navController.navigate(Screen.AddEditMaintenance.createRoute(carId))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Maintenance"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                maintenanceList.isEmpty() -> {
                    EmptyMaintenanceState(
                        onAddMaintenanceClick = {
                            navController.navigate(Screen.AddEditMaintenance.createRoute(carId))
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(maintenanceList) { maintenance ->
                            MaintenanceCard(
                                maintenance = maintenance,
                                onMaintenanceClick = { /* Navigate to details */ },
                                onEditClick = { selectedMaintenance ->
                                    navController.navigate(
                                        "${Screen.AddEditMaintenance.createRoute(carId)}?maintenanceId=${selectedMaintenance.id}"
                                    )
                                },
                                onDeleteClick = { selectedMaintenance ->
                                    viewModel.deleteMaintenance(selectedMaintenance)
                                },
                                onCompleteClick = { selectedMaintenance ->
                                    viewModel.markAsCompleted(selectedMaintenance.id, true)
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
private fun EmptyMaintenanceState(
    modifier: Modifier = Modifier,
    onAddMaintenanceClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No maintenance records",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start tracking your car's maintenance schedule and history",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddMaintenanceClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add First Maintenance")
        }
    }
}