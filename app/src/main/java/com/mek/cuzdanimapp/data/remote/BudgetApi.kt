package com.mek.cuzdanimapp.data.remote

import com.mek.cuzdanimapp.data.remote.dto.budget.BudgetDto
import com.mek.cuzdanimapp.data.remote.dto.budget.CreateBudgetRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BudgetApi {

    @GET("budgets/all")
    suspend fun getAllBudgets(): List<BudgetDto>

    @POST("budgets/create")
    suspend fun createBudget(@Body request: CreateBudgetRequestDto): BudgetDto

    @PUT("budgets/update/{id}")
    suspend fun updateBudget(
        @Path("id") id: Long,
        @Body request: CreateBudgetRequestDto
    ): BudgetDto

    @DELETE("budgets/delete/{id}")
    suspend fun deleteBudget(@Path("id") id: Long)
}