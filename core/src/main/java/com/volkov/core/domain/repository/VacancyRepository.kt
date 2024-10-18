package com.volkov.core.domain.repository

import com.volkov.core.data.model.ResponseOfferAndVacancies

interface VacancyRepository {
    suspend fun getData(): ResponseOfferAndVacancies

}