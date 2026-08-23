package com.mek.cuzdanimapp.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.budget.CreateBudgetUseCase
import com.mek.cuzdanimapp.domain.usecase.budget.DeleteBudgetUseCase
import com.mek.cuzdanimapp.domain.usecase.budget.GetAllBudgetsUseCase
import com.mek.cuzdanimapp.domain.usecase.budget.UpdateBudgetUseCase
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
class BudgetViewModel @Inject constructor(
    private val getAllBudgetsUseCase: GetAllBudgetsUseCase,
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val transactionEventBus: TransactionEventBus
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    private val _effect = Channel<BudgetEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(BudgetEvent.Load)
        observeTransactionEvents()
    }

    private fun observeTransactionEvents() {
        viewModelScope.launch {
            transactionEventBus.events.collect { event ->
                when (event) {
                    is TransactionEventBus.TransactionEvent.TransactionAdded,
                    is TransactionEventBus.TransactionEvent.TransactionDeleted -> load()
                    is TransactionEventBus.TransactionEvent.RecurringPaymentAdded -> Unit
                }
            }
        }
    }

    fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.Load,
            is BudgetEvent.Refresh -> load()

            is BudgetEvent.ShowAddSheet -> {
                _state.update {
                    it.copy(
                        isAddSheetVisible = true,
                        editingBudget = null,
                        selectedCategory = "",
                        limitAmount = "",
                        sheetErrorCode = null
                    )
                }
            }

            is BudgetEvent.ShowEditSheet -> {
                _state.update {
                    it.copy(
                        isAddSheetVisible = true,
                        editingBudget = event.budget,
                        selectedCategory = event.budget.category,
                        limitAmount = event.budget.monthlyLimit.toString(),
                        sheetErrorCode = null
                    )
                }
            }

            is BudgetEvent.HideSheet -> {
                _state.update { it.copy(isAddSheetVisible = false) }
            }

            is BudgetEvent.CategoryChanged -> {
                _state.update { it.copy(selectedCategory = event.category, sheetErrorCode = null) }
            }

            is BudgetEvent.LimitChanged -> {
                _state.update { it.copy(limitAmount = event.limit, sheetErrorCode = null) }
            }

            is BudgetEvent.Save -> save()

            is BudgetEvent.Delete -> delete(event.id)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getAllBudgetsUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(isLoading = false, budgets = result.data ?: emptyList())
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(BudgetEffect.ShowError(result.message ?: ""))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun save() {
        val state = _state.value

        if (state.selectedCategory.isEmpty()) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_CATEGORY_EMPTY") }
            return
        }

        val limit = state.limitAmount.toDoubleOrNull()
        if (limit == null || limit <= 0) {
            _state.update { it.copy(sheetErrorCode = "LOCAL_INVALID_LIMIT") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true, sheetErrorCode = null) }

            val result = if (state.editingBudget != null) {
                updateBudgetUseCase(state.editingBudget.id, state.selectedCategory, limit)
            } else {
                createBudgetUseCase(state.selectedCategory, limit)
            }

            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(isSheetLoading = false, isAddSheetVisible = false) }
                    _effect.send(BudgetEffect.Saved)
                    load()
                }
                is Resource.Error -> {
                    _state.update { it.copy(isSheetLoading = false) }
                    _effect.send(BudgetEffect.ShowError(result.message ?: ""))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            when (deleteBudgetUseCase(id)) {
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(budgets = state.budgets.filter { it.id != id })
                    }
                    _effect.send(BudgetEffect.Deleted)
                }
                is Resource.Error -> {
                    _effect.send(BudgetEffect.ShowError(""))
                }
                is Resource.Loading -> Unit
            }
        }
    }
}