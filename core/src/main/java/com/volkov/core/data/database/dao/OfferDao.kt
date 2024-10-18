package com.example.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.database.model.OfferModelDataBase

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers")
    suspend fun getAllOffers(): List<OfferModelDataBase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<OfferModelDataBase>)
}