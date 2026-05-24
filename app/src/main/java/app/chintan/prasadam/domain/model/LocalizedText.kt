package app.chintan.prasadam.domain.model

/**
 * A piece of text available in all three supported languages.
 * Use [get] to retrieve the appropriate string for the active language,
 * falling back to English if a translation is blank.
 */
data class LocalizedText(
    val en: String,
    val hi: String,
    val gu: String
) {
    /**
     * Returns the string for [language], falling back to English if the translation
     * is absent or clearly a placeholder (< 4 meaningful characters).
     *
     * TODO: Replace abbreviated placeholder content in recipes.json with complete translations.
     */
    fun get(language: Language): String = when (language) {
        Language.ENGLISH -> en.meaningfulOrNull() ?: hi.meaningfulOrNull() ?: gu
        Language.HINDI -> hi.meaningfulOrNull() ?: en
        Language.GUJARATI -> gu.meaningfulOrNull() ?: en
    }

    private fun String.meaningfulOrNull(): String? = trim().takeIf { it.length >= 4 }
}

/**
 * A localized list of strings (e.g. ingredients or instructions).
 */
data class LocalizedList(
    val en: List<String>,
    val hi: List<String>,
    val gu: List<String>
) {
    /**
     * Returns the list for [language], falling back to English if the list is
     * empty or its first entry is clearly a placeholder.
     *
     * TODO: Replace abbreviated placeholder content in recipes.json with complete translations.
     */
    fun get(language: Language): List<String> = when (language) {
        Language.ENGLISH -> en.usefulOrNull() ?: hi.usefulOrNull() ?: gu
        Language.HINDI -> hi.usefulOrNull() ?: en
        Language.GUJARATI -> gu.usefulOrNull() ?: en
    }

    private fun List<String>.usefulOrNull(): List<String>? =
        takeIf { it.isNotEmpty() && it.first().trim().length >= 4 }
}
