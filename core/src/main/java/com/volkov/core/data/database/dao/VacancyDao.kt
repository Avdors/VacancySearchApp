package com.example.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.database.model.VacancyModelDataBase
import kotlinx.coroutines.flow.Flow

@Dao
interface VacancyDao {
    //дописать запрос
    @Query("SELECT * FROM vacancies WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: String): VacancyModelDataBase

    @Query("SELECT * FROM vacancies")
    suspend fun getAllVacancies(): List<VacancyModelDataBase>

    @Query("SELECT * FROM vacancies")
    fun getAllVacanciesFlow(): Flow<List<VacancyModelDataBase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancies(vacancies: List<VacancyModelDataBase>)
}