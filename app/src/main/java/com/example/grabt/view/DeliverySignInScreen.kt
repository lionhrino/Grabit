package com.example.grabt.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.example.grabt.model.Delivery
import com.example.grabt.dao.ClientDao
import com.example.grabt.dao.DeliveryDao
import com.example.grabt.dao.RestaurantDao

@Composable
fun DeliverySignInScreen(
    clientDao: ClientDao?,
    deliveryDao: DeliveryDao?,
    restaurantDao: RestaurantDao?,
    onNavigateBack: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    var errorMessage by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var vehicle by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val backgroundColor = Color(0xFFEBE4CF)
    val titleColor = Color(0xFF262630)
    val buttonColor = Color(0xFFDBCB9E)
    val buttonBorderColor = Color.Black

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = titleColor, unfocusedBorderColor = titleColor.copy(alpha = 0.5f),
        focusedTextColor = titleColor, unfocusedTextColor = titleColor, cursorColor = titleColor,
        focusedLabelColor = titleColor, unfocusedLabelColor = titleColor.copy(alpha = 0.7f)
    )

    Column(
        modifier = Modifier.fillMaxSize().background(backgroundColor).padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registo Estafeta", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = titleColor, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Text(text = if (currentStep == 1) "Passo 1 de 2: Dados Pessoais" else "Passo 2 de 2: Detalhes de Trabalho", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = titleColor.copy(alpha = 0.7f), modifier = Modifier.padding(bottom = 32.dp))

        if (currentStep == 1) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome Completo") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), colors = textFieldColors, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), colors = textFieldColors, modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            // BLOQUEIO ANTI-DUPLICADOS!
                            val existsClient = clientDao?.checkEmail(email) != null
                            val existsDelivery = deliveryDao?.checkEmail(email) != null
                            val existsRestaurant = restaurantDao?.checkEmail(email) != null

                            withContext(Dispatchers.Main) {
                                if (existsClient || existsDelivery || existsRestaurant) {
                                    errorMessage = "Este email já está registado na GRAB!T!"
                                } else {
                                    errorMessage = ""
                                    currentStep = 2
                                }
                            }
                        }
                    } else { errorMessage = "Falta preencher alguns campos!" }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor), border = BorderStroke(2.dp, buttonBorderColor)
            ) { Text("Seguinte", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = titleColor) }
        }

        if (currentStep == 2) {
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Telemóvel") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), colors = textFieldColors, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            OutlinedTextField(value = vehicle, onValueChange = { vehicle = it }, label = { Text("Veículo (Ex: Mota, Bicicleta)") }, colors = textFieldColors, modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp))

            Button(
                onClick = {
                    if (phone.isNotBlank() && vehicle.isNotBlank()) {
                        errorMessage = ""
                        coroutineScope.launch(Dispatchers.IO) {
                            val newDelivery = Delivery(name = name, email = email, password = password, phone = phone, vehicle = vehicle)
                            deliveryDao?.insert(newDelivery)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Estafeta registado com sucesso!", Toast.LENGTH_LONG).show()
                                onNavigateBack()
                            }
                        }
                    } else { errorMessage = "Falta preencher Telemóvel e Veículo!" }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor), border = BorderStroke(2.dp, buttonBorderColor)
            ) { Text("Finalizar Registo", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = titleColor) }
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        }

        Spacer(modifier = Modifier.height(48.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            TextButton(onClick = {
                if (currentStep == 2) { currentStep = 1; errorMessage = "" } else { onNavigateBack() }
            }) { Text(text = "<-- Voltar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = titleColor) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeliverySignInScreenPreview() {
    DeliverySignInScreen(
        clientDao = null,
        deliveryDao = null,
        restaurantDao = null,
        onNavigateBack = {}
    )
}