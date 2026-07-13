package com.example.grabt.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RoleSelectionScreen(navController: NavController) {
    val backgroundColor = Color(0xFFEBE4CF)
    val titleColor = Color(0xFF262630)
    val buttonColor = Color(0xFFDBCB9E)
    val buttonBorderColor = Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Centra tudo verticalmente
    ) {

        // Título
        Text(
            text = "Quem és tu?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            ),
            color = titleColor,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        // 1. BOTÃO CLIENTE
        Button(
            onClick = { navController.navigate("client_sign_in") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            border = BorderStroke(2.dp, buttonBorderColor)
        ) {
            Text("Cliente", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = titleColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. BOTÃO ESTAFETA
        Button(
            onClick = { navController.navigate("delivery_sign_in") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            border = BorderStroke(2.dp, buttonBorderColor)
        ) {
            Text("Estafeta", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = titleColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. BOTÃO RESTAURANTE (Agora a funcionar e a navegar para o ecrã certo!)
        Button(
            onClick = { navController.navigate("restaurant_sign_in") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            border = BorderStroke(2.dp, buttonBorderColor)
        ) {
            Text("Restaurante", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = titleColor)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // BOTÃO DE VOLTAR (Intacto, com a tua formatação!)
        TextButton(onClick = { navController.popBackStack() }) {
            Text(
                text = "Voltar -->",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RoleSelectionPreview() {
    val dummyNavController = rememberNavController()
    RoleSelectionScreen(navController = dummyNavController)
}