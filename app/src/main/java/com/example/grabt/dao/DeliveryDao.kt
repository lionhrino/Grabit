package com.example.grabt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grabt.model.Delivery
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao {
    @Insert
    fun insert(delivery: Delivery)

    @Query("SELECT * FROM table_deliveries WHERE email = :email AND password = :password")
    fun login(email: String, password: String): Delivery?

    @Query("SELECT * FROM table_deliveries WHERE email = :email LIMIT 1")
    fun checkEmail(email: String): Delivery?

    @Query("SELECT * FROM table_deliveries WHERE id = :id")
    fun getDeliveryById(id: Int): Flow<Delivery>
}