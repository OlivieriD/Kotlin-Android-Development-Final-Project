package com.example.autocare.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.autocare.data.database.AutoCareDatabase
import com.example.autocare.data.entity.Car
import com.example.autocare.data.repository.CarRepository
import com.example.autocare.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCarScreen(
    navController: NavController,
    carId: Int? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = CarRepository(database.carDao())
    val viewModel: CarViewModel = viewModel { CarViewModel(repository) }

    // Form state
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var registration by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Validation errors
    var makeError by remember { mutableStateOf(false) }
    var modelError by remember { mutableStateOf(false) }
    var yearError by remember { mutableStateOf(false) }
    var registrationError by remember { mutableStateOf(false) }

    // Image Picker
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

    val isEditing = carId != null
    val selectedCar by viewModel.selectedCar.collectAsState()

    // Load car data if editing
    LaunchedEffect(carId) {
        if (carId != null) {
            viewModel.getCarById(carId)
        }
    }

    // Populate form when car data is loaded
    LaunchedEffect(selectedCar) {
        selectedCar?.let { car ->
            make = car.make
            model = car.model
            year = car.year.toString()
            registration = car.registration
            color = car.color
            mileage = if (car.mileage > 0) car.mileage.toString() else ""
            imageUri = car.imageUri?.let { Uri.parse(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Car" else "Add New Car") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
            // Image Upload Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Car Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Make Field
            OutlinedTextField(
                value = make,
                onValueChange = {
                    make = it
                    makeError = false
                },
                label = { Text("Make *") },
                placeholder = { Text("e.g., Toyota, Honda, BMW") },
                isError = makeError,
                supportingText = if (makeError) {
                    { Text("Make is required") }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )

            // Model Field
            OutlinedTextField(
                value = model,
                onValueChange = {
                    model = it
                    modelError = false
                },
                label = { Text("Model *") },
                placeholder = { Text("e.g., Camry, Civic, X3") },
                isError = modelError,
                supportingText = if (modelError) {
                    { Text("Model is required") }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )

            // Year Field
            OutlinedTextField(
                value = year,
                onValueChange = {
                    year = it
                    yearError = false
                },
                label = { Text("Year *") },
                placeholder = { Text("e.g., 2020") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = yearError,
                supportingText = if (yearError) {
                    { Text("Valid year is required") }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )

            // Registration Field
            OutlinedTextField(
                value = registration,
                onValueChange = {
                    registration = it
                    registrationError = false
                },
                label = { Text("Registration/License Plate *") },
                placeholder = { Text("e.g., ABC-123") },
                isError = registrationError,
                supportingText = if (registrationError) {
                    { Text("Registration is required") }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )

            // Color Field (Optional)
            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                placeholder = { Text("e.g., Red, Blue, Silver") },
                modifier = Modifier.fillMaxWidth()
            )

            // Mileage Field (Optional)
            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it },
                label = { Text("Mileage (km)") },
                placeholder = { Text("e.g., 50000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    // Validate form
                    makeError = make.isBlank()
                    modelError = model.isBlank()
                    yearError = year.isBlank() || year.toIntOrNull() == null ||
                            year.toInt() < 1900 || year.toInt() > 2030
                    registrationError = registration.isBlank()

                    if (!makeError && !modelError && !yearError && !registrationError) {
                        val car = if (isEditing && selectedCar != null) {
                            selectedCar!!.copy(
                                make = make.trim(),
                                model = model.trim(),
                                year = year.toInt(),
                                registration = registration.trim(),
                                color = color.trim(),
                                mileage = mileage.toIntOrNull() ?: 0,
                                imageUri = imageUri?.toString()
                            )
                        } else {
                            Car(
                                make = make.trim(),
                                model = model.trim(),
                                year = year.toInt(),
                                registration = registration.trim(),
                                color = color.trim(),
                                mileage = mileage.toIntOrNull() ?: 0,
                                imageUri = imageUri?.toString()
                            )
                        }

                        if (isEditing) {
                            viewModel.updateCar(car)
                        } else {
                            viewModel.insertCar(car)
                        }

                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Update Car" else "Add Car")
            }
        }
    }
}