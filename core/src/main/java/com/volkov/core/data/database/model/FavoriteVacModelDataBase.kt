package com.example.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class FavoriteVacModelDataBase (
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "looking_number")
    val lookingNumber: Int? = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @Embedded(prefix = "address_")
    val address: AddressFModelDataBase,

    @ColumnInfo(name = "company")
    val company: String,

    @Embedded(prefix = "experience_")
    val experience: ExperienceFModelDataBase,

    @ColumnInfo(name = "published_date")
    val publishedDate: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean,

    @Embedded(prefix = "salary_")
    val salary: SalaryFModelDataBase,

    @ColumnInfo(name = "schedules")
    val schedules: List<String>,

    @ColumnInfo(name = "applied_number")
    val appliedNumber: Int? = 0,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "responsibilities")
    val responsibilities: String? = null,

    @ColumnInfo(name = "questions")
    val questions: List<String>? = null
)

data class AddressFModelDataBase(
    @ColumnInfo(name = "town")
    val town: String,

    @ColumnInfo(name = "street")
    val street: String,

    @ColumnInfo(name = "house")
    val house: String
)

data class ExperienceFModelDataBase(
    @ColumnInfo(name = "preview_text")
    val previewText: String,

    @ColumnInfo(name = "text")
    val text: String
)

data class SalaryFModelDataBase(
    @ColumnInfo(name = "short")
    val short: String? = null,

    @ColumnInfo(name = "full")
    val full: String
)