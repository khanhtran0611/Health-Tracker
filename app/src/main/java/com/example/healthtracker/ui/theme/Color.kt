package com.example.healthtracker.ui.theme

import androidx.compose.ui.graphics.Color

// =====================================================================
// Theme preset seed colors — mỗi seed sinh ra 1 bộ ColorScheme M3 đầy đủ
// qua MaterialKolor (xem Theme.kt + domain/model/ThemePreset.kt). Đây là
// giá trị TẠM — khi có mã hex chính xác từ Stitch thì chỉ sửa Color(...)
// ở đây, không đổi cấu trúc.
// =====================================================================

/** Than chì — trầm, chắc chắn, tối giản. */
val ThemeCarbonSeed = Color(0xFF5BA2D5)

/** Đất nung — ấm, mộc mạc, gần gũi thiên nhiên. */
val ThemeTerraSeed = Color(0xFF8D6E63)

/** Vitality Material — theme mặc định, seed = primary cũ (docs/design-system.md). */
val ThemeVitalityMaterialSeed = Color(0xFF4CAF50)

/** Lụa — tím nhẹ, mềm mại, hiện đại. */
val ThemeSilkSeed = Color(0xFF7C4DFF)

/** Kẹo ngọt — hồng tươi, trẻ trung, năng động. */
val ThemeCandySeed = Color(0xFFE91E63)

// =====================================================================
// Vitality Material — theme SÁNG (docs/design-system.md)
// Token lấy nguyên hex từ frontmatter của file, không tự suy diễn.
// GIỮ LẠI để tham khảo/trưng bày — Theme.kt không còn dùng bộ này trực
// tiếp nữa (đã chuyển sang sinh ColorScheme từ seed qua MaterialKolor).
// =====================================================================

val VitalityPrimary = Color(0xFF006E1C)
val VitalityOnPrimary = Color(0xFFFFFFFF)
val VitalityPrimaryContainer = Color(0xFF4CAF50)
val VitalityOnPrimaryContainer = Color(0xFF003C0B)
val VitalityInversePrimary = Color(0xFF78DC77)

val VitalitySecondary = Color(0xFF9F4122)
val VitalityOnSecondary = Color(0xFFFFFFFF)
val VitalitySecondaryContainer = Color(0xFFFD8863)
val VitalityOnSecondaryContainer = Color(0xFF722104)

val VitalityTertiary = Color(0xFF006494)
val VitalityOnTertiary = Color(0xFFFFFFFF)
val VitalityTertiaryContainer = Color(0xFF5BA2D5)
val VitalityOnTertiaryContainer = Color(0xFF003653)

val VitalityError = Color(0xFFBA1A1A)
val VitalityOnError = Color(0xFFFFFFFF)
val VitalityErrorContainer = Color(0xFFFFDAD6)
val VitalityOnErrorContainer = Color(0xFF93000A)

val VitalityBackground = Color(0xFFF9F9F9)
val VitalityOnBackground = Color(0xFF1A1C1C)
val VitalitySurface = Color(0xFFF9F9F9)
val VitalityOnSurface = Color(0xFF1A1C1C)
val VitalitySurfaceVariant = Color(0xFFE2E2E2)
val VitalityOnSurfaceVariant = Color(0xFF3F4A3C)

val VitalityOutline = Color(0xFF6F7A6B)
val VitalityOutlineVariant = Color(0xFFBECAB9)
val VitalityInverseSurface = Color(0xFF2F3131)
val VitalityInverseOnSurface = Color(0xFFF0F1F1)

val VitalitySurfaceDim = Color(0xFFDADADA)
val VitalitySurfaceBright = Color(0xFFF9F9F9)
val VitalitySurfaceContainerLowest = Color(0xFFFFFFFF)
val VitalitySurfaceContainerLow = Color(0xFFF3F3F3)
val VitalitySurfaceContainer = Color(0xFFEEEEEE)
val VitalitySurfaceContainerHigh = Color(0xFFE8E8E8)
val VitalitySurfaceContainerHighest = Color(0xFFE2E2E2)

// =====================================================================
// Obsidian — theme TỐI (docs/design-system2.md)
// File nguồn chỉ đặc tả primary/background/tertiary/error + thang xám zinc
// + text color; các token M3 còn thiếu (container, outline, secondary...)
// được suy ra hợp lý, ghi chú rõ ở từng dòng.
// =====================================================================

val ObsidianPrimary = Color(0xFFA78BFA) // violet — tương tác, focus ring
/** Chữ trên nền violet nhạt: chữ tối cho tương phản cao hơn (đúng nguyên tắc "High contrast always"). */
val ObsidianOnPrimary = Color(0xFF09090B)
val ObsidianPrimaryContainer = Color(0xFF2E1F63) // suy ra: violet đậm, không có trong doc
val ObsidianOnPrimaryContainer = Color(0xFFD9CCFC) // suy ra: tông violet nhạt để đọc trên container đậm

/** Doc không đặc tả secondary — dùng xám zinc trung tính, đúng tinh thần "Accent colors for function, never decoration". */
val ObsidianSecondary = Color(0xFF71717A)
val ObsidianOnSecondary = Color(0xFF09090B)
val ObsidianSecondaryContainer = Color(0xFF27272A)
val ObsidianOnSecondaryContainer = Color(0xFFE4E4E7)

val ObsidianTertiary = Color(0xFF34D399) // emerald — success/positive
/** Cùng lý do như onPrimary: nền emerald sáng cần chữ tối để tương phản cao. */
val ObsidianOnTertiary = Color(0xFF09090B)
val ObsidianTertiaryContainer = Color(0xFF0B3B2A) // suy ra: emerald rất đậm
val ObsidianOnTertiaryContainer = Color(0xFFA7F3D0) // suy ra: emerald nhạt

val ObsidianError = Color(0xFFEF4444)
val ObsidianOnError = Color(0xFFFFFFFF)
val ObsidianErrorContainer = Color(0xFF450A0A) // suy ra: đỏ rất đậm
val ObsidianOnErrorContainer = Color(0xFFFCA5A5) // suy ra: đỏ nhạt

val ObsidianBackground = Color(0xFF09090B) // "true near-black"
val ObsidianOnBackground = Color(0xFFFAFAFA) // chữ chính
val ObsidianSurface = Color(0xFF0C0C0F) // đầu thang zinc (#0c0c0f → #27272a)
val ObsidianOnSurface = Color(0xFFFAFAFA)
val ObsidianSurfaceVariant = Color(0xFF27272A)
val ObsidianOnSurfaceVariant = Color(0xFFA1A1AA) // chữ phụ

val ObsidianOutlineVariant = Color(0xFF27272A) // "border-based separation: 1px solid #27272a"
/** Doc không tách riêng outline vs outline-variant — dùng tông xám sáng hơn 1 bậc cho viền cần nổi bật hơn. */
val ObsidianOutline = Color(0xFF52525B)

val ObsidianSurfaceDim = Color(0xFF09090B)
val ObsidianSurfaceBright = Color(0xFF27272A)
val ObsidianSurfaceContainerLowest = Color(0xFF09090B)
val ObsidianSurfaceContainerLow = Color(0xFF0C0C0F)
val ObsidianSurfaceContainer = Color(0xFF18181B) // "Cards: surface_container background"
val ObsidianSurfaceContainerHigh = Color(0xFF202023)
val ObsidianSurfaceContainerHighest = Color(0xFF27272A)
