package edu.itvo.kmp1

import androidx.compose.material.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.itvo.kmp1.shared.presentation.screen.CustomerFormScreen
import edu.itvo.kmp1.shared.presentation.screen.CustomerListScreen
import edu.itvo.kmp1.shared.presentation.screen.ProductFormScreen
import edu.itvo.kmp1.shared.presentation.screen.ProductListScreen
import androidx.navigation.toRoute


import kotlinx.serialization.Serializable

@Serializable
object ProductListRoute
@Serializable
object CustomerListRoute
@Serializable
object CreateProductRoute
@Serializable
data class EditProductRoute(val productCode: String)
@Serializable
object CreateCustomerRoute
@Serializable
data class EditCustomerRoute(val customerCode: String)

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = ProductListRoute) {
            composable<ProductListRoute> {
                ProductListScreen(
                    onAddProduct = { navController.navigate(CreateProductRoute) },
                    onGoToCustomers = { navController.navigate(CustomerListRoute) },
                    onEditProduct = { productCode -> navController.navigate(EditProductRoute(productCode)) }
                )
            }
            
            composable<CustomerListRoute> {
                CustomerListScreen(
                    onAddCustomer = { navController.navigate(CreateCustomerRoute) },
                    onGoToProducts = { navController.navigate(ProductListRoute) },
                    onEditCustomer = { customerCode -> navController.navigate(EditCustomerRoute(customerCode)) }
                )
            }

            composable<CreateProductRoute> { 
                ProductFormScreen(
                    productCode = null,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<EditProductRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<EditProductRoute>()
                ProductFormScreen(
                    productCode = route.productCode,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable<CreateCustomerRoute> {
                CustomerFormScreen(
                    customerId = null,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<EditCustomerRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<EditCustomerRoute>()
                CustomerFormScreen(
                    customerId = route.customerCode,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}