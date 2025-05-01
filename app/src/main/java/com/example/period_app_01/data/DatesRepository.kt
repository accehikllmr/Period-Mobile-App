package com.example.period_app_01.data

import kotlinx.coroutines.flow.Flow

// interface, so declares functions/methods without implementation
interface DatesRepository {
    // can add more functions later, when adding more features
    // for single date, plural for consistency
    suspend fun insertDates(dates: Dates)

    suspend fun deleteLast()

    fun getLastEntry(): Flow<Dates?>
}