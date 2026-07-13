package com.example.grabt.view.restaurant

import androidx.compose.foundation.BorderStroke
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
import com.example.grabt.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RestaurantOrdersScreen(
    navController: NavController,
    orderDao: OrderDao,
    restaurantId: Int
) {
    val pendingOrders by orderDao.getOrdersForRestaurantByStatus(restaurantId, "Pendente").collectAsState(initial = emptyList())
    val preparingOrders by orderDao.getOrdersForRestaurantByStatus(restaurantId, "A Preparar").collectAsState(initial = emptyList())

    val coroutineScope = rememberCoroutineScope()

    RestaurantOrdersContent(
        pendingOrders = pendingOrders,
        preparingOrders = preparingOrders,
        orderDao = orderDao,
        onNavigateHome = { navController.navigate("restaurant_home") { popUpTo(0) } },
        onNavigateProfile = { navController.navigate("restaurant_profile") }, // ATUALIZADO
        onNavigateMenu = { navController.navigate("restaurant_menu_manager") },
        onAcceptOrder = { orderId ->
            coroutineScope.launch(Dispatchers.IO) { orderDao.updateOrderStatus(orderId, "A Preparar") }
        },
        onOrderReady = { orderId ->
            coroutineScope.launch(Dispatchers.IO) { orderDao.updateOrderStatus(orderId, "A Aguardar Estafeta") }
        }
    )
}

@Composable
fun RestaurantOrdersContent(
    pendingOrders: List<Order>,
    preparingOrders: List<Order>,
    orderDao: OrderDao?,
    onNavigateHome: () -> Unit,
    onNavigateProfile: () -> Unit, // ATUALIZADO
    onNavigateMenu: () -> Unit,
    onAcceptOrder: (Int) -> Unit,
    onOrderReady: (Int) -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)
    val successGreen = Color(0xFF388E3C)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cardColor, modifier = Modifier.border(BorderStroke(2.dp, darkColor))) {
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Painel", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Painel", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateHome() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) }, selected = true, onClick = { /* Já estamos aqui */ }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Menu", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Menu", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateMenu() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateProfile() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item {
                Text("Gestão de Pedidos", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 24.dp))
            }

            item {
                Text("Novos Pedidos (${pendingOrders.size})", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = actionRed, modifier = Modifier.padding(bottom = 16.dp))
            }

            if (pendingOrders.isEmpty()) {
                item { Text("Não tens novos pedidos de momento.", color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp)) }
            } else {
                items(pendingOrders) { order ->
                    RestaurantOrderCard(order = order, orderDao = orderDao, darkColor = darkColor, cardColor = cardColor, buttonColor = darkColor, buttonText = "ACEITAR PEDIDO") {
                        onAcceptOrder(order.id)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text("Na Cozinha (${preparingOrders.size})", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 16.dp))
            }

            if (preparingOrders.isEmpty()) {
                item { Text("A tua cozinha está limpa! Nenhum pedido em preparação.", color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold) }
            } else {
                items(preparingOrders) { order ->
                    RestaurantOrderCard(order = order, orderDao = orderDao, darkColor = darkColor, cardColor = cardColor, buttonColor = successGreen, buttonText = "PRONTO P/ ESTAFETA") {
                        onOrderReady(order.id)
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantOrderCard(
    order: Order,
    orderDao: OrderDao?,
    darkColor: Color,
    cardColor: Color,
    buttonColor: Color,
    buttonText: String,
    onActionClick: () -> Unit
) {
    val orderItems by orderDao?.getItemsForOrder(order.id)?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }
    val timeString = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(order.dataHora))

    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).background(cardColor, shape = RoundedCornerShape(16.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Pedido #${order.id}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = darkColor)
            Text(text = timeString, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = darkColor.copy(alpha = 0.7f))
        }

        Divider(modifier = Modifier.padding(vertical = 12.dp), color = darkColor.copy(alpha = 0.3f), thickness = 1.dp)

        if (orderItems.isEmpty()) {
            Text("A carregar detalhes...", fontSize = 14.sp, color = darkColor.copy(alpha = 0.7f))
        } else {
            orderItems.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                    Text(text = "${item.quantidade}x", fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.width(30.dp))
                    Text(text = item.nomeProduto, fontWeight = FontWeight.Bold, color = darkColor, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = String.format("Total: %.2f €", order.valorTotal), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = darkColor)

            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText, color = Color.White, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantOrdersScreenPreview() {
    RestaurantOrdersContent(
        pendingOrders = listOf(Order(id = 123, clienteId = 1, restauranteId = 1, valorTotal = 15.40, estado = "Pendente")),
        preparingOrders = emptyList(),
        orderDao = null,
        onNavigateHome = {},
        onNavigateProfile = {},
        onNavigateMenu = {},
        onAcceptOrder = {},
        onOrderReady = {}
    )
}