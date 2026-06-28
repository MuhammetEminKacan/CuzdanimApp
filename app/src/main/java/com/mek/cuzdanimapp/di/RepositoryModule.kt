package com.mek.cuzdanimapp.di

import com.mek.cuzdanimapp.data.repository.AuthRepositoryImpl
import com.mek.cuzdanimapp.data.repository.DashboardRepositoryImpl
import com.mek.cuzdanimapp.data.repository.RecurringPaymentRepositoryImpl
import com.mek.cuzdanimapp.data.repository.TransactionRepositoryImpl
import com.mek.cuzdanimapp.domain.repository.AuthRepository
import com.mek.cuzdanimapp.domain.repository.DashboardRepository
import com.mek.cuzdanimapp.domain.repository.RecurringPaymentRepository
import com.mek.cuzdanimapp.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindRecurringPaymentRepository(
        impl: RecurringPaymentRepositoryImpl
    ): RecurringPaymentRepository
}