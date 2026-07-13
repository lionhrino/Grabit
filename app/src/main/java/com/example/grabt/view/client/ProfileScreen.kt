package com.example.grabt.view.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grabt.R
import com.example.grabt.dao.ClientDao
import com.example.grabt.model.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, clientDao: ClientDao, clientId: Int) {
    val backgroundColor = Color(0xFFEBE4CF)
    val darkColor = Color(0xFF262630)
    val cardColor = Color(0xFFDBCB9E)

    val selectedTab = 4

    val client by clientDao.getClientById(clientId).collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

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
                    onClick = { navController.navigate("client_home") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                // ÍCONE ATUALIZADO PARA O TEU ic_food!
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_food), contentDescription = "Restaurantes", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Locais", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 1,
                    onClick = { navController.navigate("restaurant_list") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_promo), contentDescription = "Promoções", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Promo", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 2,
                    onClick = { navController.navigate("restaurant_list") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_delivery), contentDescription = "Pedidos", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Pedidos", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 3,
                    onClick = { /* Navegação futura */ },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil", tint = darkColor, modifier = Modifier.size(24.dp)) },
                    label = { Text("Perfil", color = darkColor, fontWeight = FontWeight.Bold) },
                    selected = selectedTab == 4,
                    onClick = { /* Já se encontra nesta tela */ },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = backgroundColor)
                )
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
                text = "O Teu Perfil",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor,
                modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor, shape = RoundedCornerShape(20.dp))
                    .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(backgroundColor, shape = CircleShape)
                        .border(BorderStroke(2.dp, darkColor), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "Avatar",
                        tint = darkColor,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = client?.nome ?: "A carregar...", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = darkColor)
                    Text(text = client?.email ?: "...", fontSize = 14.sp, color = darkColor.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Carteira GRAB!T",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor, shape = RoundedCornerShape(20.dp))
                    .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(20.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Saldo Disponível", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = darkColor.copy(alpha = 0.8f))
                Text(
                    text = String.format("%.2f €", client?.saldo ?: 0.0),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = darkColor,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AddBalanceBtn("+ 5€", darkColor, backgroundColor) {
                        coroutineScope.launch { clientDao.addSaldo(clientId, 5.0) }
                    }
                    AddBalanceBtn("+ 10€", darkColor, backgroundColor) {
                        coroutineScope.launch { clientDao.addSaldo(clientId, 10.0) }
                    }
                    AddBalanceBtn("+ 20€", darkColor, backgroundColor) {
                        coroutineScope.launch { clientDao.addSaldo(clientId, 20.0) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProfileOptionRow(iconRes = R.drawable.ic_delivery, title = "Histórico de Pedidos", darkColor = darkColor, cardColor = cardColor)
            // A OPÇÃO MÉTODOS DE PAGAMENTO TAMBÉM USA O TEU NOVO ÍCONE SE QUISERES (aqui deixei o ic_food)
            ProfileOptionRow(iconRes = R.drawable.ic_food, title = "Métodos de Pagamento", darkColor = darkColor, cardColor = cardColor)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("initial_screen") { popUpTo(0) } },
                colors = ButtonDefaults.buttonColors(containerColor = darkColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))
            ) {
                Text("TERMINAR SESSÃO", color = backgroundColor, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AddBalanceBtn(text: String, darkColor: Color, textColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(darkColor, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
    }
}

@Composable
fun ProfileOptionRow(iconRes: Int, title: String, darkColor: Color, cardColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(cardColor, shape = RoundedCornerShape(16.dp))
            .border(BorderStroke(2.dp, darkColor), shape = RoundedCornerShape(16.dp))
            .clickable { /* Ação futura */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = iconRes), contentDescription = null, tint = darkColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = darkColor, modifier = Modifier.weight(1f))
        Text("➔", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = darkColor)
    }
}

