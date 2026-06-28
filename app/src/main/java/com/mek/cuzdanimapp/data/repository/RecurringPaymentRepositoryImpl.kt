package com.mek.cuzdanimapp.data.repository

import com.mek.cuzdanimapp.data.mapper.toDomain
import com.mek.cuzdanimapp.data.remote.RecurringPaymentApi
import com.mek.cuzdanimapp.data.remote.dto.recurring.CreateRecurringPaymentRequestDto
import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.domain.repository.RecurringPaymentRepository
import com.mek.cuzdanimapp.util.Resource
import javax.inject.Inject

class RecurringPaymentRepositoryImpl @Inject constructor(
    private val api: RecurringPaymentApi
) : RecurringPaymentRepository {

    override suspend fun getAllRecurringPayments(): Resource<List<RecurringPayment>> {
        return try {
            Resource.Success(api.getAllRecurringPayments().map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun createRecurringPayment(
        title: String,
        description: String?,
        amount: Double,
        category: String,
        type: String,
        frequency: String,
        startDate: String
    ): Resource<RecurringPayment> {
        return try {
            val response = api.createRecurringPayment(
                CreateRecurringPaymentRequestDto(
                    title, description, amount, category, type, frequency, startDate
                )
            )
            Resource.Success(response.toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun toggleRecurringPayment(id: Long): Resource<RecurringPayment> {
        return try {
            Resource.Success(api.toggleRecurringPayment(id).toDomain())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }

    override suspend fun deleteRecurringPayment(id: Long): Resource<Unit> {
        return try {
            api.deleteRecurringPayment(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Bir hata oluştu")
        }
    }
}