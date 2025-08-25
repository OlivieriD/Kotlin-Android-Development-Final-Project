package com.example.autocare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autocare.data.database.AutoCareDatabase
import com.example.autocare.data.entity.Mechanic
import com.example.autocare.data.repository.MechanicRepository
import com.example.autocare.viewmodel.MechanicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMechanicScreen(
    navController: NavController,
    carId: Int,
    mechanicId: Int? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = MechanicRepository(database.mechanicDao())
    val viewModel: MechanicViewModel = viewModel { MechanicViewModel(repository) }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var serviceCenter by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val isEditing = mechanicId != null
    val selectedMechanic by viewModel.selectedMechanic.collectAsState()

    LaunchedEffect(mechanicId) {
        mechanicId?.let { viewModel.getMechanicById(it) }
    }

    LaunchedEffect(selectedMechanic) {
        selectedMechanic?.let {
            name = it.name
            phone = it.phone
            serviceCenter = it.serviceCenter
            notes = it.notes
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Mechanic" else "Add Mechanic") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = serviceCenter,
                onValueChange = { serviceCenter = it },
                label = { Text("Service Center") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val mechanic = if (isEditing) {
                        selectedMechanic!!.copy(
                            name = name.trim(),
                            phone = phone.trim(),
                            serviceCenter = serviceCenter.trim(),
                            notes = notes.trim()
                        )
                    } else {
                        Mechanic(
                            carId = carId,
                            name = name.trim(),
                            phone = phone.trim(),
                            serviceCenter = serviceCenter.trim(),
                            notes = notes.trim()
                        )
                    }

                    if (isEditing) {
                        viewModel.updateMechanic(mechanic)
                    } else {
                        viewModel.insertMechanic(mechanic)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && phone.isNotBlank()
            ) {
                Text(if (isEditing) "Update" else "Add Mechanic")
            }
        }
    }
}