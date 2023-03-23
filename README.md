<p align="center">
  <a href="https://appcenter.ms"><img alt="master build" src="https://build.appcenter.ms/v0.1/apps/5f1daf84-6cbd-46c3-83e1-8f0e8e6bf6fb/branches/master/badge"/></a>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" width="80" height="20">
  <a href="https://kotlinlang.org"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.8.x-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <img alt="MVVM" src="https://img.shields.io/badge/MVVM-Architecture-orange"/>
  <a href="https://developer.android.com/kotlin/coroutines"><img alt="Coroutines" src="https://img.shields.io/badge/Coroutines-Asynchronous-red"/></a>
</p>

<p align="center">
  <a href="https://github.com/jakubrzeznicki/FlashScoreCompose/releases"><img alt="Release" src="https://img.shields.io/badge/FlashScore_Compose-APK-blue.svg?style=for-the-badge&logo=android"/></a>
</p>

<p align="center">
  <img src="https://user-images.githubusercontent.com/31169206/227158482-5f4ccb5b-bf68-4f9f-b03a-a5f16357e783.png" width="200" height="200">
</p>

# FlashScore Compose
FlashScore Compose is a football live score app (made in Jetpack Compose).

## 🎮 App features

* List fixtures, live, favorite, played, upcoming
* Fixture details - statistics, line ups, head to head fixtures
* League details - filtering fixtures by date
* Standings - all, home, away
* Team details - information about club, list of players, fixtures,
* Player details - information about player
* Explore - possibility to search all data (e.g. teams, players, coaches, venues, leagues)
* Profile - information about profile, edit, delete account
* Sign In / Sign Up
* Onboarding - picking favorite teams and players
* Welcome
* Notifications


