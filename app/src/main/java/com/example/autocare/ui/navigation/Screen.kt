package com.example.autocare.ui.navigation

sealed class Screen(val route: String) {
    data object CarList : Screen("car_list")
    data object AddEditCar : Screen("add_edit_car")
    data object CarDetails : Screen("car_details/{carId}") {
        fun createRoute(carId: Int) = "car_details/$carId"
    }
    data object MaintenanceList : Screen("maintenance_list/{carId}") {
        fun createRoute(carId: Int) = "maintenance_list/$carId"
    }
    data object AddEditMaintenance : Screen("add_edit_maintenance/{carId}") {
        fun createRoute(carId: Int) = "add_edit_maintenance/$carId"
    }
    data object ExpenseList : Screen("expense_list/{carId}") {
        fun createRoute(carId: Int) = "expense_list/$carId"
    }
    data object AddEditExpense : Screen("add_edit_expense/{carId}") {
        fun createRoute(carId: Int) = "add_edit_expense/$carId"
    }
    data object MechanicList : Screen("mechanic_list/{carId}") {
        fun createRoute(carId: Int) = "mechanic_list/$carId"
    }
    data object AddEditMechanic : Screen("add_edit_mechanic/{carId}") {
        fun createRoute(carId: Int) = "add_edit_mechanic/$carId"
    }
}