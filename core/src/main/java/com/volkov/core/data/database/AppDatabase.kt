package com.example.core.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.data.database.dao.FavoritesDao
import com.example.core.data.database.dao.OfferDao
import com.example.core.data.database.dao.VacancyDao
import com.example.core.data.database.model.FavoriteVacModelDataBase
import com.example.core.data.database.model.OfferModelDataBase
import com.example.core.data.database.model.VacancyModelDataBase

@Database(entities = [VacancyModelDataBase::class, OfferModelDataBase::class, FavoriteVacModelDataBase::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()  {

    abstract fun vacancyDao() : VacancyDao
    abstract fun offerDao() : OfferDao
    abstract fun favoritesDao() : FavoritesDao

    companion object{
        @Volatile
        private var INSTANSE: AppDatabase? = null // здесь обемпечиваем видимость изменения переменной из других потоков

        fun getDataBase(context: Context): AppDatabase{

            return INSTANSE?: synchronized(this){ // здесь обеспечиваем доступ только одному потоку, по ключу, то есть только один поток может выполнить код одонвременно
                val instanse = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DataBase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANSE = instanse
                // ниже возвращаем возвращаем екземпляр объекта в getDatabase
                instanse
            }


        }
    }
}