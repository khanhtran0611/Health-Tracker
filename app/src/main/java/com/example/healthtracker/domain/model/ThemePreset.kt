package com.example.healthtracker.domain.model

import androidx.compose.ui.graphics.Color
import com.example.healthtracker.ui.theme.ThemeCandySeed
import com.example.healthtracker.ui.theme.ThemeCarbonSeed
import com.example.healthtracker.ui.theme.ThemeSilkSeed
import com.example.healthtracker.ui.theme.ThemeTerraSeed
import com.example.healthtracker.ui.theme.ThemeVitalityMaterialSeed

/**
 * Preset theme của app — mỗi preset là 1 màu seed, ColorScheme M3 đầy đủ được
 * MaterialKolor sinh runtime từ seed này (xem ui/theme/Theme.kt), không viết
 * tay từng token màu như trước.
 */
enum class ThemePreset(val seedColor: Color) {
    CARBON(ThemeCarbonSeed),
    TERRA(ThemeTerraSeed),
    VITALITY_MATERIAL(ThemeVitalityMaterialSeed),
    SILK(ThemeSilkSeed),
    CANDY(ThemeCandySeed),
}
