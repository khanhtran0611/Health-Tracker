package com.example.healthtracker.ui.stats.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import kotlin.math.roundToInt

/**
 * Thẻ tổng kết 30 ngày gần nhất — cùng bố cục 3 hàng với [WeeklySummaryCard]
 * (tái dùng [SummaryRow]), nhưng KHÔNG có hàng chấm tròn cuối thẻ: 30 chấm dồn
 * 1 hàng sẽ quá nhỏ/rối, không còn ý nghĩa hiển thị trực quan như 7 chấm ở thẻ
 * tuần. Số liệu truyền vào (avgEatenPerDay/avgBurnedPerDay/daysGoalMet) LUÔN cố
 * định theo 30 ngày gần nhất — không đổi khi user lùi/tiến xem tuần khác (xem
 * monthlyFlow ở StatisticsViewModel).
 */
@Composable
fun MonthlySummaryCard(
    avgEatenPerDay: Double,
    avgBurnedPerDay: Double,
    daysGoalMet: Int,
    totalDays: Int,
    modifier: Modifier = Modifier,
) {
    val kcalUnit = stringResource(R.string.unit_kcal)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.stats_monthly_summary_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryRow(
                label = stringResource(R.string.stats_avg_eaten_label),
                value = "${avgEatenPerDay.roundToInt()} $kcalUnit",
                valueColor = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryRow(
                label = stringResource(R.string.stats_avg_burned_label),
                value = "${avgBurnedPerDay.roundToInt()} $kcalUnit",
                valueColor = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryRow(
                label = stringResource(R.string.stats_days_goal_met_label),
                value = stringResource(R.string.stats_days_goal_met_value, daysGoalMet, totalDays),
                valueColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
