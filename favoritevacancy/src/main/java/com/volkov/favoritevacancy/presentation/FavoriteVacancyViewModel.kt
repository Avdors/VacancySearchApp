package com.volkov.favoritevacancy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volkov.favoritevacancy.domain.usecase.FavoriteVacanciesUseCase
import com.volkov.favoritevacancy.presentation.mapper.FavoriteVacancyMapper
import com.volkov.favoritevacancy.presentation.model.FavoriteVacancyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteVacancyViewModel(
    private val favoriteUseCase: FavoriteVacanciesUseCase
) : ViewModel() {

    private val _vacancies = MutableStateFlow<List<FavoriteVacancyModel>>(emptyList())
    val vacancies: StateFlow<List<FavoriteVacancyModel>> = _vacancies

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Подписываемся на поток данных вакансий
        loadVacancies()
    }


    // для удобства тестирования
    fun loadVacancies() {
        viewModelScope.launch {
            favoriteUseCase.getListVacancies()
                .catch { e ->
                    _error.value = "Ошибка загрузки вакансий: ${e.message}"
                }
                .collect { domainVacancies ->
                    val uiVacancies = FavoriteVacancyMapper.mapToUIModelList(domainVacancies)
                    _vacancies.value = uiVacancies
                }
        }
    }

    fun updateFavorites(vacancy: FavoriteVacancyModel) {

        removeFromFavorites(vacancy)

    }

    private fun removeFromFavorites(vacancy: FavoriteVacancyModel) {
        viewModelScope.launch {
            try {
                val domainModel = FavoriteVacancyMapper.mapToDomainModel(vacancy)
                favoriteUseCase.removeFromFavorites(domainModel)
                updateFavoriteStatus(vacancy.id, false)
            } catch (e: Exception) {
                _error.value = "Ошибка удаления из избранного: ${e.message}"
            }
        }
    }

    private fun updateFavoriteStatus(vacancyId: String, isFavorite: Boolean) {
        val updatedVacancies = _vacancies.value.map { vacancy ->
            if (vacancy.id == vacancyId) {
                // для этого модель Data class
                vacancy.copy(isFavorite = isFavorite)
            } else {
                vacancy
            }
        }
        _vacancies.value = updatedVacancies
    }
}