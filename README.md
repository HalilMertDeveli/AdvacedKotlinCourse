# BesinlerKitabiGradleWork

This repository is a **Kotlin learning project** built during the Atil Samancioglu Android course.  
Its main goal is to teach how core Android components work together in a real mini app flow (MVVM, Room, Retrofit, Navigation, RecyclerView, and LiveData).

The app fetches food data from the internet, stores it locally, and displays it on list/detail screens.

## Why This Project Matters

This project is more than a static UI demo. It combines the most common Android building blocks in one place:

- Fetching data from a remote API
- Caching data with Room
- Managing UI state with ViewModel + LiveData
- Safe screen-to-screen argument passing (Safe Args)
- Efficient list rendering with RecyclerView
- Remote image loading with Glide

In short, this repository is a practical stepping stone from beginner-level Android to intermediate-level app structure.

## Screenshots

### Food List Screen

![Food list screen](docs/images/list-screen.png)

### Food Detail Screen

![Food detail screen](docs/images/detail-screen.png)

## App Features

- Fetches a food list from a remote JSON source
- Saves fetched data into Room database
- Shows food name, calorie, and image in a list
- Navigates to detail screen on item click
- Shows detailed nutritional values on the detail page
- Supports manual refresh with swipe-to-refresh

## Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM
- **UI Layer:** Fragment, RecyclerView, ViewBinding, DataBinding
- **State Management:** LiveData, ViewModel
- **Network Layer:** Retrofit, Gson Converter
- **Async:** RxJava2 + Coroutines
- **Local Storage:** Room
- **Navigation:** Navigation Component + Safe Args
- **Image Loading:** Glide
- **Build:** AGP 8.2.2, Gradle 8.5, Kotlin 1.9.22
- **JVM Target:** Java 17

## Detailed Architecture Walkthrough

### 1) `model` layer

The `Food` class is designed to be used for both API parsing and local persistence.

- `@SerializedName` maps API JSON fields to Kotlin properties.
- `@Entity` and `@PrimaryKey(autoGenerate = true)` define the Room table schema.
- Using one model for remote + local data keeps learning flow simple and clear.

### 2) `service` layer

This layer has two responsibilities:

1. **Remote data access (Retrofit)**
   - `FoodApi`: endpoint definition
   - `FoodApiService`: Retrofit setup and API invocation
2. **Local data access (Room)**
   - `FoodDAO`: insert, list, fetch-one, delete operations
   - `FoodDatabase`: singleton Room database initialization

This separation reduces coupling between UI and data source details.

### 3) `viewModel` layer

ViewModels hold UI state and prepare data for screens:

- `FoodListViewModel`
  - Fetches data from network
  - Saves data to Room
  - Updates `foodList`, `foodErrorMessage`, and `foodIsLoading`
- `FoodDetailViewModel`
  - Reads a single item from Room using `uuid`
  - Exposes result through `foodLiveData`
- `BaseViewModel`
  - Provides coroutine scope + lifecycle cleanup base behavior

Because of this structure, Fragments focus on rendering UI while business/data logic stays in ViewModel classes.

### 4) `view` layer

- `BesinListesiFragment`
  - Configures RecyclerView
  - Handles swipe refresh event
  - Observes LiveData for loading/error/success states
- `BesinDetayiFragment`
  - Receives `besinId` via Safe Args
  - Asks ViewModel for selected item
  - Binds text and image to the UI
- `MainActivity`
  - Hosts the NavHost container

### 5) `adapter` layer

`FoodRecyclerAdapter` handles list row rendering:

- Binds row UI with ViewBinding
- Loads image using Glide extension helpers
- Navigates safely via `BesinListesiFragmentDirections` on row click

### 6) `util` layer

- `downloadImage` and `makePlaceHolder` keep image-loading code reusable and clean.
- `PrivateSharedPreferences` stores timestamp data as a foundation for cache-time logic.

## End-to-End Data Flow

1. User opens the list screen.
2. `refreshData()` is triggered.
3. Retrofit fetches JSON data.
4. Result is written into Room (`deleteAllFood` + `insertAll`).
5. Inserted row IDs are assigned back to each model's `uuid`.
6. `foodList` LiveData is updated.
7. RecyclerView redraws with fresh data.
8. User taps one item.
9. `uuid` is sent to detail screen via Safe Args.
10. Detail screen reads the selected row from Room and renders it.

## Project Structure

```text
BTKAdvacedKotlinCourse/
├─ app/
│  ├─ src/main/java/com/atilsamancioglu/besinlerkitabigradlework/
│  │  ├─ adapter/
│  │  ├─ model/
│  │  ├─ service/
│  │  ├─ util/
│  │  ├─ view/
│  │  └─ viewModel/
│  └─ src/main/res/
├─ docs/images/
│  ├─ list-screen.png
│  └─ detail-screen.png
└─ README.md
```

## Setup and Run

### Requirements

- Android Studio (latest recommended)
- JDK 17 or newer
- Android SDK (compileSdk 34)

### Clone

```bash
git clone https://github.com/HalilMertDeveli/BTKAdvacedKotlinCourse.git
```

Then open the project in Android Studio and run it after Gradle sync completes.

### Build Commands

Linux/macOS:

```bash
./gradlew build
```

Windows:

```powershell
.\gradlew.bat build
```

## Verification Status

Build verification was executed and the project **builds successfully**.  
The screenshots in this README were captured from the running app.

## What You Learn From This Project

This repository helps you practice:

- Layered thinking with MVVM
- UI state handling (loading/error/success)
- Combining API and local database workflows
- Navigation graph and argument passing
- Fragment lifecycle + binding patterns
- Async flow and thread handling in Android

By the end of the learning process, this project provides a strong base before moving to larger-scale Android apps.

## Suggested Next Improvements

- Standardize async approach (choose RxJava or Coroutines consistently)
- Add a Repository layer for better testability
- Ensure `CompositeDisposable` is always cleared in `onCleared`
- Move all hardcoded texts fully into `strings.xml`
- Improve user-facing error feedback and retry handling

## Source Repository

GitHub repository:  
[HalilMertDeveli/BTKAdvacedKotlinCourse](https://github.com/HalilMertDeveli/BTKAdvacedKotlinCourse.git)
