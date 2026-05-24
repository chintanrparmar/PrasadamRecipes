# 🪔 Prasadam Recipes

A production-quality Android cookbook app for **sattvik vegetarian recipes** — inspired by Swaminarayan cooking traditions.  
All recipes are strictly **vegetarian and free from onion, garlic, egg, meat, and alcohol**.

> **Disclaimer:** This app is an independent cookbook inspired by sattvik vegetarian cooking traditions. It is not an official BAPS Swaminarayan Sanstha application.

---

## ✨ Features

| Feature | Details |
|---------|---------|
| 🏠 Home | App header, search bar, Today's Recipe, Festival Specials, Quick & Popular sections |
| 📋 Recipes | Filterable by category, full-text search across name + ingredients + tags |
| 🍽️ Recipe Detail | Hero image placeholder, ingredients, numbered instructions, notes, share |
| ❤️ Favorites | Persisted favorites with empty state |
| ⚙️ Settings | Language switcher (EN / HI / GU) + Theme (Light / Dark / System) + About/Disclaimer |

---

## 🏛️ Architecture

The project follows **Clean Architecture** with clear separation of concerns:

```
app.chintan.prasadam
├── app/          — Application & MainActivity
├── core/
│   ├── design/   — Material 3 color/type/shape + PrasadamTheme
│   ├── navigation/  — Screen routes + NavGraph
│   ├── common/   — UiState sealed interface
│   └── localization/ — AppStrings (EN/HI/GU)
├── data/
│   ├── local/    — Room DB, DAO, Entity, Converters
│   ├── source/   — RecipeJsonParser + DataSeedManager
│   ├── mapper/   — Entity → Domain mapping
│   └── repository/ — RecipeRepositoryImpl, UserPreferencesRepositoryImpl
├── domain/
│   ├── model/    — Recipe, LocalizedText, Language, ThemePreference …
│   ├── repository/ — Interfaces
│   └── usecase/  — 8 use cases
├── feature/
│   ├── home/     — HomeViewModel, HomeScreen
│   ├── recipes/  — RecipesViewModel, RecipesScreen
│   ├── recipedetail/ — RecipeDetailViewModel, RecipeDetailScreen
│   ├── favorites/ — FavoritesViewModel, FavoritesScreen
│   └── settings/ — SettingsViewModel, SettingsScreen
└── di/           — Hilt modules
```

**UI pattern:** Route composable → injects ViewModel via `hiltViewModel()` → passes state + callbacks to stateless child composables.

---

## 🛠️ Tech Stack

| Technology | Version / Notes |
|------------|----------------|
| Kotlin | 2.2.10 |
| Jetpack Compose | 2026 BOM |
| Material 3 | Latest via BOM |
| Hilt | 2.56 (KSP-based) |
| Room | 2.7.1 |
| DataStore Preferences | 1.1.4 |
| Navigation Compose | 2.9.0 |
| Kotlinx Serialization | 1.8.1 |
| Kotlin Coroutines + Flow | 1.10.2 |
| MockK + Turbine | Unit tests |

---

## 📦 Data Approach

1. **First launch:** `DataSeedManager` calls `RecipeJsonParser` which reads `app/src/main/assets/recipes.json` and inserts all 20 recipes into Room via `RecipeDao.insertAll()`.  
2. **Subsequent launches:** Room is the single source of truth — the JSON is never re-parsed.  
3. **Localization:** Each recipe stores `en`, `hi`, and `gu` text/list fields as flat columns. The `LocalizedText.get(Language)` helper picks the right string at render time.  
4. **Favorites:** Stored as a `isFavorite` boolean column in Room, toggled via `ToggleFavoriteRecipeUseCase`.  
5. **Preferences:** Language and theme are persisted via `DataStore<Preferences>`.

---

## 🚀 How to Run

### Prerequisites
- Android Studio Ladybug (2024.2.1) or later  
- JDK 11 or 17  
- Android device / emulator with API 26+

### Steps
```bash
git clone <repo-url>
cd PrasadamRecipes
# Open in Android Studio → sync Gradle → Run app (▶)
```

> **KSP Version Note:** If Gradle sync fails on KSP, update the `ksp` version in `gradle/libs.versions.toml` to match the latest release for your Kotlin version. Check: https://github.com/google/ksp/releases

---

## 🎨 Design System

| Token | Hex | Usage |
|-------|-----|-------|
| Saffron | `#FF9800` | Primary accent, buttons, chips |
| Deep Saffron | `#E57C00` | Gradients, hero backgrounds |
| Maroon | `#7B1E1E` | Secondary accent |
| Cream | `#FFF8E7` | Light background |
| Warm White | `#FFFCF5` | Card surfaces |
| Temple Gold | `#D4A017` | Decorative highlights |
| Leaf Green | `#4F7D3A` | Farali / sattvik badge |
| Text Brown | `#3E2723` | Body text |

Dynamic Color (Material You) is enabled on Android 12+ — the static palette is used as fallback.

---

## 🧪 Tests

Unit tests cover:

| Test Class | What it tests |
|-----------|---------------|
| `SearchRecipesUseCaseTest` | Blank query, name match, ingredient match, tag match, no-match |
| `ToggleFavoriteRecipeUseCaseTest` | Delegation to repository, multiple calls |
| `RecipeRepositoryTest` | CRUD delegation to DAO, seeding, favorite toggle |
| `RecipesViewModelTest` | Initial state, recipe emission, category selection, search clear |

Run with:
```bash
./gradlew :app:test
```

---

## 🔮 Future / TODOs

The codebase contains `// TODO` comments marking these planned extensions:

- **Backend sync:** Replace local JSON with a remote API (Retrofit/Ktor) when a community recipe submission portal is ready.
- **Custom fonts:** Bundle Noto Serif Devanagari/Gujarati for a richer multi-script experience.
- **Per-app locale API:** Migrate from custom `CompositionLocal<Language>` to Android 13+ `LocaleManager` once minSdk is raised.
- **Room auto-migration:** Add `@AutoMigration` annotations as the schema evolves.
- **Image loading:** Replace gradient placeholders with Coil once recipe images are available.
- **Community submissions:** Add a feature for users to submit and share their own sattvik recipes.

---

## 📄 Disclaimer

This app is an **independent cookbook** inspired by sattvik vegetarian cooking traditions.  
It is **not** an official BAPS Swaminarayan Sanstha application unless explicitly stated.
