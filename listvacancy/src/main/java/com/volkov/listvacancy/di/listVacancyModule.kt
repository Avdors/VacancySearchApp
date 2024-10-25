package com.volkov.listvacancy.di


import com.volkov.listvacancy.data.LocalDataSource
import com.volkov.listvacancy.data.RemoteDataSource
import com.volkov.listvacancy.data.repository.ListVacancyRepositoryImpl
import com.volkov.listvacancy.domain.mapper.ListVacOfferDataMaper
import com.volkov.listvacancy.domain.mapper.ListVacancyDataMapper
import com.volkov.listvacancy.domain.repository.ListVacancyRepository
import com.volkov.listvacancy.domain.usecase.FavoriteUseCase
import com.volkov.listvacancy.domain.usecase.ListVacancyUseCase
import com.volkov.listvacancy.presentation.ListVacancyViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listVacancyModule = module {

    // Регистрация HttpClient
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    single { RemoteDataSource(get()) }
    single {
        LocalDataSource(
            get(),
            get(),
            get()
        )
    }  // Получение DAO-ов из core (VacancyDao, OfferDao, FavoritesDao)

    // Маппер для вакансий и офферов
    single { ListVacancyDataMapper() }
    single { ListVacOfferDataMaper() }

    // Биндинг для репозитория ListVacancyRepositoryImpl
    single<ListVacancyRepository> {
        ListVacancyRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            listVacancyDataMapper = get(),
            listVacOfferDataMaper = get()
        )
    }

    // Use cases
    factory { ListVacancyUseCase(get()) }
    factory { FavoriteUseCase(get()) }

    // ViewModel
    viewModel {
        ListVacancyViewModel(
            listVacancyUseCase = get(),
            favoriteUseCase = get()
        )
    }

}