package app.chintan.prasadam.domain.model

/**
 * Broad category of a recipe in the Prasadam cookbook.
 * All recipes are sattvik (no onion, garlic, meat, egg, or alcohol).
 */
enum class RecipeCategory(val displayNameEn: String, val displayNameHi: String, val displayNameGu: String) {
    ALL("All", "सभी", "બધી"),
    FARALI("Farali", "फराली", "ફરાળી"),
    SABJI("Vegetables", "सब्जी", "શાક"),
    DAL("Dal & Legumes", "दाल", "દાળ"),
    ROTI_BREAD("Breads", "रोटी & ब्रेड", "રોટી"),
    RICE("Rice", "चावल", "ભાત"),
    SNACKS("Snacks", "नाश्ता", "નાસ્તો"),
    SWEETS("Sweets", "मिठाई", "મીઠાઈ"),
    SOUP("Soups", "सूप", "સૂપ"),
    DRINKS("Drinks", "पेय", "પીણું"),
    FESTIVAL("Festival Special", "त्योहार विशेष", "તહેવાર વિશેષ");

    fun displayName(language: Language): String = when (language) {
        Language.ENGLISH -> displayNameEn
        Language.HINDI -> displayNameHi
        Language.GUJARATI -> displayNameGu
    }
}
