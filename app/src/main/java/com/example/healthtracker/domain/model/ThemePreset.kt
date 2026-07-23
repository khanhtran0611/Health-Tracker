package com.example.healthtracker.domain.model

import androidx.compose.ui.graphics.Color
import com.example.healthtracker.ui.theme.ThemeCandySeed
import com.example.healthtracker.ui.theme.ThemeCarbonSeed
import com.example.healthtracker.ui.theme.ThemeSilkSeed
import com.example.healthtracker.ui.theme.ThemeTerraSeed
import com.example.healthtracker.ui.theme.ThemeVitalityMaterialSeed

enum class ThemePreset(val seedColor: Color) {
    CARBON(ThemeCarbonSeed),
    TERRA(ThemeTerraSeed),
    VITALITY_MATERIAL(ThemeVitalityMaterialSeed),
    SILK(ThemeSilkSeed),
    CANDY(ThemeCandySeed),
}
