package com.volkov.cardvacancy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volkov.cardvacancy.domain.model.CardVacancyDomainModel
import com.volkov.cardvacancy.domain.usecase.FavoriteCardVacancyUseCase
import com.volkov.cardvacancy.domain.usecase.LoadVacancyUseCase
import com.volkov.cardvacancy.presentation.mapper.CardVacancyMapperUI
import com.volkov.cardvacancy.presentation.model.CardVacancyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardVacancyViewModel(
    private val loadVacancyUseCase: LoadVacancyUseCase,
    private val favoriteUseCase: FavoriteCardVacancyUseCase
) : ViewModel() {
    private val _vacancy = MutableStateFlow<CardVacancyModel?>(null)
    val vacancy: StateFlow<CardVacancyModel?> = _vacancy

    private val _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private lateinit var currentVacancy: CardVacancyDomainModel

    // Загрузка вакансии по ID
    fun loadVacancy(vacancyId: String) {
        viewModelScope.launch {
            try {
                loadVacancyUseCase.loadVacancyById(vacancyId).collect { vacancyDomain ->
                    currentVacancy = vacancyDomain
                    _vacancy.value = CardVacancyMapperUI.mapToUIModel(currentVacancy)
                    _isFavorite.value = currentVacancy.isFavorite
                }
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }

    fun loadVacancyFavorite(vacancyId: String) {
        viewModelScope.launch {
            try {
                loadVacancyUseCase.loadVacancyByIdFavorite(vacancyId).collect { vacancyDomain ->
                    currentVacancy = vacancyDomain
                    _vacancy.value = CardVacancyMapperUI.mapToUIModel(currentVacancy)
                    _isFavorite.value = currentVacancy.isFavorite
                }
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }

    // Переключение состояния избранного
    fun toggleFavorite() {
        viewModelScope.launch {
            currentVacancy?.let { vacancy ->
                _isFavorite.value.let { isFavorite ->
                    val updatedVacancy = vacancy.copy(isFavorite = !isFavorite)
                    favoriteUseCase.togleFavorite(updatedVacancy)
                    _isFavorite.value = updatedVacancy.isFavorite
                    currentVacancy = updatedVacancy
                }
            }
        }
    }
}