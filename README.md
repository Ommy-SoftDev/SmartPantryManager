# Smart Pantry Manager

Module: Mobile App Development 700
Student: Makhukho Ommy Mahasha, 402104334

## Description
Smart Pantry Manager is a Java Android app that helps reduce food waste. The user
records the ingredients they have at home, and the app suggests only the recipes
they can cook right now, with no shopping trip needed. The recipe collection is
built around home-style South African cooking as well as simple everyday meals.

## Database choice
I used SQLite through the Room persistence library. I chose it because all the
data belongs to one user on one phone, so no server, account or internet
connection is needed. Room checks queries at compile time and removes a lot of
repetitive code, and the data is saved on the device, so it is still there after
the app is closed and reopened.

## Features
- Add, edit, delete and list pantry items (name, quantity, unit, optional expiry date)
- 16 pre-loaded recipes, seeded on the first run
- Strict matching: a recipe is suggested only if every ingredient is in the pantry
  in at least the required quantity (handles plurals, capital letters and
  kg/g and l/ml conversions)
- Message shown when no recipes match
- Recipe detail screen with the full ingredient list and method
- Settings screen (name for the greeting, expiring-soon warning on/off)
- Input validation on the add/edit form

## Built with
Java, Android Studio, Room (SQLite), RecyclerView with custom adapters, Intents,
SharedPreferences and Material components. Minimum Android version: API 24.

## How to set up and run
1. Install Android Studio and clone or download this repository.
2. Open the project folder in Android Studio and wait for Gradle to finish syncing.
3. Create an emulator in Tools > Device Manager, or connect an Android phone with
   USB debugging turned on.
4. Click the green Run button.
5. On the first run the app loads the recipes into the database automatically.

## How to test the strict-matching rule
1. Add bread, 2, pcs. Open "What Can I Cook?" and note that Buttered Bread does NOT appear.
2. Add butter, 10, g. Open "What Can I Cook?" again and Buttered Bread now appears.
3. Delete the butter and the recipe disappears again.
