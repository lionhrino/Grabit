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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grabt.R
import com.example.grabt.dao.ProductDao
import com.example.grabt.model.Product
import com.example.grabt.viewmodel.CartViewModel

@Composable
fun RestaurantMenuScreen(
    navController: NavController,
    restaurantId: Int,
    cartViewModel: CartViewModel,
    productDao: ProductDao
) {
    val products by productDao.getProductsByRestaurant(restaurantId).collectAsState(initial = emptyList())
    val cartItems by cartViewModel.cartItems.collectAsState()

    // CORREÇÃO: O subtotal agora escuta a mudança de quantidade do LiveData ao vivo!
    val subtotal = cartItems.sumOf { it.precoUnitario * it.quantidade }

    val restaurantName = when (restaurantId) {
        1 -> "McDonald's"
        2 -> "KFC"
        3 -> "Burger King"
        4 -> "Pizza Hut"
        else -> "Restaurante GRAB!T"
    }

    RestaurantMenuContent(
        restaurantName = restaurantName,
        products = products,
        subtotal = subtotal,
        onBackClick = { navController.popBackStack() },
        onViewCartClick = { navController.navigate("cart_screen") },
        onAddProductClick = { product ->
            cartViewModel.addItem(product.id, product.restauranteId, product.nome, product.preco)
        }
    )
}

@Composable
fun RestaurantMenuContent(
    restaurantName: String,
    products: List<Product>,
    subtotal: Double,
    onBackClick: () -> Unit,
    onViewCartClick: () -> Unit,
    onAddProductClick: (Product) -> Unit
) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onViewCartClick,
                    colors = ButtonDefaults.buttonColors(containerColor = actionRed),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(60.dp).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("VER CARRINHO", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Box(
                            modifier = Modifier.background(darkColor, shape = RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(String.format("%.2f €", subtotal), color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(innerPadding).padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp).background(cardColor, shape = RoundedCornerShape(12.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
                ) {
                    Text("<", fontWeight = FontWeight.Bold, color = darkColor, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Menu", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
            }

            Row(
                modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = restaurantName, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
                    Text(text = "⭐ 4.6 • Aberto até às 00:00", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.7f))
                }

                Image(
                    painter = painterResource(id = R.drawable.promo_food),
                    contentDescription = "Capa do Restaurante",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
                )
            }

            Text(
                text = "Produtos Disponíveis",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor,
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
            )

            if (products.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "Este restaurante está sem stock de momento...", fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.6f), fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products) { product ->
                        ProductRowCard(
                            product = product,
                            cardColor = cardColor,
                            darkColor = darkColor,
                            actionRed = actionRed,
                            onAddClick = { onAddProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductRowCard(product: Product, cardColor: Color, darkColor: Color, actionRed: Color, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(cardColor, shape = RoundedCornerShape(20.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.promo_food),
            contentDescription = "Imagem do Produto",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.nome, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
            Text(text = product.descricao, fontSize = 11.sp, color = darkColor.copy(alpha = 0.8f), maxLines = 2, modifier = Modifier.padding(vertical = 4.dp))
            Text(text = String.format("%.2f €", product.preco), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = actionRed)
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onAddClick,
            modifier = Modifier.size(40.dp).background(actionRed, shape = RoundedCornerShape(12.dp)).border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
        ) {
            Text("+", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RestaurantMenuScreenPreview() {
    val dummyProducts = listOf(
        Product(id = 1, restauranteId = 1, nome = "Menu Big Mac", descricao = "Hambúrguer com carne real, alface e queijo.", preco = 7.50, stock = 10)
    )
    RestaurantMenuContent(restaurantName = "McDonald's", products = dummyProducts, subtotal = 7.50, onBackClick = {}, onViewCartClick = {}, onAddProductClick = {})
}