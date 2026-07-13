package com.example.grabt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Imports dos DAOs
import com.example.grabt.dao.ClientDao
import com.example.grabt.dao.DeliveryDao
import com.example.grabt.dao.OrderDao
import com.example.grabt.dao.ProductDao
import com.example.grabt.dao.RestaurantDao

// Imports dos Modelos
import com.example.grabt.model.Client
import com.example.grabt.model.Delivery
import com.example.grabt.model.Order
import com.example.grabt.model.OrderItem
import com.example.grabt.model.Product
import com.example.grabt.model.Restaurant

@Database(
    entities = [
        Client::class,
        Delivery::class,
        Restaurant::class,
        Product::class,
        Order::class,     // Adicionado para os Pedidos
        OrderItem::class  // Adicionado para os Itens do Pedido
    ],
    version = 4, // Versão 4: Atualizado com sistema de pedidos e gestão de stock no produto!
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    abstract fun deliveryDao(): DeliveryDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao // Acesso aos Pedidos

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grabit_database"
                )
                    .allowMainThreadQueries() // Permite queries síncronas de forma rápida
                    .fallbackToDestructiveMigration() // Recria a base de dados em caso de mudança de versão
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}