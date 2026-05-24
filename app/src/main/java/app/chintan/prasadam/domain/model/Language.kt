package app.chintan.prasadam.domain.model

/**
 * Supported display languages in the app.
 * The app defaults to ENGLISH and allows switching to HINDI or GUJARATI.
 */
enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી")
}
