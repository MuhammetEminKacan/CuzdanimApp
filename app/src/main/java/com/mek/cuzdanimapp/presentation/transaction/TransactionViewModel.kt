package com.mek.cuzdanimapp.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.transaction.DeleteTransactionUseCase
import com.mek.cuzdanimapp.domain.usecase.transaction.GetAllTransactionsUseCase
import com.mek.cuzdanimapp.util.Resource
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
class TransactionViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state: StateFlow<TransactionState> = _state.asStateFlow()

    private val _effect = Channel<TransactionEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(TransactionEvent.LoadTransactions)
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.LoadTransactions,
            is TransactionEvent.Refresh -> loadTransactions()

            is TransactionEvent.TypeFilterChanged -> {
                _state.update {
                    it.copy(
                        selectedTypeFilter = event.type,
                        selectedCategoryFilter = "ALL"
                    )
                }
                applyFilters()
            }

            is TransactionEvent.CategoryFilterChanged -> {
                _state.update { it.copy(selectedCategoryFilter = event.category) }
                applyFilters()
            }

            is TransactionEvent.DeleteTransaction -> deleteTransaction(event.id)
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getAllTransactionsUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            transactions = result.data ?: emptyList(),
                            filteredTransactions = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(TransactionEffect.ShowError(result.message ?: "Bir hata oluştu"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun applyFilters() {
        val state = _state.value
        val filtered = state.transactions.filter { transaction ->
            val typeMatch = state.selectedTypeFilter == "ALL" ||
                    transaction.type == state.selectedTypeFilter
            val categoryMatch = state.selectedCategoryFilter == "ALL" ||
                    transaction.category == state.selectedCategoryFilter
            typeMatch && categoryMatch
        }
        _state.update { it.copy(filteredTransactions = filtered) }
    }

    private fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            when (val result = deleteTransactionUseCase(id)) {
                is Resource.Success -> {
                    _state.update {
                        val updated = it.transactions.filter { t -> t.id != id }
                        it.copy(transactions = updated)
                    }
                    applyFilters()
                    _effect.send(TransactionEffect.TransactionDeleted)
                }
                is Resource.Error -> {
                    _effect.send(TransactionEffect.ShowError(result.message ?: "Silinemedi"))
                }
                is Resource.Loading -> Unit
            }
        }
    }
}