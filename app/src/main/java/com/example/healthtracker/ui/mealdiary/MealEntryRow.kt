package com.example.healthtracker.ui.mealdiary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.healthtracker.R
import com.example.healthtracker.domain.model.MealEntry

/**
 * 1 dòng món ăn trong 1 bữa: tên món + số khẩu phần bên trái, calo + nút xoá bên phải.
 *
 * Lưu ý: MealEntry chỉ lưu `quantity` (số khẩu phần, vd 1.0, 2.0), KHÔNG lưu snapshot
 * đơn vị hiển thị kiểu "1 bowl"/"350g" như mockup — field đó không có trong schema.
 * Ở đây tạm hiện "x{quantity}"; nếu muốn khớp y hệt mockup thì phải thêm cột snapshot
 * đơn vị vào MealEntryEntity (bàn riêng khi làm Add/Edit Meal Entry).
 */
@Composable
fun MealEntryRow(
    entry: MealEntry,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(entry.foodName, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 3.dp))
            Text(
                formatQuantity(entry.quantity),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        // Gom kcal + nút xoá vào chung 1 Row, để Row ngoài chỉ còn ĐÚNG 2 phần tử
        // (giống cấu trúc header ở MealTypeSection đang thẳng hàng đúng) — nhờ vậy
        // cụm này luôn áp sát mép phải, không lệch theo weight của Column bên trái.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${entry.calories.toInt()} ${stringResource(R.string.unit_kcal)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 6.dp)
            )
            // IconButton mặc định luôn chiếm khối 48dp (chuẩn vùng chạm a11y), dù icon
            // vẽ ra chỉ 24dp -> canh giữa trong khối đó tạo ra "đệm" quanh icon, làm icon
            // trông lùi vào so với mép phải. Ép size(24.dp) để khối bằng đúng icon,
            // đổi lại vùng chạm nhỏ hơn khuyến nghị — chấp nhận được vì icon đã đủ tách
            // biệt trong hàng, không dễ bấm nhầm.
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.action_delete),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

/** vd 1.0 -> "x1", 1.5 -> "x1.5" — bỏ ".0" thừa khi là số nguyên. */
private fun formatQuantity(quantity: Double): String {
    val value = if (quantity == quantity.toInt().toDouble()) {
        quantity.toInt().toString()
    } else {
        quantity.toString()
    }
    return "x$value"
}
