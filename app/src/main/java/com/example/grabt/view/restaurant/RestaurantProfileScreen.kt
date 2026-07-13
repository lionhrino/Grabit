package com.example.grabt.view.restaurant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grabt.R
import com.example.grabt.dao.RestaurantDao
import com.example.grabt.model.Restaurant

@Composable
fun RestaurantProfileScreen(
    navController: NavController,
    restaurantDao: RestaurantDao,
    restaurantId: Int
) {
    val restaurant by restaurantDao.getRestaurantById(restaurantId).collectAsState(initial = null)

    RestaurantProfileContent(
        restaurant = restaurant,
        onNavigateHome = { navController.navigate("restaurant_home") { popUpTo(0) } },
        onNavigateOrders = { navController.navigate("restaurant_orders") },
        onNavigateMenu = { navController.navigate("restaurant_menu_manager") },
        onLogoutClick = { navController.navigate("initial_screen") { popUpTo(0) } }
    )
}

@Composable
fun RestaurantProfileContent(
    restaurant: Restaurant?,
    onNavigateHome: () -> Unit,
    onNavigateOrders: () -> Unit,
    onNavigateMenu: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cardColor, modifier = Modifier.border(BorderStroke(2.dp, darkColor))) {
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Painel", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Painel", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateHome() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateOrders() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Menu", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Menu", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateMenu() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) }, selected = true, onClick = { /* Já estamos aqui */ }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp).verticalScroll(rememberScrollState())
        ) {
            Text("O Teu Restaurante", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(top = 32.dp, bottom = 24.dp))

            Row(
                modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(70.dp).background(backgroundColor, shape = CircleShape).border(BorderStroke(2.dp, darkColor), shape = CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Avatar", tint = darkColor, modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = restaurant?.nome ?: "A carregar...", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
                    Text(text = restaurant?.email ?: "...", fontSize = 14.sp, color = darkColor.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
                    Text(text = "${restaurant?.concelho ?: ""}, ${restaurant?.distrito ?: ""}", fontSize = 12.sp, color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Ganhos da Plataforma", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 12.dp))

            Column(
                modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Saldo Disponível para Levantamento", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.8f))
                Text(text = String.format("%.2f €", restaurant?.saldo ?: 0.0), fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Lógica de levantar dinheiro */ },
                    colors = ButtonDefaults.buttonColors(containerColor = darkColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("LEVANTAR FUNDOS", color = backgroundColor, fontWeight = FontWeight.ExtraBold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(containerColor = actionRed),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(60.dp).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))
            ) {
                Text("TERMINAR SESSÃO", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantProfilePreview() {
    RestaurantProfileContent(
        restaurant = Restaurant(nome = "McDonald's", email = "mcd@grabt.com", password = "", rua = "Rua", codigoPostal = "1000", concelho = "Lisboa", distrito = "Lisboa", saldo = 145.50),
        onNavigateHome = {}, onNavigateOrders = {}, onNavigateMenu = {}, onLogoutClick = {}
    )
}