package com.mek.cuzdanimapp.presentation.recurring

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.ui.theme.appColors
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringPaymentsScreen(
    viewModel: RecurringPaymentViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var paymentToDelete by remember { mutableStateOf<Long?>(null) }

    val deletedMessage = stringResource(R.string.recurring_deleted)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RecurringPaymentEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is RecurringPaymentEffect.Deleted -> {
                    snackbarHostState.showSnackbar(deletedMessage)
                }
            }
        }
    }

    paymentToDelete?.let { id ->
        AlertDialog(
            onDismissRequest = { paymentToDelete = null },
            title = { Text(stringResource(R.string.recurring_delete_title)) },
            text = { Text(stringResource(R.string.recurring_delete_confirmation)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(RecurringPaymentEvent.Delete(id))
                        paymentToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.budget_delete_action), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { paymentToDelete = null }) {
                    Text(stringResource(R.string.budget_cancel_action))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(RecurringPaymentEvent.Refresh) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.nav_recurring),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (state.recurringPayments.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.recurring_empty_message),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                items(
                    items = state.recurringPayments,
                    key = { it.id }
                ) { payment ->
                    RecurringPaymentCard(
                        payment = payment,
                        onToggle = { viewModel.onEvent(RecurringPaymentEvent.ToggleActive(payment.id)) },
                        onDelete = { paymentToDelete = payment.id }
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
private fun RecurringPaymentCard(
    payment: RecurringPayment,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val categoryTrimmed = payment.category.trim()
    val isIncome = categoryTrimmed.equals("INCOME", ignoreCase = true) ||
            categoryTrimmed.equals("GELİR", ignoreCase = true) ||
            categoryTrimmed.equals("GELIR", ignoreCase = true)

    val localizedCategory = getLocalizedCategory(payment.category)
    val localizedFrequency = getLocalizedFrequency(payment.frequency)
    val displayTitle = getLocalizedCategory(payment.title)

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
                        text = displayTitle.firstOrNull()?.toString() ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isIncome) MaterialTheme.appColors.income else MaterialTheme.appColors.expense
                    )
                }

                Column {
                    Text(
                        text = displayTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = localizedCategory,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = localizedFrequency,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.dashboard_amount_format, String.format(Locale.US, "%.2f", payment.amount)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isIncome) MaterialTheme.appColors.income else MaterialTheme.appColors.expense
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Switch(
                        checked = payment.active,
                        onCheckedChange = { onToggle() },
                        modifier = Modifier.size(40.dp, 24.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    IconButton(
                        onClick = onDelete,
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
}

@Composable
private fun getLocalizedCategory(category: String): String {
    val c = category.trim()
    fun match(vararg words: String) = words.any { it.equals(c, ignoreCase = true) }

    return when {
        match("INCOME", "GELİR", "GELIR") -> stringResource(R.string.transaction_type_income)
        match("EXPENSE", "GİDER", "GIDER") -> stringResource(R.string.transaction_type_expense)
        match("SALARY", "MAAŞ", "MAAS") -> stringResource(R.string.category_salary)
        match("FREELANCE") -> stringResource(R.string.category_freelance)
        match("INVESTMENT", "YATIRIM", "YATIRIM") -> stringResource(R.string.category_investment)
        match("SCHOLARSHIP", "BURS") -> stringResource(R.string.category_scholarship)
        match("BONUS", "PRİM", "PRIM") -> stringResource(R.string.category_bonus)
        match("GROCERIES", "MARKET") -> stringResource(R.string.category_groceries)
        match("FOOD", "YEMEK") -> stringResource(R.string.category_food)
        match("TRANSPORTATION", "ULAŞIM", "ULASIM") -> stringResource(R.string.category_transportation)
        match("FUEL", "YAKIT", "YAKIT") -> stringResource(R.string.category_fuel)
        match("HEALTH", "SAĞLIK", "SAGLIK") -> stringResource(R.string.category_health)
        match("EDUCATION", "EĞİTİM", "EGITIM") -> stringResource(R.string.category_education)
        match("ENTERTAINMENT", "EĞLENCE", "EGLENCE") -> stringResource(R.string.category_entertainment)
        match("RENT", "KİRA", "KIRA") -> stringResource(R.string.category_rent)
        match("BILLS", "FATURA") -> stringResource(R.string.category_bills)
        match("SHOPPING", "ALIŞVERİŞ", "ALISVERIS") -> stringResource(R.string.category_shopping)
        match("OTHER", "DİĞER", "DIGER") -> stringResource(R.string.category_other)
        else -> category
    }
}

@Composable
private fun getLocalizedFrequency(frequency: String): String {
    val f = frequency.trim()
    fun match(vararg words: String) = words.any { it.equals(f, ignoreCase = true) }

    return when {
        match("DAILY", "GÜNLÜK", "GUNLUK") -> stringResource(R.string.frequency_daily)
        match("WEEKLY", "HAFTALIK") -> stringResource(R.string.frequency_weekly)
        match("MONTHLY", "AYLIK") -> stringResource(R.string.frequency_monthly)
        match("YEARLY", "YILLIK") -> stringResource(R.string.frequency_yearly)
        else -> frequency
    }
}