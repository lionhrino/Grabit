package com.example.grabt.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grabt.R // <-- Importante para o Android encontrar a tua pasta drawable!

@Composable
fun InitialScreen(navController: NavController) {
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
        verticalArrangement = Arrangement.Center
    ) {

        // A TUA IMAGEM VOLTOU AQUI!
        // Confirma se o nome do teu ficheiro é mesmo "logo". Se não for, muda ali onde diz R.drawable.logo
        Image(
            painter = painterResource(id = R.drawable.logo_grabit),
            contentDescription = "Logo GRAB!T",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "GRAB!T",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 48.sp
            ),
            color = titleColor,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        Button(
            onClick = { navController.navigate("login_screen") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            border = BorderStroke(2.dp, buttonBorderColor)
        ) {
            Text("Login", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = titleColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // VOLTOU A SER "SIGN IN"!
        Button(
            onClick = { navController.navigate("role_selection") },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            border = BorderStroke(2.dp, buttonBorderColor)
        ) {
            Text("Sign In", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = titleColor)
        }
    }
}

// O PREVIEW DO ECRÃ INICIAL ESTÁ AQUI
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InitialScreenPreview() {
    val dummyNavController = rememberNavController()
    InitialScreen(navController = dummyNavController)
}