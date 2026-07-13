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
fun PromoScreen(navController: NavController) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)
    val actionRed = Color(0xFFD32F2F)

    val selectedTab = 2 // Tab das Promoções

    Scaffold(
        bottomBar = {
            // A BARRA DE NAVEGAÇÃO COM A TAB 2 SELECIONADA
            NavigationBar(containerColor = cardColor, modifier = Modifier.border(BorderStroke(2.dp, darkColor))) {
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Início", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Início", color = darkColor, fontWeight = FontWeight.Bold) }, selected = selectedTab == 0, onClick = { navController.navigate("client_home") }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Locais", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Locais", color = darkColor, fontWeight = FontWeight.Bold) }, selected = selectedTab == 1, onClick = { navController.navigate("restaurant_list") }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_promo), contentDescription = "Promo", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Promo", color = darkColor, fontWeight = FontWeight.Bold) }, selected = selectedTab == 2, onClick = { /* Já estamos aqui */ }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) }, selected = selectedTab == 3, onClick = { navController.navigate("orders_screen") }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
                NavigationBarItem(icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) }, label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) }, selected = selectedTab == 4, onClick = { navController.navigate("profile_screen") }, colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor))
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
            Text(
                text = "Ofertas Exclusivas",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
            )
            Text(
                text = "Os melhores descontos da GRAB!T",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = darkColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // LISTA DE PROMOÇÕES GIGANTES
            PromoVerticalCard(brandName = "McDonald's", promoText = "-30% EM TODOS OS MENUS", rating = "4.6", imageRes = R.drawable.img_mcd, logoRes = R.drawable.logo_mcd, cardColor = cardColor, darkColor = darkColor, actionRed = actionRed) { navController.navigate("restaurant_menu/1") }
            Spacer(modifier = Modifier.height(20.dp))
            PromoVerticalCard(brandName = "KFC", promoText = "BALDE 2x1", rating = "4.4", imageRes = R.drawable.img_kfc, logoRes = R.drawable.logo_kfc, cardColor = cardColor, darkColor = darkColor, actionRed = actionRed) { navController.navigate("restaurant_menu/2") }
            Spacer(modifier = Modifier.height(20.dp))
            PromoVerticalCard(brandName = "Pizza Hut", promoText = "TAXA DE ENTREGA GRÁTIS", rating = "4.7", imageRes = R.drawable.img_pizzahut, logoRes = R.drawable.logo_pizzahut, cardColor = cardColor, darkColor = darkColor, actionRed = actionRed) { navController.navigate("restaurant_menu/4") }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PromoVerticalCard(brandName: String, promoText: String, rating: String, imageRes: Int, logoRes: Int, cardColor: Color, darkColor: Color, actionRed: Color, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(cardColor, shape = RoundedCornerShape(24.dp))
            .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Image(painter = painterResource(id = imageRes), contentDescription = brandName, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.padding(12.dp).background(actionRed, shape = RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                Text(text = promoText, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = logoRes), contentDescription = "Logo $brandName", modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = brandName, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = darkColor)
                Text(text = "⭐ $rating • Termina hoje!", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = darkColor.copy(alpha = 0.8f))
            }
            Text("➔", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = darkColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PromoScreenPreview() { PromoScreen(rememberNavController()) }