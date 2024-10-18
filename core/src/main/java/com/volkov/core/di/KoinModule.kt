package com.volkov.core.di

import androidx.room.Room
import com.example.core.api.KtorKlienProvider
import com.example.core.data.database.AppDatabase
import com.volkov.core.data.repository.VacancyRepositoryImpl
import com.volkov.core.domain.repository.VacancyRepository
import com.volkov.core.domain.usecase.GetMappedVacanciesUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

val KoinModule = module {
    // Регистрация HttpClient
    single {
        HttpClient(Android){
            install(ContentNegotiation){
                json()
            }
            install(Logging){
                level = LogLevel.ALL
            }
        }
    }

    // Database
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .build()
    }
    single { get<AppDatabase>().vacancyDao() }
    single { get<AppDatabase>().offerDao() }
    single { get<AppDatabase>().favoritesDao() }


    single { KtorKlienProvider.provideHttpClient() }
    single<VacancyRepository> { VacancyRepositoryImpl(get()) }
    factory { GetMappedVacanciesUseCase(get()) }
}