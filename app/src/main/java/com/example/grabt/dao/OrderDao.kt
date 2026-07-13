package com.example.grabt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.grabt.model.Order
import com.example.grabt.model.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    fun insertOrder(order: Order): Long

    @Insert
    fun insertOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM table_orders WHERE restauranteId = :restauranteId AND estado = :estado")
    fun getOrdersForRestaurantByStatus(restauranteId: Int, estado: String): Flow<List<Order>>

    @Query("SELECT * FROM table_order_items WHERE pedidoId = :pedidoId")
    fun getItemsForOrder(pedidoId: Int): Flow<List<OrderItem>>

    @Query("UPDATE table_orders SET estado = :novoEstado WHERE id = :pedidoId")
    fun updateOrderStatus(pedidoId: Int, novoEstado: String)

    @Query("SELECT SUM(valorTotal) FROM table_orders WHERE restauranteId = :restauranteId AND estado = 'Entregue'")
    fun getGanhosHoje(restauranteId: Int): Flow<Double?>

    // --- QUERIES DO ESTAFETA ---
    @Query("SELECT * FROM table_orders WHERE estado = 'A Aguardar Estafeta'")
    fun getAvailableOrdersForDelivery(): Flow<List<Order>>

    @Query("SELECT * FROM table_orders WHERE estafetaId = :estafetaId AND estado = 'Em Caminho' LIMIT 1")
    fun getCurrentOrderForDelivery(estafetaId: Int): Flow<Order?>

    @Query("UPDATE table_orders SET estafetaId = :estafetaId, estado = 'Em Caminho' WHERE id = :pedidoId")
    fun acceptDelivery(pedidoId: Int, estafetaId: Int)

    @Query("SELECT COUNT(*) * 1.90 FROM table_orders WHERE estafetaId = :estafetaId AND estado = 'Entregue'")
    fun getDeliveryEarnings(estafetaId: Int): Flow<Double?>

    // NOVO: O Cliente precisa de ler o seu próprio histórico ordenado pelo mais recente!
    @Query("SELECT * FROM table_orders WHERE clienteId = :clienteId ORDER BY id DESC")
    fun getOrdersForClient(clienteId: Int): Flow<List<Order>>
}