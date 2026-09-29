# Implementation Plan - SplitStay App

SplitStay is an expense and chore splitter designed for PG/hostel roommates, addressing common frictions like shared payments, chore tracking, and resource management.

## User Review Required

> [!IMPORTANT]
> The app requires several external integrations (Firebase, Google Maps, API keys). I will set up the code structure and placeholders, but you will need to provide your own API keys for production use.

> [!NOTE]
> We will use a **Single Activity with Fragments** architecture using the Jetpack Navigation Component for a modern, efficient UI flow.

## Proposed Changes

### 1. Build & Dependencies
Update `libs.versions.toml` and `build.gradle.kts` to include:
- **Room**: For the offline expense ledger.
- **Firebase**: For real-time sync and notifications.
- **Retrofit**: For network finance API calls.
- **Google Maps SDK**: For finding grocery stores.
- **CameraX + Location Services**: For geo-tagged chore proof.
- **Navigation Component**: For UI flow.

---

### 2. UI Layout Design

#### [MODIFY] [activity_main.xml](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/res/layout/activity_main.xml)
- Implement `BottomNavigationView` for navigation between Dashboard, Expenses, Chores, and Tools.
- Add a `FragmentContainerView` for hosting screen fragments.

#### [NEW] Fragments for main tabs:
- **DashboardFragment**: Summary cards for "Total Balance", "Next Chore", and "Recent Activity".
- **ExpenseListFragment**: RecyclerView for expenses with a FAB to "Add Expense".
- **ChoreTrackerFragment**: List of chores with "Mark Done" action (triggers Camera/GPS).
- **ToolsFragment**: Entry points for Maps (Grocery), Chatbot, and ML Expense Prediction.

---

### 3. Data Layer (SQLite & Firebase)

#### [NEW] [ExpenseDao.kt](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/java/com/example/splistay/data/local/ExpenseDao.kt) & [AppDatabase.kt](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/java/com/example/splistay/data/local/AppDatabase.kt)
- Define the Room database for local expense persistence.

#### [NEW] [FirebaseSyncManager.kt](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/java/com/example/splistay/data/remote/FirebaseSyncManager.kt)
- Logic to push local SQLite changes to Firebase Realtime Database/Firestore.

---

### 4. Specialized Features

#### [NEW] [ChoreProofActivity.kt](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/java/com/example/splistay/ui/chores/ChoreProofActivity.kt)
- Handles photo capture using CameraX and appends GPS coordinates for verification.

#### [NEW] [GroceryMapFragment.kt](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/java/com/example/splistay/ui/tools/GroceryMapFragment.kt)
- Integrates Google Maps to show nearby "grocery_or_supermarket" places.

#### [NEW] [PredictionWorker.kt](file:///C:/Users/Asus/AndroidStudioProjects/splistay/app/src/main/java/com/example/splistay/ml/PredictionWorker.kt)
- Uses WorkManager to periodically analyze expense patterns and suggest advance contributions.

## Verification Plan

### Automated Tests
- Room database migration and CRUD tests.
- ViewModel unit tests for split calculations.

### Manual Verification
- Deploy to an emulator/device to verify the Bottom Navigation flow.
- Mock location/camera to test chore proof completion.
