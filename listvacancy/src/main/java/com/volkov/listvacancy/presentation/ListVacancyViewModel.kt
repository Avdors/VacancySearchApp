package com.volkov.listvacancy.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volkov.listvacancy.domain.usecase.FavoriteUseCase
import com.volkov.listvacancy.domain.usecase.ListVacancyUseCase
import com.volkov.listvacancy.presentation.model.ListOfferModel
import com.volkov.listvacancy.presentation.model.ListVacancyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListVacancyViewModel(
    private val listVacancyUseCase: ListVacancyUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : ViewModel() {
    //не private чтобы в тестах добавить вакансию в _vacancies
    private val _vacancies = MutableStateFlow<List<ListVacancyModel>>(emptyList())
    val vacancies: StateFlow<List<ListVacancyModel>> = _vacancies

    private val _offers = MutableStateFlow<List<ListOfferModel>>(emptyList())
    val offers: StateFlow<List<ListOfferModel>> = _offers

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    init {
        viewModelScope.launch {
            // загружаем данные из сети в кеш
            listVacancyUseCase.launchNetworkLoad()
        }
        viewModelScope.launch {
            observeVacanciesFromDB()
            observeOffersFromDB()

        }
    }

    // смотрим за списком вакансий в БД, при загрузке из сети обновляем данные в БД
    private fun observeVacanciesFromDB() {
        viewModelScope.launch {
            listVacancyUseCase.getVacanciesFromDB().collect { domainVacancies ->
                val uiVacancies = ListVacancyMapper.mapToUIModelList(domainVacancies)
                _vacancies.value = uiVacancies
            }
        }

    }

    private fun observeOffersFromDB() {
        viewModelScope.launch {
            listVacancyUseCase.getOffersFromDB().collect { domainOffers ->
                val uiOffers = ListOfferMapper.mapToUIModelList(domainOffers)
                _offers.value = uiOffers
            }
        }

    }

    fun updateFavorites(vacancy: ListVacancyModel) {
        if (vacancy.isFavorite) {
            removeFromFavorites(vacancy)
        } else {
            addToFavorites(vacancy)
        }
    }

    fun removeFromFavorites(vacancy: ListVacancyModel) {
        viewModelScope.launch {
            try {
                val domainModel = ListVacancyMapper.mapToDomainModel(vacancy)
                favoriteUseCase.removeFavorite(domainModel)
                updateFavoriteStatus(vacancy.id, false)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addToFavorites(vacancy: ListVacancyModel) {
        viewModelScope.launch {
            try {
                val domainModel = ListVacancyMapper.mapToDomainModel(vacancy, true)
                favoriteUseCase.addToFavorite(domainModel)
                updateFavoriteStatus(vacancy.id, true)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private fun updateFavoriteStatus(vacancyId: String, isFavorite: Boolean) {
        val updatedVacancies = _vacancies.value.map { vacancy ->
            if (vacancy.id == vacancyId) {
                vacancy.copy(isFavorite = isFavorite)
            } else {
                vacancy
            }
        }
        _vacancies.value = updatedVacancies
    }
}