package com.example.grabt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grabt.model.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert
    fun insert(client: Client)

    @Query("SELECT * FROM table_clients WHERE email = :email AND password = :password")
    fun login(email: String, password: String): Client?

    @Query("SELECT * FROM table_clients WHERE id = :id")
    fun getClientById(id: Int): Flow<Client>

    @Query("UPDATE table_clients SET saldo = saldo + :valor WHERE id = :id")
    fun addSaldo(id: Int, valor: Double)

    // NOVO: Verifica se o email já existe na tabela de Clientes
    @Query("SELECT * FROM table_clients WHERE email = :email LIMIT 1")
    fun checkEmail(email: String): Client?
}