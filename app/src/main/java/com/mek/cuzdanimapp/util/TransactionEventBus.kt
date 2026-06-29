package com.mek.cuzdanimapp.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionEventBus @Inject constructor() {

    private val _events = MutableSharedFlow<TransactionEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    suspend fun emit(event: TransactionEvent) {
        _events.emit(event)
    }

    sealed class TransactionEvent {
        data object TransactionAdded : TransactionEvent()
        data object TransactionDeleted : TransactionEvent()
    }
}