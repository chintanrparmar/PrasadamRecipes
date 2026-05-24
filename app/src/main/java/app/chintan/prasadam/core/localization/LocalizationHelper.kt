package app.chintan.prasadam.core.localization

import app.chintan.prasadam.domain.model.Language

/**
 * Centralised string catalogue for UI labels.
 * Keys are returned for the active [Language] so composables don't need
 * separate string resources for each language.
 *
 * NOTE: For a full release consider moving these to string resource files
 * (values-hi / values-gu) and using Android's built-in per-app locale API.
 */
object AppStrings {

    fun appTitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "Prasadam Recipes"
        Language.HINDI -> "प्रसादम रेसिपी"
        Language.GUJARATI -> "પ્રસાદમ રેસિપી"
    }

    fun appSubtitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "Sattvik recipes without onion & garlic"
        Language.HINDI -> "बिना प्याज-लहसुन के सात्विक व्यंजन"
        Language.GUJARATI -> "ડુંગળી-લસણ વગરની સાત્વિક રેસિપી"
    }

    fun search(lang: Language) = when (lang) {
        Language.ENGLISH -> "Search recipes…"
        Language.HINDI -> "रेसिपी खोजें…"
        Language.GUJARATI -> "રેસિપી શોધો…"
    }

    fun todaysRecipe(lang: Language) = when (lang) {
        Language.ENGLISH -> "Today's Recipe"
        Language.HINDI -> "आज की रेसिपी"
        Language.GUJARATI -> "આજની રેસિપી"
    }

    fun festivalSpecials(lang: Language) = when (lang) {
        Language.ENGLISH -> "Festival Specials"
        Language.HINDI -> "त्योहार विशेष"
        Language.GUJARATI -> "તહેવાર વિશેષ"
    }

    fun quickRecipes(lang: Language) = when (lang) {
        Language.ENGLISH -> "Quick Recipes"
        Language.HINDI -> "झटपट रेसिपी"
        Language.GUJARATI -> "ઝડપી રેસિપી"
    }

    fun popularRecipes(lang: Language) = when (lang) {
        Language.ENGLISH -> "Popular Recipes"
        Language.HINDI -> "लोकप्रिय रेसिपी"
        Language.GUJARATI -> "લોકપ્રિય રેસિપી"
    }

    fun seeAll(lang: Language) = when (lang) {
        Language.ENGLISH -> "See All"
        Language.HINDI -> "सब देखें"
        Language.GUJARATI -> "બધી જુઓ"
    }

    fun favorites(lang: Language) = when (lang) {
        Language.ENGLISH -> "Favorites"
        Language.HINDI -> "पसंदीदा"
        Language.GUJARATI -> "મનપસંદ"
    }

    fun settings(lang: Language) = when (lang) {
        Language.ENGLISH -> "Settings"
        Language.HINDI -> "सेटिंग्स"
        Language.GUJARATI -> "સેટિંગ્સ"
    }

    fun recipes(lang: Language) = when (lang) {
        Language.ENGLISH -> "Recipes"
        Language.HINDI -> "रेसिपी"
        Language.GUJARATI -> "રેસિપી"
    }

    fun home(lang: Language) = when (lang) {
        Language.ENGLISH -> "Home"
        Language.HINDI -> "होम"
        Language.GUJARATI -> "હોમ"
    }

    fun ingredients(lang: Language) = when (lang) {
        Language.ENGLISH -> "Ingredients"
        Language.HINDI -> "सामग्री"
        Language.GUJARATI -> "સામગ્રી"
    }

    fun instructions(lang: Language) = when (lang) {
        Language.ENGLISH -> "Instructions"
        Language.HINDI -> "विधि"
        Language.GUJARATI -> "પ્રક્રિયા"
    }

    fun notes(lang: Language) = when (lang) {
        Language.ENGLISH -> "Notes"
        Language.HINDI -> "टिप्पणी"
        Language.GUJARATI -> "નોંધ"
    }

    fun prepTime(lang: Language) = when (lang) {
        Language.ENGLISH -> "Prep"
        Language.HINDI -> "तैयारी"
        Language.GUJARATI -> "તૈયારી"
    }

    fun cookTime(lang: Language) = when (lang) {
        Language.ENGLISH -> "Cook"
        Language.HINDI -> "पकाना"
        Language.GUJARATI -> "રસોઈ"
    }

    fun servings(lang: Language) = when (lang) {
        Language.ENGLISH -> "Serves"
        Language.HINDI -> "परोसे"
        Language.GUJARATI -> "પીરસો"
    }

    fun difficulty(lang: Language) = when (lang) {
        Language.ENGLISH -> "Difficulty"
        Language.HINDI -> "कठिनाई"
        Language.GUJARATI -> "કઠિનાઈ"
    }

    fun farali(lang: Language) = when (lang) {
        Language.ENGLISH -> "Farali"
        Language.HINDI -> "फराली"
        Language.GUJARATI -> "ફરાળી"
    }

    fun festivalSpecial(lang: Language) = when (lang) {
        Language.ENGLISH -> "Festival Special"
        Language.HINDI -> "त्योहार विशेष"
        Language.GUJARATI -> "તહેવાર વિશેષ"
    }

    fun noFavoritesTitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "No Favorites Yet"
        Language.HINDI -> "कोई पसंदीदा नहीं"
        Language.GUJARATI -> "હજૂ કોઈ મનપસંદ નથી"
    }

    fun noFavoritesSubtitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "Tap the heart on any recipe to save it here."
        Language.HINDI -> "किसी भी रेसिपी पर दिल दबाएं और यहाँ सेव करें।"
        Language.GUJARATI -> "કોઈ પણ રેસિપી પર હૃદય ટૅપ કરીને અહીં સાચવો."
    }

    fun noResultsTitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "No Recipes Found"
        Language.HINDI -> "कोई रेसिपी नहीं मिली"
        Language.GUJARATI -> "કોઈ રેસિપી મળી નહીં"
    }

    fun noResultsSubtitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "Try a different search term or category."
        Language.HINDI -> "अलग शब्द या श्रेणी आज़माएं।"
        Language.GUJARATI -> "અલગ શબ્દ અથવા વર્ગ અજમાવો."
    }

    fun languageTitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "Language"
        Language.HINDI -> "भाषा"
        Language.GUJARATI -> "ભાષા"
    }

    fun themeTitle(lang: Language) = when (lang) {
        Language.ENGLISH -> "Theme"
        Language.HINDI -> "थीम"
        Language.GUJARATI -> "થીમ"
    }

    fun themeLight(lang: Language) = when (lang) {
        Language.ENGLISH -> "Light"
        Language.HINDI -> "लाइट"
        Language.GUJARATI -> "લાઇટ"
    }

    fun themeDark(lang: Language) = when (lang) {
        Language.ENGLISH -> "Dark"
        Language.HINDI -> "डार्क"
        Language.GUJARATI -> "ડાર્ક"
    }

    fun themeSystem(lang: Language) = when (lang) {
        Language.ENGLISH -> "Follow System"
        Language.HINDI -> "सिस्टम"
        Language.GUJARATI -> "સિસ્ટમ"
    }

    fun about(lang: Language) = when (lang) {
        Language.ENGLISH -> "About"
        Language.HINDI -> "परिचय"
        Language.GUJARATI -> "પરિચય"
    }

    fun disclaimer(lang: Language) = when (lang) {
        Language.ENGLISH ->
            "This app is an independent cookbook inspired by sattvik vegetarian cooking traditions. " +
                "It is not an official BAPS Swaminarayan Sanstha application."
        Language.HINDI ->
            "यह ऐप सात्विक शाकाहारी पाक परंपराओं से प्रेरित एक स्वतंत्र कुकबुक है। " +
                "यह BAPS स्वामीनारायण संस्था का आधिकारिक ऐप नहीं है।"
        Language.GUJARATI ->
            "આ ઍપ સાત્વિક શાકાહારી રસોઈ પ્રણાલીથી પ્રેરિત એક સ્વતંત્ર કૂકબૂક છે. " +
                "આ BAPS સ્વામીનારાયણ સંસ્થાની સત્તાવાર ઍપ નથી."
    }

    fun minuteLabel(lang: Language) = when (lang) {
        Language.ENGLISH -> "min"
        Language.HINDI -> "मिनट"
        Language.GUJARATI -> "મિ"
    }

    fun share(lang: Language) = when (lang) {
        Language.ENGLISH -> "Share Recipe"
        Language.HINDI -> "शेयर करें"
        Language.GUJARATI -> "શેર કરો"
    }

    fun version(lang: Language) = when (lang) {
        Language.ENGLISH -> "Version"
        Language.HINDI -> "संस्करण"
        Language.GUJARATI -> "આવૃત્તિ"
    }
}
