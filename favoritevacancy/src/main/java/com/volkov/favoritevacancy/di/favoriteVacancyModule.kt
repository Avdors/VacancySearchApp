package com.volkov.favoritevacancy.di

import com.example.core.data.database.AppDatabase
import com.volkov.favoritevacancy.data.repository.FavoriteVacancyRepositoryImpl
import com.volkov.favoritevacancy.domain.mapper.FavoriteVacancyDataMaper
import com.volkov.favoritevacancy.domain.repository.FavoriteVacancyRepository
import com.volkov.favoritevacancy.domain.usecase.FavoriteVacanciesUseCase
import com.volkov.favoritevacancy.presentation.FavoriteVacancyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteVacancyModule = module {
    // Репозиторий для избранных вакансий
    single<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(
            vacancyDataMaper = get(),
            favoritesDao = get() // Dao для работы с БД (FavoritesDao)
        )
    }

    // Маппер для избранных вакансий (преобразование данных между Domain и Database слоями)
    single { FavoriteVacancyDataMaper() }

    // Use case для работы с избранными вакансиями (CRUD операции)
    factory { FavoriteVacanciesUseCase(get()) }

    // ViewModel для работы с избранными вакансиями
    viewModel {
        FavoriteVacancyViewModel(
            favoriteUseCase = get() // Подключаем use case к ViewModel
        )
    }

    // DAO для работы с таблицей избранных вакансий (FavoritesDao) - получаем из core
    single { get<AppDatabase>().favoritesDao() }
}