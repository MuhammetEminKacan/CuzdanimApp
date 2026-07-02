package com.mek.cuzdanimapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mek.cuzdanimapp.domain.usecase.dashboard.GetDashboardSummaryUseCase
import com.mek.cuzdanimapp.presentation.navigation.RecurringRoute
import com.mek.cuzdanimapp.presentation.navigation.TransactionsRoute
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
class DashboardViewModel @Inject constructor(
    private val getDashboardSummaryUseCase: GetDashboardSummaryUseCase,
    private val transactionEventBus: TransactionEventBus
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = Channel<DashboardEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(DashboardEvent.LoadDashboard)
        observeTransactionEvents()
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LoadDashboard,
            is DashboardEvent.Refresh -> loadDashboard()
            is DashboardEvent.OnSeeAllPaymentsClicked -> {
                viewModelScope.launch {
                    _effect.send(DashboardEffect.NavigateTo(RecurringRoute))
                }
            }
            is DashboardEvent.OnSeeAllTransactionsClicked -> {
                viewModelScope.launch {
                    _effect.send(DashboardEffect.NavigateTo(TransactionsRoute))
                }
            }
        }
    }

    private fun observeTransactionEvents() {
        viewModelScope.launch {
            transactionEventBus.events.collect { event ->
                when (event) {
                    is TransactionEventBus.TransactionEvent.TransactionAdded -> loadDashboard()
                    is TransactionEventBus.TransactionEvent.TransactionDeleted -> loadDashboard()
                    is TransactionEventBus.TransactionEvent.RecurringPaymentAdded -> loadDashboard()
                }
            }
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getDashboardSummaryUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(isLoading = false, dashboardData = result.data)
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(DashboardEffect.ShowError(result.message ?: "Bir hata oluştu"))
                }
                is Resource.Loading -> Unit
            }
        }
    }
}