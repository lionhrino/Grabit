package com.example.grabt.view.client

import android.widget.Toast
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grabt.dao.ClientDao
import com.example.grabt.dao.OrderDao
import com.example.grabt.model.Order
import com.example.grabt.model.OrderItem
import com.example.grabt.viewmodel.CartItem
import com.example.grabt.viewmodel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    clientDao: ClientDao,
    orderDao: OrderDao, // NOVO DAO INJETADO
    clientId: Int
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()
    val subtotal = cartViewModel.getSubtotal()
    val client by clientDao.getClientById(clientId).collectAsState(initial = null)

    val coroutineScope = rememberCoroutineScope() // PARA O LAUNCH DA COROUTINE

    CartScreenContent(
        cartItems = cartItems,
        subtotal = subtotal,
        saldoDisponivel = client?.saldo ?: 0.0,
        onBackClick = { navController.popBackStack() },
        onRemoveItem = { id -> cartViewModel.removeItem(id) },
        onCheckout = { total ->
            if (client != null && cartItems.isNotEmpty()) {
                if (client!!.saldo >= total) {

                    // A MÁGICA DA BASE DE DADOS ACONTECE AQUI!
                    coroutineScope.launch(Dispatchers.IO) {
                        // 1. Desconta o saldo da carteira do cliente
                        clientDao.addSaldo(clientId, -total)

                        // 2. Descobre o Restaurante (através do primeiro item do carrinho)
                        val idRestaurante = cartItems.first().restauranteId

                        // 3. Cria a Encomenda Geral (Order)
                        val novaEncomenda = Order(
                            clienteId = clientId,
                            restauranteId = idRestaurante,
                            valorTotal = total,
                            estado = "Pendente"
                        )
                        // Guarda e recebe o ID (Cod_Pedido) gerado!
                        val idDaEncomenda = orderDao.insertOrder(novaEncomenda).toInt()

                        // 4. Grava cada produto associado a esta Encomenda (OrderItem)
                        cartItems.forEach { item ->
                            val novoItem = OrderItem(
                                pedidoId = idDaEncomenda,
                                produtoId = item.id,
                                nomeProduto = item.nome,
                                quantidade = item.quantidade,
                                precoNaHora = item.precoUnitario
                            )
                            orderDao.insertOrderItem(novoItem)
                        }

                        // 5. Volta à interface principal e dá a mensagem de sucesso
                        withContext(Dispatchers.Main) {
                            cartViewModel.clearCart()
                            Toast.makeText(context, "Pagamento aprovado! O restaurante já recebeu o pedido.", Toast.LENGTH_LONG).show()
                            navController.navigate("client_home") { popUpTo(0) }
                        }
                    }

                } else {
                    Toast.makeText(context, "Saldo insuficiente! Vai ao Perfil carregar a tua carteira.", Toast.LENGTH_LONG).show()
                }
            } else if (cartItems.isEmpty()) {
                Toast.makeText(context, "O teu carrinho está vazio!", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Composable
fun CartScreenContent(
    cartItems: List<CartItem>,
    subtotal: Double,
    saldoDisponivel: Double,
    onBackClick: () -> Unit,
    onRemoveItem: (Int) -> Unit,
    onCheckout: (Double) -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)

    val taxaEntrega = if (cartItems.isEmpty()) 0.00 else 1.90
    val total = subtotal + taxaEntrega

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(24.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal", fontWeight = FontWeight.Bold, color = darkColor, fontSize = 16.sp)
                    Text(String.format("%.2f €", subtotal), fontWeight = FontWeight.Bold, color = darkColor, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Taxa de Entrega", fontWeight = FontWeight.Bold, color = darkColor, fontSize = 16.sp)
                    Text(String.format("%.2f €", taxaEntrega), fontWeight = FontWeight.Bold, color = darkColor, fontSize = 16.sp)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp), color = darkColor, thickness = 2.dp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total a Pagar", fontWeight = FontWeight.ExtraBold, color = darkColor, fontSize = 20.sp)
                    Text(String.format("%.2f €", total), fontWeight = FontWeight.ExtraBold, color = actionRed, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Saldo na Carteira: ${String.format("%.2f €", saldoDisponivel)}",
                    fontWeight = FontWeight.Medium,
                    color = darkColor.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.End)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onCheckout(total) },
                    colors = ButtonDefaults.buttonColors(containerColor = actionRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(60.dp).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))
                ) {
                    Text("FINALIZAR PEDIDO", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp).background(cardColor, shape = RoundedCornerShape(12.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
                ) {
                    Text("<", fontWeight = FontWeight.Bold, color = darkColor, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "O Teu Pedido", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
            }

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("O teu carrinho está vazio...", fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            item = item,
                            cardColor = cardColor,
                            darkColor = darkColor,
                            onRemove = { onRemoveItem(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, cardColor: Color, darkColor: Color, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(Color(0xFFEBE4CF), shape = RoundedCornerShape(10.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${item.quantidade}x", fontWeight = FontWeight.ExtraBold, color = darkColor, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.nome, fontWeight = FontWeight.ExtraBold, color = darkColor, fontSize = 16.sp)
            Text(text = String.format("%.2f €", item.precoUnitario * item.quantidade), fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.7f), fontSize = 14.sp)
        }

        IconButton(onClick = onRemove) {
            Text("X", fontWeight = FontWeight.ExtraBold, color = Color(0xFFD32F2F), fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    val mockItems = listOf(
        CartItem(id = 1, restauranteId = 1, nome = "Menu Big Mac", precoUnitario = 7.50, quantidade = 2),
        CartItem(id = 2, restauranteId = 1, nome = "Batatas Fritas", precoUnitario = 2.80, quantidade = 1)
    )
    CartScreenContent(
        cartItems = mockItems,
        subtotal = 17.80,
        saldoDisponivel = 25.00,
        onBackClick = {},
        onRemoveItem = {},
        onCheckout = {}
    )
}