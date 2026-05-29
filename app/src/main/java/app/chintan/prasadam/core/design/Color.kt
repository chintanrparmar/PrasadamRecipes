package app.chintan.prasadam.core.design

import androidx.compose.ui.graphics.Color

// ── Background tones ──────────────────────────────────────────────────────────
val AppBackground   = Color(0xFFFFF8EF)   // warm off-white
val SoftCream       = Color(0xFFFFF3E0)   // slightly deeper cream
val CardWhite       = Color(0xFFFFFFFF)   // pure white for cards

// ── Primary — Fresh Green ─────────────────────────────────────────────────────
val FreshGreen      = Color(0xFF2F5D3A)
val SoftLeafGreen   = Color(0xFF6B8E4E)
val LightGreenChip  = Color(0xFFEAF3E5)
val DarkForestGreen = Color(0xFF1A3D24)  // hero gradient dark end

// ── Accent — Warm saffron / peach ─────────────────────────────────────────────
val WarmSaffron     = Color(0xFFF59E0B)
val SoftOrange      = Color(0xFFF97316)
val LightPeach      = Color(0xFFFFE4D1)
val PeachTint       = Color(0xFFFFF0E6)  // very soft peach for badges

// ── Text ──────────────────────────────────────────────────────────────────────
val PrimaryText     = Color(0xFF1F2933)
val SecondaryText   = Color(0xFF6B7280)
val MutedText       = Color(0xFF8A8A8A)

// ── Special ───────────────────────────────────────────────────────────────────
val FavoriteGold    = Color(0xFFD4A017)
val ErrorRed        = Color(0xFFB00020)

// ── Dark theme neutrals ───────────────────────────────────────────────────────
val DarkBg          = Color(0xFF111B14)   // very dark, green-tinted
val DarkSurface     = Color(0xFF1A2B1E)
val DarkSurfaceVar  = Color(0xFF253320)
val DarkPrimary     = Color(0xFF6FBB82)
val DarkPrimaryContainer = Color(0xFF1A4825)

// ── Legacy aliases kept for files not yet updated ─────────────────────────────
val Saffron       = WarmSaffron
val DeepSaffron   = SoftOrange
val Maroon        = Color(0xFF7B1E1E)
val DarkMaroon    = Color(0xFF5C1414)
val Cream         = AppBackground
val WarmWhite     = CardWhite
val TempleGold    = FavoriteGold
val LeafGreen     = SoftLeafGreen
val TextBrown     = PrimaryText
val LightBrown    = SecondaryText

// ── Light scheme tokens ───────────────────────────────────────────────────────
val md_theme_light_primary              = FreshGreen
val md_theme_light_onPrimary            = Color.White
val md_theme_light_primaryContainer     = LightGreenChip
val md_theme_light_onPrimaryContainer   = DarkForestGreen
val md_theme_light_secondary            = WarmSaffron
val md_theme_light_onSecondary          = Color.White
val md_theme_light_secondaryContainer   = PeachTint
val md_theme_light_onSecondaryContainer = Color(0xFF4A2000)
val md_theme_light_tertiary             = SoftLeafGreen
val md_theme_light_onTertiary           = Color.White
val md_theme_light_tertiaryContainer    = LightGreenChip
val md_theme_light_onTertiaryContainer  = DarkForestGreen
val md_theme_light_error                = ErrorRed
val md_theme_light_onError              = Color.White
val md_theme_light_errorContainer       = Color(0xFFFFDAD6)
val md_theme_light_onErrorContainer     = Color(0xFF410002)
val md_theme_light_background           = AppBackground
val md_theme_light_onBackground         = PrimaryText
val md_theme_light_surface              = CardWhite
val md_theme_light_onSurface            = PrimaryText
val md_theme_light_surfaceVariant       = SoftCream
val md_theme_light_onSurfaceVariant     = SecondaryText
val md_theme_light_outline              = Color(0xFFD1D5DB)
val md_theme_light_outlineVariant       = Color(0xFFE5E7EB)
val md_theme_light_scrim                = Color.Black

// ── Dark scheme tokens ────────────────────────────────────────────────────────
val md_theme_dark_primary               = DarkPrimary
val md_theme_dark_onPrimary             = Color(0xFF003913)
val md_theme_dark_primaryContainer      = DarkPrimaryContainer
val md_theme_dark_onPrimaryContainer    = Color(0xFF9EFAB2)
val md_theme_dark_secondary             = Color(0xFFFFBB4D)
val md_theme_dark_onSecondary           = Color(0xFF4A2000)
val md_theme_dark_secondaryContainer    = Color(0xFF6A3100)
val md_theme_dark_onSecondaryContainer  = Color(0xFFFFDEB4)
val md_theme_dark_tertiary              = Color(0xFF8DBF7A)
val md_theme_dark_onTertiary            = Color(0xFF1A3A0C)
val md_theme_dark_tertiaryContainer     = Color(0xFF28541A)
val md_theme_dark_onTertiaryContainer   = Color(0xFFA9DC94)
val md_theme_dark_error                 = Color(0xFFFFB4AB)
val md_theme_dark_onError               = Color(0xFF690005)
val md_theme_dark_errorContainer        = Color(0xFF93000A)
val md_theme_dark_onErrorContainer      = Color(0xFFFFDAD6)
val md_theme_dark_background            = DarkBg
val md_theme_dark_onBackground          = Color(0xFFE0F0E6)
val md_theme_dark_surface               = DarkSurface
val md_theme_dark_onSurface             = Color(0xFFDCEEE1)
val md_theme_dark_surfaceVariant        = DarkSurfaceVar
val md_theme_dark_onSurfaceVariant      = Color(0xFFBBCBBF)
val md_theme_dark_outline               = Color(0xFF748C79)
val md_theme_dark_outlineVariant        = Color(0xFF3A4E3E)
val md_theme_dark_scrim                 = Color.Black
