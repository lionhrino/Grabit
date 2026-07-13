package com.example.grabt

object UserSession {
    var currentClientId: Int = 1
    var currentRestaurantId: Int = -1
    var currentDeliveryId: Int = -1 // NOVO: Guarda o ID do Estafeta!
}