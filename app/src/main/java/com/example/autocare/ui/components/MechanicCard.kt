package com.example.autocare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.autocare.data.entity.Mechanic

@Composable
fun MechanicCard(
    mechanic: Mechanic,
    onEditClick: (Mechanic) -> Unit,
    onDeleteClick: (Mechanic) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mechanic.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mechanic.phone,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (mechanic.serviceCenter.isNotBlank()) {
                        Text(
                            text = mechanic.serviceCenter,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Row {
                    IconButton(onClick = { onEditClick(mechanic) }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }
            if (mechanic.notes.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = mechanic.notes,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Mechanic") },
            text = { Text("Are you sure you want to delete ${mechanic.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(mechanic)
                        showDeleteDialog = false
                    }
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) { Text("Cancel") }
            }
        )
    }
}