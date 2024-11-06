package com.example.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.database.model.OfferModelDataBase
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers")
    suspend fun getAllOffers(): List<OfferModelDataBase>

    @Query("SELECT * FROM offers")
    fun getAllOffersFlow(): Flow<List<OfferModelDataBase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<OfferModelDataBase>)
}