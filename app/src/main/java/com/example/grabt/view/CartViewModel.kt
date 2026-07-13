package com.example.grabt.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ADICIONÁMOS O restauranteId AQUI!
data class CartItem(val id: Int, val restauranteId: Int, val nome: String, val precoUnitario: Double, val quantidade: Int = 1)

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // A FUNÇÃO AGORA RECEBE O restauranteId
    fun addItem(id: Int, restauranteId: Int, nome: String, preco: Double) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }

        if (index != -1) {
            val itemAtual = currentList[index]
            currentList[index] = itemAtual.copy(quantidade = itemAtual.quantidade + 1)
        } else {
            currentList.add(CartItem(id, restauranteId, nome, preco)) // Passamos o ID aqui!
        }
        _cartItems.value = currentList
    }

    fun removeItem(productId: Int) {
        _cartItems.value = _cartItems.value.filter { it.id != productId }
    }

    fun getSubtotal(): Double {
        return _cartItems.value.sumOf { it.precoUnitario * it.quantidade }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}