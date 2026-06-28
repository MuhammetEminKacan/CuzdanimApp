package com.mek.cuzdanimapp.data.remote

import com.mek.cuzdanimapp.data.remote.dto.recurring.CreateRecurringPaymentRequestDto
import com.mek.cuzdanimapp.data.remote.dto.recurring.RecurringPaymentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RecurringPaymentApi {

    @GET("recurring-payments/all")
    suspend fun getAllRecurringPayments(): List<RecurringPaymentDto>

    @POST("recurring-payments/create")
    suspend fun createRecurringPayment(
        @Body request: CreateRecurringPaymentRequestDto
    ): RecurringPaymentDto

    @PATCH("recurring-payments/toggle/{id}")
    suspend fun toggleRecurringPayment(
        @Path("id") id: Long
    ): RecurringPaymentDto

    @DELETE("recurring-payments/delete/{id}")
    suspend fun deleteRecurringPayment(
        @Path("id") id: Long
    )
}