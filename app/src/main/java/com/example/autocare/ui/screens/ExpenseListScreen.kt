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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.autocare.data.database.AutoCareDatabase
import com.example.autocare.data.database.YearlyExpense
import com.example.autocare.data.repository.ExpenseRepository
import com.example.autocare.ui.components.ExpenseCard
import com.example.autocare.ui.navigation.Screen
import com.example.autocare.viewmodel.ExpenseViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navController: NavController,
    carId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AutoCareDatabase.getDatabase(context)
    val repository = ExpenseRepository(database.expenseDao())
    val viewModel: ExpenseViewModel = viewModel { ExpenseViewModel(repository) }

    val expenseList by viewModel.expenseList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val categoryExpenses by viewModel.categoryExpenses.collectAsState()
    // NEW: Collect yearly expenses from ViewModel
    val yearlyExpenses by viewModel.yearlyExpenses.collectAsState()

    LaunchedEffect(carId) {
        viewModel.loadExpensesByCarId(carId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Records") },
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
                    navController.navigate(Screen.AddEditExpense.createRoute(carId))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Expense"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Summary Card (Updated with yearly expenses)
            if (totalExpenses > 0 || categoryExpenses.isNotEmpty() || yearlyExpenses.isNotEmpty()) {
                ExpenseSummaryCard(
                    totalExpenses = totalExpenses,
                    categoryExpenses = categoryExpenses,
                    yearlyExpenses = yearlyExpenses, // NEW: Pass yearly data
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Expense List
            Box(
                modifier = Modifier.weight(1f)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    expenseList.isEmpty() -> {
                        EmptyExpenseState(
                            onAddExpenseClick = {
                                navController.navigate(Screen.AddEditExpense.createRoute(carId))
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(expenseList) { expense ->
                                ExpenseCard(
                                    expense = expense,
                                    onExpenseClick = { /* Navigate to details */ },
                                    onEditClick = { selectedExpense ->
                                        navController.navigate(
                                            "${Screen.AddEditExpense.createRoute(carId)}?expenseId=${selectedExpense.id}"
                                        )
                                    },
                                    onDeleteClick = { selectedExpense ->
                                        viewModel.deleteExpense(selectedExpense)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Updated ExpenseSummaryCard with yearly breakdown
@Composable
private fun ExpenseSummaryCard(
    totalExpenses: Double,
    categoryExpenses: List<com.example.autocare.data.database.CategoryExpense>,
    yearlyExpenses: List<YearlyExpense>, // NEW: Yearly parameter
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Expense Summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: ${String.format(Locale.getDefault(), "$%.2f", totalExpenses)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // Yearly Breakdown Section (NEW)
            if (yearlyExpenses.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "By Year:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                yearlyExpenses.forEach { yearlyExpense ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = yearlyExpense.year,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "$%.2f", yearlyExpense.total),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Category Breakdown
            if (categoryExpenses.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "By Category:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                categoryExpenses.take(5).forEach { categoryExpense ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = categoryExpense.category,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "$%.2f", categoryExpense.total),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

// Rest of the file remains unchanged
@Composable
private fun EmptyExpenseState(
    modifier: Modifier = Modifier,
    onAddExpenseClick: () -> Unit
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
            text = "No expenses recorded",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start tracking your vehicle expenses like fuel, maintenance, and insurance",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddExpenseClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add First Expense")
        }
    }
}