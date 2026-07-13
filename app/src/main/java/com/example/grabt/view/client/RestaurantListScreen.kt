package com.example.grabt.view.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.navigation.compose.rememberNavController
import com.example.grabt.R
import com.example.grabt.model.Restaurant

@Composable
fun RestaurantListScreen(navController: NavController, restaurantList: List<Restaurant>) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)

    var searchQuery by remember { mutableStateOf("") }

    // Filtrar a lista dinamicamente com base no que o utilizador escreve
    val filteredRestaurants = restaurantList.filter {
        it.nome.contains(searchQuery, ignoreCase = true)
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {

            // --- TOPO COM BOTÃO VOLTAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(cardColor, shape = RoundedCornerShape(12.dp))
                        .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
                ) {
                    Text("<", fontWeight = FontWeight.Bold, color = darkColor, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Explorar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = darkColor
                )
            }

            // --- BARRA DE PESQUISA FILTRANTE ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Pesquisar restaurantes...", fontWeight = FontWeight.Bold) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(56.dp)
            )

            // --- LISTA DINÂMICA DE RESTAURANTES ---
            if (filteredRestaurants.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum restaurante encontrado...", fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredRestaurants) { restaurant ->
                        RestaurantRowCard(
                            restaurant = restaurant,
                            cardColor = cardColor,
                            darkColor = darkColor,
                            onClick = {
                                navController.navigate("restaurant_menu/${restaurant.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantRowCard(
    restaurant: Restaurant,
    cardColor: Color,
    darkColor: Color,
    onClick: () -> Unit
) {
    val logoResource = when (restaurant.nome.lowercase()) {
        "mcdonald's", "mcdonalds" -> R.drawable.logo_mcd
        "kfc" -> R.drawable.logo_kfc
        "burger king" -> R.drawable.logo_bk
        "pizza hut" -> R.drawable.logo_pizzahut
        else -> R.drawable.logo_grabit
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardColor, shape = RoundedCornerShape(20.dp))
            .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = logoResource),
            contentDescription = "Logo ${restaurant.nome}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = restaurant.nome,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor
            )
            Text(
                text = "${restaurant.concelho}, ${restaurant.distrito}",
                fontSize = 13.sp,
                color = darkColor.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = "➔",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = darkColor,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

// --- PREVIEW AGORA FUNCIONA A 100% SEM ERROS DE DAO ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RestaurantListScreenPreview() {
    val dummyNavController = rememberNavController()
    val dummyList = listOf(
        Restaurant(id = 1, nome = "McDonald's", email = "", password = "", rua = "", codigoPostal = "", concelho = "Beja", distrito = "Beja"),
        Restaurant(id = 2, nome = "KFC", email = "", password = "", rua = "", codigoPostal = "", concelho = "Beja", distrito = "Beja"),
        Restaurant(id = 3, nome = "Burger King", email = "", password = "", rua = "", codigoPostal = "", concelho = "Beja", distrito = "Beja")
    )
    RestaurantListScreen(navController = dummyNavController, restaurantList = dummyList)
}