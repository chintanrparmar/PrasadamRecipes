package app.chintan.prasadam.domain.model

/**
 * Cooking difficulty level for a recipe.
 */
enum class Difficulty(val labelEn: String, val labelHi: String, val labelGu: String) {
    EASY("Easy", "आसान", "સરળ"),
    MEDIUM("Medium", "मध्यम", "મધ્યમ"),
    HARD("Hard", "कठिन", "અઘરું");

    fun label(language: Language): String = when (language) {
        Language.ENGLISH -> labelEn
        Language.HINDI -> labelHi
        Language.GUJARATI -> labelGu
    }
}