## 📷 Screenshots
![3.jpg](https://user-images.githubusercontent.com/31169206/227136308-15e13c88-851e-4316-a8da-a82ed17b1b32.jpg)
![6.jpg](https://user-images.githubusercontent.com/31169206/227137429-ad6ec9cf-b59c-4a14-b8bc-bd8806d40e09.jpg)
![4.jpg](https://user-images.githubusercontent.com/31169206/227136305-bbe123ab-cc0d-4188-8eb9-80481652d551.jpg)
![5.jpg](https://user-images.githubusercontent.com/31169206/227136306-3c49d5fc-4ed9-458e-bd62-a16bd1e2347c.jpg)

## 🎥 Preview
<p align="center">
  <img src="https://github.com/jakubrzeznicki/FlashScore-Assets/blob/master/record_1.gif">
  <img src="https://github.com/jakubrzeznicki/FlashScore-Assets/blob/master/record_2.gif">
</p>

## 🏛️ Architecture

FlashScore Compose is Multi-modular application with a meaningful separation for layers and features with the necessary grouping.
Modern architecture (Clean Architecture, Multi-Module setup, Model-View-ViewModel)

Modules Design:

* <b> app </b> - Brings everything together required for the app to function correctly. This includes UI Scaffolding and navigation.
* <b> build-src </b> - Managing and sharing dependencies between modules.
* <b> core </b>
    * <b> authentication </b> - Provides user authentication, to proper use the app by user.
    * <b> common </b> - Common classes and utils shared between modules
    * <b> data </b> - Fetching app data from multiple sources (remote and local), shared by different features.
    * <b> database </b> - Local database storage using Room.
    * <b> datastore </b> - Storing persistent data using DataStore.
    * <b> favoritefixtureinteractor </b> - Interactor containing logic to add or remove fixture from favorites.
    * <b> model </b> - Model classes used throughout the app.
    * <b> network </b> - Making network requests and handling responses from a remote data source.
    * <b> notificationservice </b> - Managing and handling notifications by manager and services.
    * <b> test </b> - Testing dependencies and  repositories
    * <b> ui </b> - UI components, composables and resources used by different features.
* <b> fixtures </b>
    * <b> explore </b> - Displays all data, such fixtures, leagues, teams and more. Give possibility to looking for data.
    * <b> fixturedetails </b> - Dispalys details about fixture such statistics, lineups, head to head fixtures, last fixtures.
    * <b> home </b> - First screen after login. Displays fixtures from current day.
    * <b> leaguedetails </b> - Displays details about league, give possibility to looking for fixtures by calendar.
    * <b> notifications </b> - Displays list of notifications that are in queue.
    * <b> onboarding </b> - Give possibility to pick favorite teams and players. Launch only through first logging.
    * <b> playerdetails </b> - Displays details about player.
    * <b> profile </b> - Displays information about user, such name, email, phone or address. Give possibility to change password, log out or even delete accout.
    * <b> signin </b> - Form to log in and create accout.
    * <b> standings </b> - Displays list of actual standings for specific leagues.
    * <b> standings </b> </b>details - Displays actual standing for selected league.
    * <b> teamdetails </b> - Displays details about team such informations, players, fixtuers.
    * <b> welcome </b> - Welcoming screen, give possibility to sign in, create accout or sign in as guest.

<p align="center">
  <img src="https://user-images.githubusercontent.com/31169206/227161910-a81e7e9e-da37-473a-95c7-526685b6167f.png">
</p>

## 🛠 Built With

* [Kotlin](https://kotlinlang.org/) - Programming language
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Best practices and recommended architecture for building robust, high-quality apps
* [Koin](https://insert-koin.io/) - Dependency injection library
* [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern toolkit for building native UI
* [Accompanist](https://github.com/google/accompanist) - Libraries that aim to supplement Jetpack Compose
* [Coil](https://coil-kt.github.io/coil/) - Image loading library
* [Retrofit](https://square.github.io/retrofit/) - Type-safe HTTP client for Android and Java
* [GSON](https://github.com/google/gson) - Convert Java Objects into their JSON representation
* [OkHttp](https://square.github.io/okhttp/) - HTTP client
* [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Design pattern for writing asynchronous programs for running multiple tasks concurrently
* [Flow](https://developer.android.com/kotlin/flow) - Stream of multiple, asynchronously computed values
* [Android Jetpack](https://developer.android.com/jetpack) - Libraries to help developers follow best practices, reduce boilerplate code
  * [Room](https://developer.android.com/jetpack/androidx/releases/room) - Store offline cache. Provides an abstraction layer over SQLite
  * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Perform an action when lifecycle state changes
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Business logic or screen level state holder
  * [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Data storage solution that allows you to store key-value pairs or typed objects
* [Material3](https://developer.android.com/jetpack/compose/designsystems/material3) - Design system
* [Firebase](https://firebase.google.com/) - App development platform
  * [Crashlytics](https://firebase.google.com/products/crashlytics) - Real time crash reporting tool
  * [Firebase Storage](https://firebase.google.com/docs/storage) - Storage for images and files
  * [Authentication](https://firebase.google.com/products/cloud-messaging) - Backend services, authenticate user to the app
* [Sheets Compose Dialog](https://github.com/maxkeppeler/sheets-compose-dialogs) - Sheets compose dialogs
* [Compose Destinations](https://github.com/raamcosta/compose-destinations)- Provides navigate between composables, hides the complex, boilerplate code
* [TOML](https://toml.io/en/) - Managing and sharing dependencies between modules

##  CI/CD

* [GitHub Actions](https://github.com/jakubrzeznicki/FlashScoreCompose/actions) - Continuous integration and continuous delivery (CI/CD) platform
* [Bitrise](https://bitrise.io/) - Continuous integration and continuous delivery (CI/CD) platform
* [CircleCI](https://circleci.com/) - Continuous integration and continuous delivery (CI/CD) platform
* [Fastlane](https://fastlane.tools/) - The easiest way to automate building and release iOS and Android apps
* [Firebase App Distribution](https://firebase.google.com/docs/app-distribution) - Distributing apps to trusted testers

##  TODO

* [ ] Unit test
* [ ] Ktlint / Detekt

##  API

FlashScore Compose uses the [API-FOOTBALL](https://rapidapi.com/api-sports/api/api-football) to access all required data.

API-FOOTBALL provides +960 football leagues & cups, livescore, line-ups, coachs, players, standings, statistics and more.

##  Design
Design was inspired by UI KIT on figma created by 🏀 Dribbble : [Odama](https://dribbble.com/odamastudio)

Link to figma: [Live Score UI KIT (FREEBIES) ⚽️](https://www.figma.com/community/file/936495139689782604)
