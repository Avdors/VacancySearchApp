package com.volkov.cardvacancy.di

import com.example.core.data.database.AppDatabase
import com.volkov.cardvacancy.data.mapper.CardVacancyMapper
import com.volkov.cardvacancy.data.repository.CardVacancyRepositoryImpl
import com.volkov.cardvacancy.domain.repository.CardVacancyRepository
import com.volkov.cardvacancy.domain.usecase.FavoriteCardVacancyUseCase
import com.volkov.cardvacancy.domain.usecase.LoadVacancyUseCase
import com.volkov.cardvacancy.presentation.CardVacancyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cardVacancyModule = module {
    // DAO
    single { get<AppDatabase>().vacancyDao() }
    single { get<AppDatabase>().favoritesDao() }

    // Mapper
    single { CardVacancyMapper() }

    // Repository
    single<CardVacancyRepository> { CardVacancyRepositoryImpl(get(), get(), get()) }

    // Use cases
    factory { LoadVacancyUseCase(get()) }
    factory { FavoriteCardVacancyUseCase(get()) }

    // ViewModel
    viewModel { CardVacancyViewModel(get(), get()) }
}