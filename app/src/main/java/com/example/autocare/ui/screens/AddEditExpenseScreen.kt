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
import com.example.autocare.data.entity.Expense
import com.example.autocare.data.repository.ExpenseRepository
import com.example.autocare.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseScreen(
    navController: NavController,
    carId: Int,
    modifier: Modifier = Modifier,
    expenseId: Int? = null
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = ExpenseRepository(database.expenseDao())
    val viewModel: ExpenseViewModel = viewModel { ExpenseViewModel(repository) }

    // Form state
    var selectedCategory by remember { mutableStateOf("Fuel") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var expenseDate by remember { mutableStateOf(Calendar.getInstance()) }
    var location by remember { mutableStateOf("") }
    var odometer by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Validation errors
    var categoryError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }

    val expenseCategories = listOf(
        "Fuel",
        "Maintenance",
        "Insurance",
        "Repairs",
        "Registration",
        "Parking",
        "Tolls",
        "Car Wash",
        "Accessories",
        "Other"
    )

    val isEditing = expenseId != null
    val selectedExpense by viewModel.selectedExpense.collectAsState()

    // Load expense data if editing
    LaunchedEffect(expenseId) {
        if (expenseId != null) {
            viewModel.getExpenseById(expenseId)
        }
    }

    // Populate form when expense data is loaded
    LaunchedEffect(selectedExpense) {
        selectedExpense?.let { expense ->
            selectedCategory = expense.category
            amount = expense.amount.toString()
            description = expense.description
            expenseDate = Calendar.getInstance().apply {
                timeInMillis = expense.date
            }
            location = expense.location
            odometer = if (expense.odometer > 0) expense.odometer.toString() else ""
            notes = expense.notes
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Edit Expense" else "Add Expense")
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
            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = categoryError,
                    supportingText = if (categoryError) {
                        { Text("Please select a category") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    expenseCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                                categoryError = false
                            }
                        )
                    }
                }
            }

            // Amount Field
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = false
                },
                label = { Text("Amount ($) *") },
                placeholder = { Text("e.g., 65.50") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = amountError,
                supportingText = if (amountError) {
                    { Text("Please enter a valid amount") }
                } else null,
                modifier = Modifier.fillMaxWidth()
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("e.g., Regular unleaded, Oil change") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Date Field
            OutlinedTextField(
                value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(expenseDate.time),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date *") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    expenseDate = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, year)
                                        set(Calendar.MONTH, month)
                                        set(Calendar.DAY_OF_MONTH, day)
                                    }
                                },
                                expenseDate.get(Calendar.YEAR),
                                expenseDate.get(Calendar.MONTH),
                                expenseDate.get(Calendar.DAY_OF_MONTH)
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

            // Location Field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                placeholder = { Text("e.g., Shell Gas Station, Downtown Auto") },
                modifier = Modifier.fillMaxWidth()
            )

            // Odometer Field
            OutlinedTextField(
                value = odometer,
                onValueChange = { odometer = it },
                label = { Text("Odometer Reading (km)") },
                placeholder = { Text("e.g., 75500") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Notes Field
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                placeholder = { Text("Additional notes or details") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    // Validate form
                    categoryError = selectedCategory.isBlank()
                    amountError = amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() <= 0

                    if (!categoryError && !amountError) {
                        val expense = if (isEditing && selectedExpense != null) {
                            selectedExpense!!.copy(
                                category = selectedCategory,
                                amount = amount.toDouble(),
                                description = description.trim(),
                                date = expenseDate.timeInMillis,
                                location = location.trim(),
                                odometer = odometer.toIntOrNull() ?: 0,
                                notes = notes.trim()
                            )
                        } else {
                            Expense(
                                carId = carId,
                                category = selectedCategory,
                                amount = amount.toDouble(),
                                description = description.trim(),
                                date = expenseDate.timeInMillis,
                                location = location.trim(),
                                odometer = odometer.toIntOrNull() ?: 0,
                                notes = notes.trim()
                            )
                        }

                        if (isEditing) {
                            viewModel.updateExpense(expense)
                        } else {
                            viewModel.insertExpense(expense)
                        }

                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Update Expense" else "Add Expense")
            }
        }
    }
}