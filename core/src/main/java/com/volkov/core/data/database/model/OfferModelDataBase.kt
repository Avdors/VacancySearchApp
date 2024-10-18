package com.example.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class OfferModelDataBase(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "link")
    val link: String,
    @Embedded(prefix = "button_")
    val button: ButtonModDataBase? = null // button присутствует не во всех объектах offers
)

data class ButtonModDataBase(

        @ColumnInfo(name = "text")
        val text: String
)
