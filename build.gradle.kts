// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Note: kotlin.android is intentionally absent — AGP 9.x has built-in Kotlin support.
// Kotlin 2.3+ treats applying kotlin.android with AGP 9 as a hard error.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
