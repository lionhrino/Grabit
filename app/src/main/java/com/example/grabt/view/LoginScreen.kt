package com.example.grabt.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.grabt.dao.ClientDao
import com.example.grabt.dao.DeliveryDao
import com.example.grabt.dao.RestaurantDao
import com.example.grabt.UserSession

@Composable
fun LoginScreen(
    navController: NavController,
    clientDao: ClientDao?,
    deliveryDao: DeliveryDao?,
    restaurantDao: RestaurantDao?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor = Color(0xFFEBE4CF)
    val titleColor = Color(0xFF262630)
    val buttonColor = Color(0xFFDBCB9E)
    val buttonBorderColor = Color.Black

    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Iniciar Sessão", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = titleColor, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = titleColor, unfocusedBorderColor = titleColor, focusedTextColor = titleColor, unfocusedTextColor = titleColor)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = titleColor, unfocusedBorderColor = titleColor, focusedTextColor = titleColor, unfocusedTextColor = titleColor)
        )

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    coroutineScope.launch(Dispatchers.IO) {

                        // 1. Tenta Cliente
                        val isClient = clientDao?.login(email, password)
                        if (isClient != null) {
                            UserSession.currentClientId = isClient.id
                            withContext(Dispatchers.Main) {
                                navController.navigate("client_home") { popUpTo("initial_screen") { inclusive = false } }
                            }
                            return@launch
                        }

                        // 2. Tenta Estafeta
                        val isDelivery = deliveryDao?.login(email, password)
                        if (isDelivery != null) {
                            UserSession.currentDeliveryId = isDelivery.id // GRAVA O ID!
                            withContext(Dispatchers.Main) {
                                navController.navigate("delivery_home") { popUpTo("initial_screen") { inclusive = false } }
                            }
                            return@launch
                        }

                        // 3. Tenta Restaurante
                        val isRestaurant = restaurantDao?.login(email, password)
                        if (isRestaurant != null) {
                            UserSession.currentRestaurantId = isRestaurant.id
                            withContext(Dispatchers.Main) {
                                navController.navigate("restaurant_home") { popUpTo("initial_screen") { inclusive = false } }
                            }
                            return@launch
                        }

                        withContext(Dispatchers.Main) { errorMessage = "Email ou password incorretos!" }
                    }
                } else {
                    errorMessage = "Preenche o email e a password."
                }
            },
            modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor), border = BorderStroke(2.dp, buttonBorderColor)
        ) { Text("Entrar", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = titleColor) }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = "<-- Voltar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = titleColor)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() { LoginScreen(navController = rememberNavController(), clientDao = null, deliveryDao = null, restaurantDao = null) }