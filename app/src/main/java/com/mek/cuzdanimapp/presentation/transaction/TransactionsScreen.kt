package com.mek.cuzdanimapp.presentation.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mek.cuzdanimapp.R
import com.mek.cuzdanimapp.domain.model.Transaction
import com.mek.cuzdanimapp.presentation.main.categoryDisplayName
import com.mek.cuzdanimapp.presentation.main.expenseCategories
import com.mek.cuzdanimapp.presentation.main.incomeCategories
import com.mek.cuzdanimapp.ui.theme.appColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var transactionToDelete by remember { mutableStateOf<Long?>(null) }

    val deletedMessage = stringResource(R.string.transaction_deleted)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TransactionEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is TransactionEffect.TransactionDeleted -> {
                    snackbarHostState.showSnackbar(deletedMessage)
                }
            }
        }
    }

    // Silme dialog'u
    transactionToDelete?.let { id ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text(stringResource(R.string.transaction_delete_title)) },
            text = { Text(stringResource(R.string.transaction_delete_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(TransactionEvent.DeleteTransaction(id))
                        transactionToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.budget_delete_action), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) {
                    Text(stringResource(R.string.budget_cancel_action))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(TransactionEvent.Refresh) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Başlık
                item {
                    Text(
                        text = stringResource(R.string.nav_transactions),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Tip filtresi
                item {
                    val allLabel = stringResource(R.string.filter_all)
                    val incomeLabel = stringResource(R.string.transaction_type_income)
                    val expenseLabel = stringResource(R.string.transaction_type_expense)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val typeFilters = listOf(
                            "ALL" to allLabel,
                            "INCOME" to incomeLabel,
                            "EXPENSE" to expenseLabel
                        )
                        items(typeFilters) { (type, label) ->
                            FilterChip(
                                selected = state.selectedTypeFilter == type,
                                onClick = {
                                    viewModel.onEvent(TransactionEvent.TypeFilterChanged(type))
                                },
                                label = { Text(label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }

                // Kategori filtresi
                item {
                    val categories = when (state.selectedTypeFilter) {
                        "INCOME" -> incomeCategories
                        "EXPENSE" -> expenseCategories
                        else -> incomeCategories + expenseCategories
                    }.distinct()

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = state.selectedCategoryFilter == "ALL",
                                onClick = {
                                    viewModel.onEvent(
                                        TransactionEvent.CategoryFilterChanged("ALL")
                                    )
                                },
                                label = { Text(stringResource(R.string.filter_all_categories)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                        items(categories) { category ->
                            FilterChip(
                                selected = state.selectedCategoryFilter == category,
                                onClick = {
                                    viewModel.onEvent(
                                        TransactionEvent.CategoryFilterChanged(category)
                                    )
                                },
                                label = { Text(categoryDisplayName(category)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }

                // Boş durum
                if (state.filteredTransactions.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.transaction_empty_message),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // İşlem listesi
                items(
                    items = state.filteredTransactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onDeleteClick = { transactionToDelete = transaction.id }
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { data ->
            Snackbar(snackbarData = data)
        }
    }
}

@Composable
private fun TransactionCard(
    transaction: Transaction,
    onDeleteClick: () -> Unit
) {
    val isIncome = transaction.type == "INCOME"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Kategori ikonu
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isIncome) MaterialTheme.appColors.income.copy(alpha = 0.1f)
                            else MaterialTheme.appColors.expense.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = categoryDisplayName(transaction.category).first().toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isIncome) MaterialTheme.appColors.income else MaterialTheme.appColors.expense
                    )
                }

                Column {
                    Text(
                        text = categoryDisplayName(transaction.category),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!transaction.description.isNullOrBlank()) {
                        Text(
                            text = transaction.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = transaction.transactionDate,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(
                            if (isIncome) R.string.dashboard_income_format else R.string.dashboard_expense_format,
                            String.format("%.2f", transaction.amount)
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isIncome) MaterialTheme.appColors.income else MaterialTheme.appColors.expense
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isIncome) MaterialTheme.appColors.income.copy(alpha = 0.1f)
                                else MaterialTheme.appColors.expense.copy(alpha = 0.1f)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isIncome) stringResource(R.string.label_income_caps) else stringResource(R.string.label_expense_caps),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isIncome) MaterialTheme.appColors.income else MaterialTheme.appColors.expense
                        )
                    }
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.budget_delete_action),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}