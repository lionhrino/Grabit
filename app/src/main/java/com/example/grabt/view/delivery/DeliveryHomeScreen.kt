package com.example.grabt.view.delivery

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
import com.example.grabt.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DeliveryHomeScreen(navController: NavController, orderDao: OrderDao, deliveryId: Int) {
    val currentOrder by orderDao.getCurrentOrderForDelivery(deliveryId).collectAsState(initial = null)
    val availableOrders by orderDao.getAvailableOrdersForDelivery().collectAsState(initial = emptyList())

    val coroutineScope = rememberCoroutineScope()

    DeliveryHomeContent(
        currentOrder = currentOrder,
        availableOrders = availableOrders,
        onNavigateProfile = { navController.navigate("delivery_profile") },
        onAcceptOrder = { orderId ->
            coroutineScope.launch(Dispatchers.IO) { orderDao.acceptDelivery(orderId, deliveryId) }
        },
        onCompleteOrder = { orderId ->
            coroutineScope.launch(Dispatchers.IO) { orderDao.updateOrderStatus(orderId, "Entregue") }
        }
    )
}

@Composable
fun DeliveryHomeContent(
    currentOrder: Order?,
    availableOrders: List<Order>,
    onNavigateProfile: () -> Unit,
    onAcceptOrder: (Int) -> Unit,
    onCompleteOrder: (Int) -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)
    val successGreen = Color(0xFF388E3C)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cardColor, modifier = Modifier.border(BorderStroke(2.dp, darkColor))) {
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Painel", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Entregas", color = darkColor, fontWeight = FontWeight.Bold) }, selected = true, onClick = { /* Já estamos aqui */ }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateProfile() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
            }
        }
    ) { innerPadding -> LazyColumn(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp)
        ) {
            item {
                //Esta parte é a central de estafetas aqui em cima
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Central Estafetas", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
                    Image(painter = painterResource(id = R.drawable.logo_grabit), contentDescription = "Logo", modifier = Modifier.size(48.dp))
                }
            }
                //Atividade em curso
            if (currentOrder != null) {
                item { Text("Entrega em Curso", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = actionRed, modifier = Modifier.padding(bottom = 16.dp)) }
                item {
                    Column(modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(24.dp)) {
                        Text(text = "Pedido #${currentOrder.id}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = darkColor, modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = "Restaurante: ID ${currentOrder.restauranteId}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = darkColor.copy(alpha = 0.8f), modifier = Modifier.padding(bottom = 4.dp))
                        Text(text = "Cliente: ID ${currentOrder.clienteId}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = darkColor.copy(alpha = 0.8f), modifier = Modifier.padding(bottom = 24.dp))

                        Button(onClick = { onCompleteOrder(currentOrder.id) }, colors = ButtonDefaults.buttonColors(containerColor = successGreen), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(60.dp).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))) {
                            Text("ENTREGA CONCLUÍDA", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                    }
                }
            } else {
                item { Text("A Aguardar Estafeta (${availableOrders.size})", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 16.dp)) }
                //Mensagem de que aparece quando não temos pedidos
                if (availableOrders.isEmpty()) {
                    item { Text("Não há entregas disponíveis de momento.", color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold) }
                } else {
                    items(availableOrders) { order ->
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).background(cardColor, shape = RoundedCornerShape(16.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "Pedido #${order.id}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = darkColor)
                                Text(text = "Restaurante ID: ${order.restauranteId}", fontSize = 14.sp, color = darkColor.copy(alpha = 0.8f))
                            }
                            Button(onClick = { onAcceptOrder(order.id) }, colors = ButtonDefaults.buttonColors(containerColor = darkColor), shape = RoundedCornerShape(12.dp)) {
                                Text("ACEITAR", color = backgroundColor, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeliveryHomeScreenWaitingPreview() {
    // PREVIEW: Estafeta livre, à espera que caiam pedidos dos restaurantes
    DeliveryHomeContent(
        currentOrder = null,
        availableOrders = listOf(
            Order(id = 1, clienteId = 3, restauranteId = 1, valorTotal = 14.50, estado = "A Aguardar Estafeta"),
            Order(id = 2, clienteId = 5, restauranteId = 3, valorTotal = 22.80, estado = "A Aguardar Estafeta")
        ),
        onNavigateProfile = {},
        onAcceptOrder = {},
        onCompleteOrder = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeliveryHomeScreenActivePreview() {
    // PREVIEW: Estafeta ocupado, com uma entrega a decorrer na mala
    DeliveryHomeContent(
        currentOrder = Order(id = 42, clienteId = 2, restauranteId = 1, valorTotal = 18.90, estado = "Em Caminho"),
        availableOrders = emptyList(),
        onNavigateProfile = {},
        onAcceptOrder = {},
        onCompleteOrder = {}
    )
}