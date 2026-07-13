package com.example.grabt.view.restaurant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grabt.R
import com.example.grabt.dao.ProductDao
import com.example.grabt.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RestaurantMenuManagerScreen(
    navController: NavController,
    productDao: ProductDao,
    restaurantId: Int
) {
    val products by productDao.getProductsByRestaurant(restaurantId).collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    RestaurantMenuManagerContent(
        products = products,
        onNavigateHome = { navController.navigate("restaurant_home") { popUpTo(0) } },
        onNavigateOrders = { navController.navigate("restaurant_orders") },
        onNavigateProfile = { navController.navigate("restaurant_profile") }, // ATUALIZADO
        onAddProduct = { nome, desc, preco ->
            coroutineScope.launch(Dispatchers.IO) {
                productDao.insert(Product(restauranteId = restaurantId, nome = nome, descricao = desc, preco = preco, stock = 0))
            }
        },
        onDeleteProduct = { product ->
            coroutineScope.launch(Dispatchers.IO) { productDao.delete(product) }
        },
        onUpdateStock = { produtoId, novoStock ->
            if (novoStock >= 0) {
                coroutineScope.launch(Dispatchers.IO) { productDao.updateStock(produtoId, novoStock) }
            }
        }
    )
}

@Composable
fun RestaurantMenuManagerContent(
    products: List<Product>,
    onNavigateHome: () -> Unit,
    onNavigateOrders: () -> Unit,
    onNavigateProfile: () -> Unit, // ATUALIZADO
    onAddProduct: (String, String, Double) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onUpdateStock: (Int, Int) -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)

    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    var newNome by remember { mutableStateOf("") }
    var newDesc by remember { mutableStateOf("") }
    var newPreco by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.nome.contains(searchQuery, ignoreCase = true)
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = cardColor,
            titleContentColor = darkColor,
            textContentColor = darkColor,
            title = { Text("Adicionar Novo Prato", fontWeight = FontWeight.ExtraBold) },
            text = {
                Column {
                    OutlinedTextField(value = newNome, onValueChange = { newNome = it }, label = { Text("Nome do Prato") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkColor, unfocusedBorderColor = darkColor, focusedTextColor = darkColor, unfocusedTextColor = darkColor))
                    OutlinedTextField(value = newDesc, onValueChange = { newDesc = it }, label = { Text("Descrição Breve") }, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkColor, unfocusedBorderColor = darkColor, focusedTextColor = darkColor, unfocusedTextColor = darkColor))
                    OutlinedTextField(value = newPreco, onValueChange = { newPreco = it }, label = { Text("Preço (€)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkColor, unfocusedBorderColor = darkColor, focusedTextColor = darkColor, unfocusedTextColor = darkColor))
                }
            },
            confirmButton = {
                Button(onClick = {
                    val precoFinal = newPreco.toDoubleOrNull() ?: 0.0
                    if (newNome.isNotBlank()) {
                        onAddProduct(newNome, newDesc, precoFinal)
                        showAddDialog = false
                        newNome = ""; newDesc = ""; newPreco = ""
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = actionRed)) { Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancelar", color = darkColor, fontWeight = FontWeight.Bold) }
            }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = cardColor, modifier = Modifier.border(BorderStroke(2.dp, darkColor))) {
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Painel", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Painel", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateHome() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateOrders() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Menu", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Menu", color = darkColor, fontWeight = FontWeight.Bold) }, selected = true, onClick = { /* Já estamos aqui */ }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) }, selected = false, onClick = { onNavigateProfile() }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = darkColor, contentColor = backgroundColor, shape = RoundedCornerShape(16.dp), modifier = Modifier.border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))) { Text("+", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold) }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp)
        ) {
            item {
                Text("Gestão de Cardápio", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.padding(bottom = 16.dp))
            }

            item {
                OutlinedTextField(
                    value = searchQuery, onValueChange = { searchQuery = it },
                    placeholder = { Text("Procurar no menu...", fontWeight = FontWeight.Bold) },
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = "Pesquisar", tint = darkColor, modifier = Modifier.size(20.dp)) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = darkColor, unfocusedBorderColor = darkColor, focusedContainerColor = cardColor, unfocusedContainerColor = cardColor, focusedTextColor = darkColor, unfocusedTextColor = darkColor),
                    shape = RoundedCornerShape(50), modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (filteredProducts.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("Ainda não tens pratos ou nenhum corresponde à pesquisa.", color = darkColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            } else {
                items(filteredProducts) { product ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).background(cardColor, shape = RoundedCornerShape(16.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp)).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.nome, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
                            Text(product.descricao, fontSize = 13.sp, color = darkColor.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(String.format("%.2f €", product.preco), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = actionRed)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(32.dp).background(backgroundColor, RoundedCornerShape(8.dp)).border(2.dp, darkColor, RoundedCornerShape(8.dp)).clickable { onUpdateStock(product.id, product.stock - 1) }, contentAlignment = Alignment.Center) {
                                    Text("-", fontWeight = FontWeight.ExtraBold, color = darkColor)
                                }
                                Text(text = "${product.stock}", modifier = Modifier.width(36.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold, color = darkColor, fontSize = 16.sp)
                                Box(modifier = Modifier.size(32.dp).background(darkColor, RoundedCornerShape(8.dp)).clickable { onUpdateStock(product.id, product.stock + 1) }, contentAlignment = Alignment.Center) {
                                    Text("+", fontWeight = FontWeight.ExtraBold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            IconButton(onClick = { onDeleteProduct(product) }, modifier = Modifier.size(24.dp)) {
                                Icon(painter = painterResource(id = R.drawable.ic_trash), contentDescription = "Apagar", tint = actionRed, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantMenuManagerPreview() {
    val dummyProducts = listOf(
        Product(id = 1, restauranteId = 1, nome = "Burger Rústico", descricao = "Carne dupla com queijo cheddar.", preco = 8.50, stock = 12),
        Product(id = 2, restauranteId = 1, nome = "Batatas Supremas", descricao = "Batatas rústicas com bacon.", preco = 3.20, stock = 0)
    )
    RestaurantMenuManagerContent(products = dummyProducts, onNavigateHome = {}, onNavigateOrders = {}, onNavigateProfile = {}, onAddProduct = {_,_,_->}, onDeleteProduct = {}, onUpdateStock = {_,_->})
}