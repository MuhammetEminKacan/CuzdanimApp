package com.mek.cuzdanimapp.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.budget.CreateBudgetUseCase
import com.mek.cuzdanimapp.domain.usecase.budget.DeleteBudgetUseCase
import com.mek.cuzdanimapp.domain.usecase.budget.GetAllBudgetsUseCase
import com.mek.cuzdanimapp.domain.usecase.budget.UpdateBudgetUseCase
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
class BudgetViewModel @Inject constructor(
    private val getAllBudgetsUseCase: GetAllBudgetsUseCase,
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    private val _effect = Channel<BudgetEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(BudgetEvent.Load)
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
                        sheetErrorMessage = null
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
                        sheetErrorMessage = null
                    )
                }
            }

            is BudgetEvent.HideSheet -> {
                _state.update { it.copy(isAddSheetVisible = false) }
            }

            is BudgetEvent.CategoryChanged -> {
                _state.update { it.copy(selectedCategory = event.category, sheetErrorMessage = null) }
            }

            is BudgetEvent.LimitChanged -> {
                _state.update { it.copy(limitAmount = event.limit, sheetErrorMessage = null) }
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
                    _effect.send(BudgetEffect.ShowError(result.message ?: "Hata"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun save() {
        val state = _state.value

        if (state.selectedCategory.isEmpty()) {
            _state.update { it.copy(sheetErrorMessage = "Kategori seçiniz") }
            return
        }

        val limit = state.limitAmount.toDoubleOrNull()
        if (limit == null || limit <= 0) {
            _state.update { it.copy(sheetErrorMessage = "Geçerli bir limit giriniz") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true, sheetErrorMessage = null) }

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
                    _effect.send(BudgetEffect.ShowError(result.message ?: "Kaydedilemedi"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            when (deleteUseCase(id)) {
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(budgets = state.budgets.filter { it.id != id })
                    }
                    _effect.send(BudgetEffect.Deleted)
                }
                is Resource.Error -> {
                    _effect.send(BudgetEffect.ShowError("Silinemedi"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private suspend fun deleteUseCase(id: Long) = deleteBudgetUseCase(id)
}