package com.example.data.di

import com.example.data.local.dao.ExpenseDao
import com.example.data.remote.GeminiService
import com.example.repository.ExpenseRepository
import com.example.repository.ExpenseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao,
        geminiService: GeminiService
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao, geminiService)
    }
}