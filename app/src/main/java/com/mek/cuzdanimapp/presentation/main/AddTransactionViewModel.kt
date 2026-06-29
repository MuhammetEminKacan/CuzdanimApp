package com.mek.cuzdanimapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.recurring.CreateRecurringPaymentUseCase
import com.mek.cuzdanimapp.domain.usecase.transaction.CreateTransactionUseCase
import com.mek.cuzdanimapp.util.Resource
import com.mek.cuzdanimapp.util.TransactionEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val createRecurringPaymentUseCase: CreateRecurringPaymentUseCase,
    private val transactionEventBus: TransactionEventBus
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionSheetState())
    val state: StateFlow<AddTransactionSheetState> = _state.asStateFlow()

    private val _effect = Channel<AddTransactionSheetEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AddTransactionSheetEvent) {
        when (event) {
            is AddTransactionSheetEvent.Show -> {
                _state.update {
                    AddTransactionSheetState(isVisible = true)
                }
            }
            is AddTransactionSheetEvent.Hide -> {
                _state.update { it.copy(isVisible = false) }
            }
            is AddTransactionSheetEvent.TypeChanged -> {
                _state.update {
                    it.copy(selectedType = event.type, selectedCategory = "")
                }
            }
            is AddTransactionSheetEvent.CategoryChanged -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }
            is AddTransactionSheetEvent.AmountChanged -> {
                _state.update { it.copy(amount = event.amount, errorMessage = null) }
            }
            is AddTransactionSheetEvent.DescriptionChanged -> {
                _state.update { it.copy(description = event.description) }
            }
            is AddTransactionSheetEvent.DateChanged -> {
                _state.update { it.copy(transactionDate = event.date) }
            }
            is AddTransactionSheetEvent.ToggleRecurring -> {
                _state.update { it.copy(isRecurring = !it.isRecurring) }
            }
            is AddTransactionSheetEvent.FrequencyChanged -> {
                _state.update { it.copy(frequency = event.frequency) }
            }
            is AddTransactionSheetEvent.SaveClicked -> save()
        }
    }

    private fun save() {
        val state = _state.value

        if (state.selectedCategory.isEmpty()) {
            _state.update { it.copy(errorMessage = "Kategori seçiniz") }
            return
        }

        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _state.update { it.copy(errorMessage = "Geçerli bir tutar giriniz") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = if (state.isRecurring) {
                val title = state.description.ifBlank {
                    categoryDisplayName(state.selectedCategory)
                }
                createRecurringPaymentUseCase(
                    title = title,
                    description = state.description.ifBlank { null },
                    amount = amount,
                    category = state.selectedCategory,
                    type = state.selectedType,
                    frequency = state.frequency,
                    startDate = state.startDate
                )
            } else {
                createTransactionUseCase(
                    type = state.selectedType,
                    category = state.selectedCategory,
                    amount = amount,
                    description = state.description.ifBlank { null },
                    transactionDate = state.transactionDate
                )
            }

            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isVisible = false) }
                    transactionEventBus.emit(TransactionEventBus.TransactionEvent.TransactionAdded)
                    _effect.send(AddTransactionSheetEffect.TransactionAdded)
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(
                        AddTransactionSheetEffect.ShowError(result.message ?: "Bir hata oluştu")
                    )
                }
                is Resource.Loading -> Unit
            }
        }
    }
}