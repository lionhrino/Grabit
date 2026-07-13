package com.example.grabt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grabt.model.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Insert
    fun insert(restaurant: Restaurant)

    @Query("SELECT * FROM table_restaurants WHERE email = :email AND password = :password")
    fun login(email: String, password: String): Restaurant?

    @Query("SELECT * FROM table_restaurants")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT COUNT(*) FROM table_restaurants")
    fun getRestaurantCount(): Int

    @Query("SELECT * FROM table_restaurants WHERE email = :email LIMIT 1")
    fun checkEmail(email: String): Restaurant?

    // NOVO: Buscar o Restaurante pelo ID para o Perfil!
    @Query("SELECT * FROM table_restaurants WHERE id = :id")
    fun getRestaurantById(id: Int): Flow<Restaurant>
}