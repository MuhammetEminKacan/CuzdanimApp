package com.mek.cuzdanimapp.presentation.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.mek.cuzdanimapp.R
import com.mek.cuzdanimapp.domain.model.DashboardData
import com.mek.cuzdanimapp.domain.model.RecurringPayment
import com.mek.cuzdanimapp.domain.model.Transaction
import com.mek.cuzdanimapp.ui.theme.appColors
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (NavKey) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DashboardEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is DashboardEffect.NavigateTo -> onNavigate(effect.route)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(DashboardEvent.Refresh) }
        ) {
            if (state.isLoading && state.dashboardData == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                DashboardContent(
                    data = state.dashboardData,
                    onEvent = { viewModel.onEvent(it) }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun DashboardContent(
    data: DashboardData?,
    onEvent: (DashboardEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Card — her zaman göster
        item {
            BalanceCard(data = data)
        }

        // Yaklaşan Ödemeler — her zaman başlık göster
        item {
            SectionHeader(
                title = stringResource(R.string.dashboard_upcoming_payments),
                onSeeAll = { onEvent(DashboardEvent.OnSeeAllPaymentsClicked) }
            )
        }

        val upcomingPayments = data?.upcomingPayments ?: emptyList()
        if (upcomingPayments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_no_upcoming_payments),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(upcomingPayments) { payment ->
                        UpcomingPaymentCard(payment = payment)
                    }
                }
            }
        }

        // Son İşlemler — her zaman başlık göster
        item {
            SectionHeader(
                title = stringResource(R.string.dashboard_recent_transactions),
                onSeeAll = { onEvent(DashboardEvent.OnSeeAllTransactionsClicked) }
            )
        }

        val recentTransactions = data?.recentTransactions ?: emptyList()
        if (recentTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_no_transactions),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        recentTransactions.forEach { transaction ->
                            TransactionItem(transaction = transaction)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceCard(data: DashboardData?) {
    val contentColor = MaterialTheme.colorScheme.onPrimary
    val contentColorAlpha = contentColor.copy(alpha = 0.7f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.dashboard_total_balance),
                fontSize = 12.sp,
                color = contentColorAlpha
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.dashboard_amount_format,
                    String.format(Locale.US, "%.2f", data?.totalBalance ?: 0.0)
                ),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(contentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.dashboard_income_label),
                            fontSize = 11.sp,
                            color = contentColorAlpha
                        )
                        Text(
                            text = stringResource(
                                R.string.dashboard_amount_format,
                                String.format(Locale.US, "%.2f", data?.monthlyIncome ?: 0.0)
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(contentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.dashboard_expense_label),
                            fontSize = 11.sp,
                            color = contentColorAlpha
                        )
                        Text(
                            text = stringResource(
                                R.string.dashboard_amount_format,
                                String.format(Locale.US, "%.2f", data?.monthlyExpense ?: 0.0)
                            ),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextButton(onClick = onSeeAll) {
            Text(
                text = stringResource(R.string.dashboard_see_all),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun UpcomingPaymentCard(payment: RecurringPayment) {
    val localizedFrequency = getLocalizedFrequency(payment.frequency)
    val displayTitle = getLocalizedCategory(payment.title)

    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayTitle.firstOrNull()?.toString() ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = displayTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = localizedFrequency,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(
                    R.string.dashboard_amount_format,
                    String.format(Locale.US, "%.2f", payment.amount)
                ),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val type = transaction.type.trim()
    val isIncome = type.equals("INCOME", ignoreCase = true) ||
            type.equals("GELİR", ignoreCase = true) ||
            type.equals("GELIR", ignoreCase = true)

    val localizedCategory = getLocalizedCategory(transaction.category)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedCategory.firstOrNull()?.toString() ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column {
                Text(
                    text = localizedCategory,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = transaction.transactionDate,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = stringResource(
                if (isIncome) R.string.dashboard_income_format
                else R.string.dashboard_expense_format,
                String.format(Locale.US, "%.2f", transaction.amount)
            ),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isIncome) MaterialTheme.appColors.income else MaterialTheme.appColors.expense
        )
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
        match("INVESTMENT", "YATIRIM") -> stringResource(R.string.category_investment)
        match("SCHOLARSHIP", "BURS") -> stringResource(R.string.category_scholarship)
        match("BONUS", "PRİM", "PRIM") -> stringResource(R.string.category_bonus)
        match("GROCERIES", "MARKET") -> stringResource(R.string.category_groceries)
        match("FOOD", "YEMEK") -> stringResource(R.string.category_food)
        match("TRANSPORTATION", "ULAŞIM", "ULASIM") -> stringResource(R.string.category_transportation)
        match("FUEL", "YAKIT") -> stringResource(R.string.category_fuel)
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