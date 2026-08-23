package com.mek.cuzdanimapp.di

import com.mek.cuzdanimapp.BuildConfig
import com.mek.cuzdanimapp.data.remote.AuthApi
import com.mek.cuzdanimapp.data.remote.BudgetApi
import com.mek.cuzdanimapp.data.remote.DashboardApi
import com.mek.cuzdanimapp.data.remote.ProfileApi
import com.mek.cuzdanimapp.data.remote.RecurringPaymentApi
import com.mek.cuzdanimapp.data.remote.TransactionApi
import com.mek.cuzdanimapp.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    // Interceptor'sız - sadece refresh için
    @Provides
    @Singleton
    @Named("refreshClient")
    fun provideRefreshOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Auth interceptor'lı - normal istekler için
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callFactory(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Refresh için ayrı Retrofit
    @Provides
    @Singleton
    @Named("refreshRetrofit")
    fun provideRefreshRetrofit(
        @Named("refreshClient") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callFactory(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    // Refresh için ayrı AuthApi
    @Provides
    @Singleton
    @Named("refreshAuthApi")
    fun provideRefreshAuthApi(
        @Named("refreshRetrofit") retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDashboardApi(retrofit: Retrofit): DashboardApi {
        return retrofit.create(DashboardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionApi(retrofit: Retrofit): TransactionApi {
        return retrofit.create(TransactionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecurringPaymentApi(retrofit: Retrofit): RecurringPaymentApi {
        return retrofit.create(RecurringPaymentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBudgetApi(retrofit: Retrofit): BudgetApi {
        return retrofit.create(BudgetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }
}