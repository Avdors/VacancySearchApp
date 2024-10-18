package com.example.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.core.data.database.model.FavoriteVacModelDataBase
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Upsert
    suspend fun upsertItem(item: FavoriteVacModelDataBase)

    @Query("SELECT * FROM favorites WHERE id = :vacancyId")
    suspend fun getVacancyByIdfromFavorite(vacancyId: String): FavoriteVacModelDataBase
    @Query("SELECT * FROM favorites")
    fun getItemList(): Flow<List<FavoriteVacModelDataBase>>

    @Delete
    suspend fun delete(item: FavoriteVacModelDataBase)
}