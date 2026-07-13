package com.example.grabt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.grabt.database.AppDatabase
import com.example.grabt.model.Restaurant
import com.example.grabt.model.Product
import com.example.grabt.UserSession

import com.example.grabt.view.InitialScreen
import com.example.grabt.view.LoginScreen
import com.example.grabt.view.RoleSelectionScreen
import com.example.grabt.view.ClientSignInScreen
import com.example.grabt.view.DeliverySignInScreen
import com.example.grabt.view.RestaurantSignInScreen

import com.example.grabt.view.client.ClientHomeScreen
import com.example.grabt.view.client.RestaurantListScreen
import com.example.grabt.view.client.RestaurantMenuScreen
import com.example.grabt.view.client.CartScreen
import com.example.grabt.view.client.ProfileScreen
import com.example.grabt.view.client.PromoScreen
import com.example.grabt.view.client.OrdersScreen

import com.example.grabt.view.restaurant.RestaurantHomeScreen
import com.example.grabt.view.restaurant.RestaurantOrdersScreen
import com.example.grabt.view.restaurant.RestaurantMenuManagerScreen
import com.example.grabt.view.restaurant.RestaurantProfileScreen

import com.example.grabt.view.delivery.DeliveryHomeScreen
import com.example.grabt.view.delivery.DeliveryProfileScreen

import com.example.grabt.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val clientDao = db.clientDao()
        val deliveryDao = db.deliveryDao()
        val restaurantDao = db.restaurantDao()
        val productDao = db.productDao()
        val orderDao = db.orderDao()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()
                    val cartViewModel: CartViewModel = viewModel()
                    val currentRestaurants by restaurantDao.getAllRestaurants().collectAsState(initial = emptyList())

                    LaunchedEffect(Unit) {
                        if (restaurantDao.getRestaurantCount() == 0) {
                            restaurantDao.insert(Restaurant(nome = "McDonald's", email = "mcd@grabt.com", password = "123", rua = "Rua Direita", codigoPostal = "1000", concelho = "Lisboa", distrito = "Lisboa"))
                            restaurantDao.insert(Restaurant(nome = "KFC", email = "kfc@grabt.com", password = "123", rua = "Rua Esquerda", codigoPostal = "2000", concelho = "Lisboa", distrito = "Lisboa"))
                            restaurantDao.insert(Restaurant(nome = "Burger King", email = "bk@grabt.com", password = "123", rua = "Avenida Central", codigoPostal = "3000", concelho = "Porto", distrito = "Porto"))
                            restaurantDao.insert(Restaurant(nome = "Pizza Hut", email = "ph@grabt.com", password = "123", rua = "Praça Nova", codigoPostal = "4000", concelho = "Faro", distrito = "Faro"))
                        }
                    }

                    LaunchedEffect(Unit) {
                        if (productDao.getProductCount() == 0) {
                            productDao.insert(Product(restauranteId = 1, nome = "Menu Big Mac", descricao = "O clássico com dois hambúrgueres...", preco = 7.50, stock = 10))
                            productDao.insert(Product(restauranteId = 1, nome = "McChicken", descricao = "Frango panado estaladiço...", preco = 5.20, stock = 15))
                            productDao.insert(Product(restauranteId = 1, nome = "Batatas Fritas", descricao = "As mais famosas do mundo.", preco = 2.80, stock = 50))
                            productDao.insert(Product(restauranteId = 2, nome = "Balde de Frango", descricao = "Receita secreta crocante.", preco = 14.90, stock = 8))
                        }
                    }

                    NavHost(navController = navController, startDestination = "initial_screen") {
                        composable("initial_screen") { InitialScreen(navController = navController) }
                        composable("login_screen") { LoginScreen(navController = navController, clientDao = clientDao, deliveryDao = deliveryDao, restaurantDao = restaurantDao) }
                        composable("role_selection") { RoleSelectionScreen(navController = navController) }
                        composable("client_sign_in") { ClientSignInScreen(clientDao = clientDao, deliveryDao = deliveryDao, restaurantDao = restaurantDao, onNavigateBack = { navController.popBackStack() }) }
                        composable("delivery_sign_in") { DeliverySignInScreen(clientDao = clientDao, deliveryDao = deliveryDao, restaurantDao = restaurantDao, onNavigateBack = { navController.popBackStack() }) }
                        composable("restaurant_sign_in") { RestaurantSignInScreen(clientDao = clientDao, deliveryDao = deliveryDao, restaurantDao = restaurantDao, onNavigateBack = { navController.popBackStack() }) }

                        composable("client_home") { ClientHomeScreen(navController = navController) }
                        composable("profile_screen") { ProfileScreen(navController = navController, clientDao = clientDao, clientId = UserSession.currentClientId) }
                        composable("restaurant_list") { RestaurantListScreen(navController = navController, restaurantList = currentRestaurants) }
                        composable(route = "restaurant_menu/{restaurantId}", arguments = listOf(navArgument("restaurantId") { type = NavType.IntType })) { backStackEntry ->
                            val restaurantId = backStackEntry.arguments?.getInt("restaurantId") ?: 0
                            RestaurantMenuScreen(navController = navController, restaurantId = restaurantId, cartViewModel = cartViewModel, productDao = productDao)
                        }
                        composable("cart_screen") { CartScreen(navController = navController, cartViewModel = cartViewModel, clientDao = clientDao, orderDao = orderDao, clientId = UserSession.currentClientId) }
                        composable("promo_screen") { PromoScreen(navController = navController) }

                        // CORREÇÃO: Rota atualizada para ler dados ao vivo na tela de pedidos!
                        composable("orders_screen") {
                            OrdersScreen(
                                navController = navController,
                                orderDao = orderDao,
                                restaurantDao = restaurantDao,
                                clientId = UserSession.currentClientId
                            )
                        }

                        composable("restaurant_home") { RestaurantHomeScreen(navController = navController, orderDao = orderDao, restaurantId = UserSession.currentRestaurantId) }
                        composable("restaurant_orders") { RestaurantOrdersScreen(navController = navController, orderDao = orderDao, restaurantId = UserSession.currentRestaurantId) }
                        composable("restaurant_menu_manager") { RestaurantMenuManagerScreen(navController = navController, productDao = productDao, restaurantId = UserSession.currentRestaurantId) }
                        composable("restaurant_profile") { RestaurantProfileScreen(navController = navController, restaurantDao = restaurantDao, restaurantId = UserSession.currentRestaurantId) }

                        composable("delivery_home") { DeliveryHomeScreen(navController = navController, orderDao = orderDao, deliveryId = UserSession.currentDeliveryId) }
                        composable("delivery_profile") { DeliveryProfileScreen(navController = navController, deliveryDao = deliveryDao, orderDao = orderDao, deliveryId = UserSession.currentDeliveryId) }
                    }
                }
            }
        }
    }
}