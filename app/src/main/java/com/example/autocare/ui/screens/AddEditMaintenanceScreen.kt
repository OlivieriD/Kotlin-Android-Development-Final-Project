package com.example.autocare.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autocare.data.database.AutoCareDatabase
import com.example.autocare.data.entity.Maintenance
import com.example.autocare.data.repository.MaintenanceRepository
import com.example.autocare.viewmodel.MaintenanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMaintenanceScreen(
    navController: NavController,
    carId: Int,
    modifier: Modifier = Modifier,
    maintenanceId: Int? = null
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = MaintenanceRepository(database.maintenanceDao())
    val viewModel: MaintenanceViewModel = viewModel { MaintenanceViewModel(repository) }

    // Form state
    var selectedType by remember { mutableStateOf("Oil Change") }
    var description by remember { mutableStateOf("") }
    var scheduledDate by remember { mutableStateOf(Calendar.getInstance()) }
    var cost by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var mechanicName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Validation errors
    var typeError by remember { mutableStateOf(false) }

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }

    val maintenanceTypes = listOf(
        "Oil Change",
        "Tire Rotation",
        "Brake Service",
        "Engine Service",
        "Transmission Service",
        "Air Filter Change",
        "Spark Plug Replacement",
        "Battery Check",
        "Inspection",
        "General Service",
        "Other"
    )

    val isEditing = maintenanceId != null
    val selectedMaintenance by viewModel.selectedMaintenance.collectAsState()

    // Load maintenance data if editing
    LaunchedEffect(maintenanceId) {
        if (maintenanceId != null) {
            viewModel.getMaintenanceById(maintenanceId)
        }
    }

    // Populate form when maintenance data is loaded
    LaunchedEffect(selectedMaintenance) {
        selectedMaintenance?.let { maintenance ->
            selectedType = maintenance.type
            description = maintenance.description
            scheduledDate = Calendar.getInstance().apply {
                timeInMillis = maintenance.scheduledDate
            }
            cost = if (maintenance.cost > 0) maintenance.cost.toString() else ""
            mileage = if (maintenance.mileage > 0) maintenance.mileage.toString() else ""
            mechanicName = maintenance.mechanicName
            notes = maintenance.notes
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Edit Maintenance" else "Add Maintenance")
                },
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
            // Maintenance Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Maintenance Type *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = typeError,
                    supportingText = if (typeError) {
                        { Text("Please select a maintenance type") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    maintenanceTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                                typeError = false
                            }
                        )
                    }
                }
            }

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("e.g., Regular maintenance, Replace worn parts") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Scheduled Date Field
            OutlinedTextField(
                value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(scheduledDate.time),
                onValueChange = {},
                readOnly = true,
                label = { Text("Scheduled Date *") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    scheduledDate = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, year)
                                        set(Calendar.MONTH, month)
                                        set(Calendar.DAY_OF_MONTH, day)
                                    }
                                },
                                scheduledDate.get(Calendar.YEAR),
                                scheduledDate.get(Calendar.MONTH),
                                scheduledDate.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Cost Field
            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Cost ($)") },
                placeholder = { Text("e.g., 50.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Mileage Field
            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it },
                label = { Text("Current Mileage (km)") },
                placeholder = { Text("e.g., 75000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Mechanic Name Field
            OutlinedTextField(
                value = mechanicName,
                onValueChange = { mechanicName = it },
                label = { Text("Mechanic/Service Center") },
                placeholder = { Text("e.g., Joe's Auto Shop") },
                modifier = Modifier.fillMaxWidth()
            )

            // Notes Field
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                placeholder = { Text("Additional notes or observations") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    // Validate form
                    typeError = selectedType.isBlank()

                    if (!typeError) {
                        val maintenance = if (isEditing && selectedMaintenance != null) {
                            selectedMaintenance!!.copy(
                                type = selectedType,
                                description = description.trim(),
                                scheduledDate = scheduledDate.timeInMillis,
                                cost = cost.toDoubleOrNull() ?: 0.0,
                                mileage = mileage.toIntOrNull() ?: 0,
                                mechanicName = mechanicName.trim(),
                                notes = notes.trim()
                            )
                        } else {
                            Maintenance(
                                carId = carId,
                                type = selectedType,
                                description = description.trim(),
                                scheduledDate = scheduledDate.timeInMillis,
                                cost = cost.toDoubleOrNull() ?: 0.0,
                                mileage = mileage.toIntOrNull() ?: 0,
                                mechanicName = mechanicName.trim(),
                                notes = notes.trim()
                            )
                        }

                        if (isEditing) {
                            viewModel.updateMaintenance(maintenance)
                        } else {
                            viewModel.insertMaintenance(maintenance)
                        }

                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Update Maintenance" else "Add Maintenance")
            }
        }
    }
}