package com.example.autocare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.autocare.ui.screens.CarListScreen
import com.example.autocare.ui.screens.AddEditCarScreen
import com.example.autocare.ui.screens.CarDetailsScreen
import com.example.autocare.ui.screens.MaintenanceListScreen
import com.example.autocare.ui.screens.AddEditMaintenanceScreen
import com.example.autocare.ui.screens.ExpenseListScreen
import com.example.autocare.ui.screens.AddEditExpenseScreen
import com.example.autocare.ui.screens.AddEditMechanicScreen
import com.example.autocare.ui.screens.MechanicListScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CarList.route,
        modifier = modifier
    ) {
        composable(Screen.CarList.route) {
            CarListScreen(navController = navController)
        }

        composable(
            route = "${Screen.AddEditCar.route}?carId={carId}",
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId")
            AddEditCarScreen(
                navController = navController,
                carId = if (carId == -1) null else carId
            )
        }
        composable(
            route = Screen.MechanicList.route,
            arguments = listOf(navArgument("carId") { type = NavType.IntType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            MechanicListScreen(navController, carId)
        }

        composable(
            route = "${Screen.AddEditMechanic.route}?mechanicId={mechanicId}",
            arguments = listOf(
                navArgument("carId") { type = NavType.IntType },
                navArgument("mechanicId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            val mechanicId = backStackEntry.arguments?.getInt("mechanicId")
            AddEditMechanicScreen(
                navController = navController,
                carId = carId,
                mechanicId = if (mechanicId == -1) null else mechanicId
            )
        }
        composable(
            route = Screen.CarDetails.route,
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            CarDetailsScreen(
                navController = navController,
                carId = carId
            )
        }

        composable(
            route = Screen.MaintenanceList.route,
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            MaintenanceListScreen(
                navController = navController,
                carId = carId
            )
        }

        composable(
            route = "${Screen.AddEditMaintenance.route}?maintenanceId={maintenanceId}",
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.IntType
                },
                navArgument("maintenanceId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            val maintenanceId = backStackEntry.arguments?.getInt("maintenanceId")
            AddEditMaintenanceScreen(
                navController = navController,
                carId = carId,
                maintenanceId = if (maintenanceId == -1) null else maintenanceId
            )
        }

        composable(
            route = Screen.ExpenseList.route,
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            ExpenseListScreen(
                navController = navController,
                carId = carId
            )
        }

        composable(
            route = "${Screen.AddEditExpense.route}?expenseId={expenseId}",
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.IntType
                },
                navArgument("expenseId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            val expenseId = backStackEntry.arguments?.getInt("expenseId")
            AddEditExpenseScreen(
                navController = navController,
                carId = carId,
                expenseId = if (expenseId == -1) null else expenseId
            )
        }
    }
}