package com.example.grabt.view.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.compose.rememberNavController
import com.example.grabt.R

@Composable
fun ClientHomeScreen(navController: NavController) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)

    var searchQueries by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = cardColor,
                modifier = Modifier.border(BorderStroke(2.dp, darkColor))
            ) {
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Início", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Início", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Restaurantes", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Locais", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("restaurant_list")
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_promo), contentDescription = "Promoções", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Promo", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("promo_screen")
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                // É AQUI QUE ESTAVA O ERRO! AGORA JÁ TEM O NAVIGATE.
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        navController.navigate("orders_screen")
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 4,
                    onClick = {
                        selectedTab = 4
                        navController.navigate("profile_screen")
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("cart_screen") },
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))
            ) {
                Text("🛒", fontSize = 20.sp)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    OutlinedTextField(
                        value = searchQueries,
                        onValueChange = { searchQueries = it },
                        readOnly = true,
                        placeholder = { Text("Search for anything...", fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "Ícone de Pesquisa",
                                tint = darkColor,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = darkColor,
                            unfocusedBorderColor = darkColor,
                            focusedContainerColor = cardColor,
                            unfocusedContainerColor = cardColor,
                            focusedTextColor = darkColor,
                            unfocusedTextColor = darkColor
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { navController.navigate("restaurant_list") }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_grabit),
                    contentDescription = "Logo GRAB!T",
                    modifier = Modifier.size(60.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CategoryButton(emoji = "🍔", title = "Burgers", cardColor = cardColor, darkColor = darkColor, onClick = { navController.navigate("restaurant_list") })
                CategoryButton(emoji = "🍕", title = "Pizzas", cardColor = cardColor, darkColor = darkColor, onClick = { navController.navigate("restaurant_list") })
                CategoryButton(emoji = "🍣", title = "Sushi", cardColor = cardColor, darkColor = darkColor, onClick = { navController.navigate("restaurant_list") })
                CategoryButton(emoji = "🥗", title = "Healthy", cardColor = cardColor, darkColor = darkColor, onClick = { navController.navigate("restaurant_list") })
            }

            Text(
                text = "Special offer",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.promo_food),
                    contentDescription = "Promoção Especial",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(text = "ENTREGA GRÁTIS", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Em todos os pedidos até à meia-noite", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Today's deal",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DealCard(
                    brandName = "McDonald's", promoText = "-30% HOJE", rating = "4.6",
                    imageRes = R.drawable.img_mcd, logoRes = R.drawable.logo_mcd,
                    cardColor = cardColor, darkColor = darkColor, modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("restaurant_menu/1") }
                )
                DealCard(
                    brandName = "KFC", promoText = "2x1 FRANGO", rating = "4.4",
                    imageRes = R.drawable.img_kfc, logoRes = R.drawable.logo_kfc,
                    cardColor = cardColor, darkColor = darkColor, modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("restaurant_menu/2") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DealCard(
                    brandName = "Burger King", promoText = "ENTREGA GRÁTIS", rating = "4.5",
                    imageRes = R.drawable.img_bk, logoRes = R.drawable.logo_bk,
                    cardColor = cardColor, darkColor = darkColor, modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("restaurant_menu/3") }
                )
                DealCard(
                    brandName = "Pizza Hut", promoText = "MENU 10€", rating = "4.7",
                    imageRes = R.drawable.img_pizzahut, logoRes = R.drawable.logo_pizzahut,
                    cardColor = cardColor, darkColor = darkColor, modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("restaurant_menu/4") }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CategoryButton(emoji: String, title: String, cardColor: Color, darkColor: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(cardColor, shape = RoundedCornerShape(16.dp))
                .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = darkColor, maxLines = 1)
    }
}

@Composable
fun DealCard(
    brandName: String,
    promoText: String,
    rating: String,
    imageRes: Int,
    logoRes: Int,
    cardColor: Color,
    darkColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(180.dp)
            .background(cardColor, shape = RoundedCornerShape(24.dp))
            .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = brandName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(0xFFD32F2F), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = promoText, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Logo $brandName",
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = brandName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = darkColor)
                Text(text = "⭐ $rating", fontSize = 12.sp, color = darkColor.copy(alpha = 0.8f))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClientHomeScreenPreview() {
    val dummyNavController = rememberNavController()
    ClientHomeScreen(navController = dummyNavController)
}