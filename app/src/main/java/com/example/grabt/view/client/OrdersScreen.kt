package com.example.grabt.view.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.grabt.dao.OrderDao
import com.example.grabt.dao.RestaurantDao
import com.example.grabt.model.Order
import com.example.grabt.model.Restaurant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    navController: NavController,
    orderDao: OrderDao,
    restaurantDao: RestaurantDao,
    clientId: Int
) {
    val allOrders by orderDao.getOrdersForClient(clientId).collectAsState(initial = emptyList())
    val allRestaurants by restaurantDao.getAllRestaurants().collectAsState(initial = emptyList())

    val activeOrders = allOrders.filter { it.estado != "Entregue" && it.estado != "Cancelado" }
    val pastOrders = allOrders.filter { it.estado == "Entregue" || it.estado == "Cancelado" }

    OrdersScreenContent(
        activeOrders = activeOrders,
        pastOrders = pastOrders,
        restaurants = allRestaurants,
        onBackClick = { navController.popBackStack() }
    )
}

@Composable
fun OrdersScreenContent(
    activeOrders: List<Order>,
    pastOrders: List<Order>,
    restaurants: List<Restaurant>,
    onBackClick: () -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)

    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor).padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp).background(cardColor, shape = RoundedCornerShape(12.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
            ) {
                Text("<", fontWeight = FontWeight.Bold, color = darkColor, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Os Teus Pedidos", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Text("Em Curso", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 16.dp))
            }

            if (activeOrders.isEmpty()) {
                item {
                    Text("Não tens pedidos a decorrer de momento.", color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp), fontSize = 14.sp)
                }
            } else {
                items(activeOrders) { order ->
                    OrderCardDynamic(order = order, restaurants = restaurants, darkColor = darkColor, cardColor = cardColor)
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            item {
                Text("Histórico", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 16.dp))
            }

            if (pastOrders.isEmpty()) {
                item {
                    Text("Ainda não tens histórico de encomendas finalizadas.", color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            } else {
                items(pastOrders) { order ->
                    OrderCardDynamic(order = order, restaurants = restaurants, darkColor = darkColor, cardColor = cardColor)
                }
            }
        }
    }
}

@Composable
fun OrderCardDynamic(order: Order, restaurants: List<Restaurant>, darkColor: Color, cardColor: Color) {
    val restaurantName = restaurants.find { it.id == order.restauranteId }?.nome ?: "Restaurante GRAB!T"
    val timeString = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(order.dataHora))

    // CORES DINÂMICAS BASEADAS NO ESTADO REAL
    val statusColor = when (order.estado) {
        "Pendente", "A Preparar" -> Color(0xFFD32F2F) // Vermelho
        "A Aguardar Estafeta", "Em Caminho" -> Color(0xFFF57C00) // Laranja
        "Entregue" -> Color(0xFF388E3C) // Verde
        else -> darkColor
    }

    val logoResource = when (order.restauranteId) {
        1 -> R.drawable.logo_mcd
        2 -> R.drawable.logo_kfc
        3 -> R.drawable.logo_bk
        4 -> R.drawable.logo_pizzahut
        else -> R.drawable.logo_grabit
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = logoResource),
            contentDescription = "Logo Restaurante",
            modifier = Modifier.size(56.dp).background(Color.White, RoundedCornerShape(12.dp)).border(2.dp, darkColor, RoundedCornerShape(12.dp)).padding(8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = restaurantName, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
            Text(text = timeString, fontSize = 13.sp, color = darkColor.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = String.format("%.2f €", order.valorTotal), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
        }

        Box(
            modifier = Modifier.border(BorderStroke(2.dp, statusColor), shape = RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = order.estado, color = statusColor, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenPreview() {
    OrdersScreenContent(
        activeOrders = listOf(Order(id = 1, clienteId = 1, restauranteId = 3, valorTotal = 14.20, estado = "A Preparar")),
        pastOrders = listOf(Order(id = 2, clienteId = 1, restauranteId = 1, valorTotal = 18.50, estado = "Entregue")),
        restaurants = emptyList(),
        onBackClick = {}
    )
}