package com.mek.cuzdanimapp.presentation.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.recurring.DeleteRecurringPaymentUseCase
import com.mek.cuzdanimapp.domain.usecase.recurring.GetAllRecurringPaymentsUseCase
import com.mek.cuzdanimapp.domain.usecase.recurring.ToggleRecurringPaymentUseCase
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
class RecurringPaymentViewModel @Inject constructor(
    private val getAllRecurringPaymentsUseCase: GetAllRecurringPaymentsUseCase,
    private val toggleRecurringPaymentUseCase: ToggleRecurringPaymentUseCase,
    private val deleteRecurringPaymentUseCase: DeleteRecurringPaymentUseCase,
    private val transactionEventBus: TransactionEventBus
) : ViewModel() {

    private val _state = MutableStateFlow(RecurringPaymentState())
    val state: StateFlow<RecurringPaymentState> = _state.asStateFlow()

    private val _effect = Channel<RecurringPaymentEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(RecurringPaymentEvent.LoadRecurringPayments)
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            transactionEventBus.events.collect { event ->
                when (event) {
                    is TransactionEventBus.TransactionEvent.RecurringPaymentAdded -> load()
                    else -> Unit
                }
            }
        }
    }

    fun onEvent(event: RecurringPaymentEvent) {
        when (event) {
            is RecurringPaymentEvent.LoadRecurringPayments,
            is RecurringPaymentEvent.Refresh -> load()
            is RecurringPaymentEvent.ToggleActive -> toggle(event.id)
            is RecurringPaymentEvent.Delete -> delete(event.id)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getAllRecurringPaymentsUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            recurringPayments = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(RecurringPaymentEffect.ShowError(result.message ?: "Hata"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun toggle(id: Long) {
        viewModelScope.launch {
            when (val result = toggleRecurringPaymentUseCase(id)) {
                is Resource.Success -> {
                    result.data?.let { updated ->
                        _state.update { state ->
                            state.copy(
                                recurringPayments = state.recurringPayments.map {
                                    if (it.id == id) updated else it
                                }
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _effect.send(RecurringPaymentEffect.ShowError(result.message ?: "Hata"))
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            when (deleteRecurringPaymentUseCase(id)) {
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(
                            recurringPayments = state.recurringPayments.filter { it.id != id }
                        )
                    }
                    _effect.send(RecurringPaymentEffect.Deleted)
                }
                is Resource.Error -> {
                    _effect.send(RecurringPaymentEffect.ShowError("Silinemedi"))
                }
                is Resource.Loading -> Unit
            }
        }
    }
}