package com.example.grabt.view.restaurant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grabt.R
import com.example.grabt.dao.OrderDao

@Composable
fun RestaurantHomeScreen(
    navController: NavController,
    orderDao: OrderDao,
    restaurantId: Int
) {
    val pendingOrders by orderDao.getOrdersForRestaurantByStatus(restaurantId, "Pendente").collectAsState(initial = emptyList())
    val ganhosHoje by orderDao.getGanhosHoje(restaurantId).collectAsState(initial = 0.0)

    RestaurantHomeContent(
        pendingCount = pendingOrders.size,
        ganhosHoje = ganhosHoje ?: 0.0,
        onProfileClick = { navController.navigate("restaurant_profile") }, // ATUALIZADO
        onOrdersClick = { navController.navigate("restaurant_orders") },
        onMenuClick = { navController.navigate("restaurant_menu_manager") }
    )
}

@Composable
fun RestaurantHomeContent(
    pendingCount: Int,
    ganhosHoje: Double,
    onProfileClick: () -> Unit, // ATUALIZADO
    onOrdersClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cardColor, modifier = Modifier.border(BorderStroke(2.dp, darkColor))) {
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Painel", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Painel", color = darkColor, fontWeight = FontWeight.Bold) }, selected = true, onClick = { /* Já estamos aqui */ }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onOrdersClick() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Menu", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Menu", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onMenuClick() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onProfileClick() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 32.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Dashboard", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
                Image(painter = painterResource(id = R.drawable.logo_grabit), contentDescription = "Logo GRAB!T", modifier = Modifier.size(56.dp))
            }

            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DashboardCard(title = "Pendentes", value = pendingCount.toString(), valueColor = actionRed, darkColor = darkColor, cardColor = cardColor, modifier = Modifier.weight(1f))
                DashboardCard(title = "Ganhos Hoje", value = String.format("%.2f €", ganhosHoje), valueColor = darkColor, darkColor = darkColor, cardColor = cardColor, modifier = Modifier.weight(1f))
            }

            Box(
                modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(24.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(24.dp)).padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Bem-vindo ao teu Painel!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 12.dp))
                    Text("Usa a aba Pedidos lá em baixo para aceitar novas encomendas em tempo real e a aba Menu para adicionar pratos e gerir o teu stock disponível.", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = darkColor.copy(alpha = 0.8f), textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, valueColor: Color, darkColor: Color, cardColor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier.background(cardColor, shape = RoundedCornerShape(16.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantHomeScreenPreview() {
    RestaurantHomeContent(pendingCount = 2, ganhosHoje = 45.00, onProfileClick = {}, onOrdersClick = {}, onMenuClick = {})
}