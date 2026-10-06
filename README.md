# 📚 Books App

A modern Android books browser built with **Clean Architecture**, **MVI**, and a fully **multi-module** Gradle project structure. It lets users browse, search, favourite, and read details about books — all with offline-first support, adaptive theming, and localisation.

---

## Table of Contents

- [Screenshots](#screenshots)
- [Architecture Overview](#architecture-overview)
- [Module Graph](#module-graph)
- [Module Breakdown](#module-breakdown)
  - [app](#app)
  - [core modules](#core-modules)
  - [common modules](#common-modules)
  - [feature modules](#feature-modules)
- [MVI Pattern](#mvi-pattern)
- [Navigation](#navigation)
- [Dependency Injection](#dependency-injection)
- [Networking](#networking)
- [Database & Local Storage](#database--local-storage)
- [Background Sync](#background-sync)
- [Build Logic & Convention Plugins](#build-logic--convention-plugins)
- [Static Analysis](#static-analysis)
- [CI/CD](#cicd)
- [Tech Stack](#tech-stack)
- [Project Setup](#project-setup)

---

## Architecture Overview

The project follows **Clean Architecture** principles with a strict three-layer separation inside every feature:

```
Presentation  ──►  Domain  ◄──  Data
     │                │            │
 ViewModel        UseCases     Repository (impl)
 UI State         Entities     Remote / Local
 Intents          Outcome      API / Room
```

Key architectural decisions:

- **Offline-first** — Room is the single source of truth; network data flows through a `RemoteMediator` into the local database before reaching the UI.
- **MVI** — every screen has a `State`, `Intent`, and `Effect`; state transitions happen through a pure `Reducer` function.
- **Contract/Provider pattern** — cross-feature dependencies are expressed as interfaces in `core:contract`, so no feature module depends directly on another.
- **Plugin-based navigation** — each feature registers its own routes via a `FeatureEntryProvider` SAM interface; the app module collects them all at runtime through Koin.

---

## Module Graph

```
app
 ├── core:networking
 ├── core:database
 ├── core:navigation
 ├── core:design-system
 ├── core:contract
 ├── common:data
 ├── common:presentation
 ├── feature:home:presentation / domain / data
 ├── feature:search:presentation / domain / data
 ├── feature:favourites:presentation / domain / data
 ├── feature:settings:presentation / domain / data
 └── feature:book-details:presentation / domain

common:domain        ← pure Kotlin, no Android
common:presentation  ← depends on common:domain
common:data          ← depends on core:networking + core:database

feature:*:domain      ← depends on common:domain + core:contract
feature:*:data        ← depends on feature:*:domain + core:networking + core:database
feature:*:presentation← depends on feature:*:domain + common:presentation + core:design-system + core:navigation + core:contract
```

Dependency rule: features never depend on other features. All cross-cutting communication goes through `core:contract` interfaces.

---

## Module Breakdown

### `app`

The shell module that wires everything together. Nothing business-logic lives here.

| File | Responsibility |
|---|---|
| `BooksApplication` | Initialises Koin, registers WorkManager factory, schedules periodic sync |
| `MainActivity` | Edge-to-edge setup, sets `BookAppRoot` as content |
| `BooksAppRoot` | Root Composable: theme wrapper, bottom navigation bar, `NavDisplay` host, `SharedTransitionLayout` |
| `NavTransitions` | Custom enter / pop / predictive-pop animation specs for Nav3 |
| `AppModule` / `allModules` | Assembles the 18 Koin modules from every layer |
| `SyncWorker` | `CoroutineWorker` that calls `bookRepository.refresh()` and retries on failure |
| `SyncScheduler` | Schedules `SyncWorker` as a periodic `WorkManager` task |

---

### Core Modules

#### `core:networking`

Ktor `HttpClient` factory backed by the OkHttp engine.

- Bearer token auth via the Ktor `Auth` plugin + `x-api-key` header
- Content negotiation with `kotlinx-serialization-json` (lenient, coerce input values, ignore unknown keys)
- `HttpRequestRetry` — up to 2 retries on server errors with exponential backoff
- Timeouts: 30 s request / 15 s connect / 30 s socket
- `AndroidHttpLogger` with sanitised `Authorization` header in logs
- Base URL and API key injected from `local.properties` → `BuildConfig`

#### `core:database`

Room 2.8.x with KSP2-generated Kotlin code.

| Entity | Table | Purpose |
|---|---|---|
| `BookEntity` | `books` | Cached book data; stores page + position for paging order |
| `BookRemoteKeyEntity` | `book_remote_keys` | Paging 3 `RemoteMediator` cursor tracking |
| `FavoriteBookEntity` | `favorite_books` | Locally-favourited books; `pendingSync` flag for offline queue |

`BookConverters` serialises `List<AuthorEntity>` to/from JSON via a Room `TypeConverter`. Schema is exported to `/schemas` for migration tests.

#### `core:navigation`

- Typed `@Serializable` `NavKey` destinations: `Home`, `Search`, `Favourites`, `Settings`, `BookDetails(bookId)`, `BookList`
- `FeatureEntryProvider` (`fun interface`) — each feature implements this to register its own routes; the app collects them at runtime via `getKoin().getAll<FeatureEntryProvider>()`
- `LocalSharedTransitionScope` composition local + `sharedBookElement()` modifier for shared-element transitions between list and detail

#### `core:contract`

Pure Kotlin interfaces for cross-feature communication:

```kotlin
interface BooksProvider {
    suspend fun getBook(id: Int): Outcome<BookSummary>
    fun observeBook(id: Int): Flow<BookSummary?>
}

interface FavoritesProvider {
    fun observeFavoriteIds(): Flow<Set<Int>>
    suspend fun toggleFavorite(bookId: Int)
}

interface UserPreferencesRepository {
    val preferences: Flow<UserPreferences>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setDynamicColor(value: DynamicColor)
    suspend fun setLanguage(language: AppLanguage)
}
```

#### `core:design-system`

Compose theme (`BookTheme`) with `ThemeMode` (Light / Dark / System) and `DynamicColor` support, shared UI components, and typography.

---

### Common Modules

#### `common:domain`

Pure Kotlin — zero Android dependencies.

- `UseCase<P, R>` and `FlowUseCase<P, R>` interfaces
- `Outcome<T>` sealed result type:

```kotlin
sealed interface Outcome<out T> {
    data class Success<out T>(val data: T) : Outcome<T>
    data class Failure(val error: AppError)  : Outcome<Nothing>
}
```

Extension functions: `map`, `onSuccess`, `onFailure`, `getOrNull`, `asSuccess`, `asFailure`.

- `AppError` sealed hierarchy: `Network`, `Timeout`, `Unauthorized`, `NotFound`, `Http(code)`, `Database`, `Unknown(cause)`
- `ConnectivityObserver` interface + `NetworkStatus` enum

#### `common:data`

Shared data-layer utilities (mappers, base `RemoteMediator` helpers, common data sources) reused across feature data modules.

#### `common:presentation`

MVI base infrastructure (see [MVI Pattern](#mvi-pattern)), `ResourceProvider` abstraction, error-to-string mappers, and string resources in **English** and **Arabic (RTL)**.

---

### Feature Modules

Each feature is split into three sub-modules following Clean Architecture:

| Feature | Screens | Notes |
|---|---|---|
| **home** | Book feed with paged list | Paging 3 + `RemoteMediator`; favorites overlay; connectivity banner |
| **search** | Search results with paging | Full-text search via API; results cached in Room |
| **favourites** | Saved books list | Local-only; reads from `favorite_books` table |
| **settings** | Theme, dynamic color, language | DataStore Preferences; changes reflected immediately in UI |
| **book-details** | Single book detail view | No `data` sub-module; reads via `BooksProvider` contract; shared-element poster transition |

---

## MVI Pattern

Implemented in `common:presentation` and applied consistently across every feature.

```
User Action
    │
    ▼
 Intent  ──►  Reducer (pure function)  ──►  State  ──►  UI
                      │
                  ViewModel
                      │
                  Side Effect  ──►  one-shot events (snackbar, navigation)
```

**Core contracts:**

```kotlin
interface UiState
interface Intent
interface Effect
fun interface Reducer<S : UiState, I : Intent> {
    fun reduce(state: S, intent: I): S
}
```

**`BaseViewModel<S, I, E>`:**

- `StateFlow<S>` exposed as `state` — UI subscribes with `collectAsStateWithLifecycle`
- `Channel<E>(BUFFERED)` exposed as `effect` — one-shot side effects that survive recomposition
- `sendIntent(intent)` — runs the reducer synchronously, then calls `handleIntent` for async work
- `sendEffect(effect)` — sends to the buffered channel from a coroutine

**Example — Home feature:**

```
HomeState    — isRefreshing, isOffline, fatalError
HomeIntent   — Load, Refresh, Retry, OpenDetails, ToggleFavorite, ConnectivityChanged, RefreshFinished
HomeEffect   — NavigateToDetails(bookId)
HomeReducer  — pure (state, intent) → state transitions
HomeViewModel— orchestrates use cases, emits effects
```

---

## Navigation

Uses **Jetpack Navigation 3 (nav3 1.1.2)** — type-safe, back-stack-first navigation:

- Destinations are `@Serializable data object` / `data class` values implementing `NavKey`
- `rememberNavBackStack(Home)` manages the stack; `NavDisplay` renders the current entry
- Bottom navigation clears the back stack and pushes the selected destination
- Features register routes via the `FeatureEntryProvider` plugin pattern — the app has zero direct screen references
- Custom transitions: slide + fade on forward navigation, scale-out on predictive pop
- Shared-element transitions between book list items and the detail screen via `SharedTransitionLayout`

---

## Dependency Injection

**Koin 4.2.1** (BOM-managed):

```kotlin
// BooksApplication.onCreate()
startKoin {
    androidContext(this@BooksApplication)
    workManagerFactory()
    modules(allModules)   // 18 modules assembled in AppModule.kt
}
```

Each layer owns its own Koin module file. The `KoinConventionPlugin` automatically adds `koin-core`, `koin-android`, and `koin-test` to every module that opts in. `FeatureEntryProvider` instances are registered per feature and collected at navigation setup time via `getKoin().getAll<FeatureEntryProvider>()`.

---

## Networking

**Ktor 3.5.0** + OkHttp engine:

| Setting | Value |
|---|---|
| Base URL | `https://api.bigbookapi.com/` |
| Auth | Bearer token + `x-api-key` header (from `local.properties`) |
| Serialization | `kotlinx-serialization-json` (lenient) |
| Retry | 2 retries, exponential backoff |
| Timeouts | 30 s request, 15 s connect, 30 s socket |
| Logging | Full, Authorization header sanitised |

---

## Database & Local Storage

### Room 2.8.x

- KSP2 generates Kotlin code (`room.generateKotlin = true`)
- Schema exported for migration tests in `core/database/schemas/`
- Paging 3 `RemoteMediator` pattern via `BookRemoteKeyEntity`
- Favorites have a `pendingSync` flag for offline-first sync via WorkManager

### DataStore Preferences

Used by `feature:settings:data` to persist:

```kotlin
data class UserPreferences(
    val themeMode: ThemeMode,       // LIGHT | DARK | SYSTEM
    val dynamicColor: DynamicColor, // ENABLED | DISABLED
    val language: AppLanguage,      // ENGLISH | ARABIC
)
```

---

## Background Sync

`SyncWorker` is a `CoroutineWorker` injected by Koin via the WorkManager factory:

- Calls `bookRepository.refresh()` on execution
- Returns `Result.success()` on `Outcome.Success`, `Result.retry()` on `Outcome.Failure`
- `SyncScheduler.schedulePeriodicSync()` is called from `Application.onCreate()` to keep local data fresh

---

## Build Logic & Convention Plugins

Convention plugins live in `build-logic/convention/` and are applied across modules by ID:

| Plugin | ID | What it does |
|---|---|---|
| `AndroidApplicationConventionPlugin` | `hyperdesign.android.application` | `com.android.application` + Kotlin; SDK levels from `KotlinAndroid.kt` |
| `AndroidLibraryConventionPlugin` | `hyperdesign.android.library` | `com.android.library` + Kotlin; auto-adds JUnit 5 / Turbine / MockK / coroutines-test |
| `AndroidComposeConventionPlugin` | `hyperdesign.android.compose` | Kotlin Compose plugin + full Compose BOM stack |
| `AndroidFeatureConventionPlugin` | `hyperdesign.android.feature` | Composes `library` + `compose` + `koin`; auto-adds Nav3, Paging Compose, Coil, common/core deps |
| `KotlinLibraryConventionPlugin` | `hyperdesign.kotlin.library` | Pure JVM lib; coroutines + JUnit 5 / Turbine / MockK |
| `AndroidRoomConventionPlugin` | `hyperdesign.android.room` | Room + KSP plugins; schema dir; Kotlin code gen; all Room deps |
| `KoinConventionPlugin` | `hyperdesign.koin` | Koin BOM + core + android + test |

**Global SDK constants** (`KotlinAndroid.kt`): `compileSdk = 36`, `minSdk = 24`, `targetSdk = 36`, Java 17, opt-in for `RequiresOptIn` + `ExperimentalCoroutinesApi`.

---

## Static Analysis

Applied to every subproject via the root `build.gradle.kts`:

| Tool | Version | Config |
|---|---|---|
| **Detekt** | 1.23.8 | `config/detekt/detekt.yml` + `baseline.xml`; JVM target 17; parallel execution |
| **KtLint** | 12.1.2 | Android mode enabled |

Run manually:

```bash
./gradlew ktlintCheck detekt
# auto-fix formatting
./gradlew ktlintFormat
```

---

## CI/CD

GitHub Actions workflow (`.github/workflows/ci.yml`) triggers on push and PR to `main` / `master` with concurrency cancel-in-progress.

### `build` job (ubuntu-latest, JDK 17 Temurin)

1. Static analysis — `ktlintCheck` + `detekt`
2. Unit tests — `testDebugUnitTest`
3. Assemble debug APK — `:app:assembleDebug`
4. Upload test report artifacts (always, even on failure)

### `instrumented` job (ubuntu-latest, KVM-enabled)

- Room migration / DAO tests on API 30 x86_64 emulator
- Uses `reactivecircus/android-emulator-runner@v2`
- Runs `:core:database:connectedDebugAndroidTest`

---

## Tech Stack

| Category | Library | Version |
|---|---|---|
| Language | Kotlin | 2.4.0 |
| Build | AGP | 9.1.0 |
| Build | KSP2 | 2.3.7 |
| UI | Jetpack Compose BOM | 2026.05.01 |
| UI | Material 3 | 1.4.0 |
| UI | Material 3 Adaptive | 1.2.0 |
| Navigation | Navigation 3 (nav3) | 1.1.2 |
| Async | Kotlin Coroutines | 1.11.0 |
| DI | Koin BOM | 4.2.1 |
| Networking | Ktor (OkHttp engine) | 3.5.0 |
| Serialization | kotlinx-serialization-json | 1.11.0 |
| Database | Room | 2.8.4 |
| Preferences | DataStore Preferences | 1.2.1 |
| Paging | Paging 3 + Compose | 3.5.0 |
| Background | WorkManager | 2.11.2 |
| Images | Coil 3 | 3.4.0 |
| Testing | JUnit Jupiter BOM | 6.1.0 |
| Testing | Turbine | 1.2.1 |
| Testing | MockK | 1.14.11 |
| Testing | kotlinx-coroutines-test | 1.11.0 |
| Static Analysis | Detekt | 1.23.8 |
| Static Analysis | KtLint | 12.1.2 |

---

## Project Setup

### Prerequisites

- Android Studio Meerkat or newer
- JDK 17
- An API key from [BigBook API](https://bigbookapi.com)

### Configuration

Add your API credentials to `local.properties` (never commit this file):

```properties
BOOKS_BASE_URL=https://api.bigbookapi.com/
BOOKS_ACCESS_TOKEN=your_api_key_here
```

### Build & Run

```bash
# Clone
git clone https://github.com/your-username/Books_app.git

# Debug build
./gradlew :app:assembleDebug

# Unit tests
./gradlew testDebugUnitTest

# Static analysis
./gradlew ktlintCheck detekt

# Room migration tests (requires emulator)
./gradlew :core:database:connectedDebugAndroidTest
```
