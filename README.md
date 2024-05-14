# Tugas Besar Pengembangan Aplikasi Mobile

### Anggota Kelompok 1:
- GIOVANNI LUCY FAUSTINE SITOMPUL (121140060)
- MARSELLA YESI NATALIA SINAGA (121140174)
- FERDINAND ZULVAN LINDAN	(121140170)
- Muhammad Ihsanudin Faruq (121140214)

## Movie Discovery
A simple project using [The Movie DB](https://www.themoviedb.org) based on Kotlin MVVM architecture. <br>
Movie Discovery is an Android application that allows users to discover movies, view details, and manage their favorite movies. <br>
This app leverages The Movie Database (TMDb) API to fetch movie data and provides an intuitive and interactive user experience. <br>

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Demo Video](#demo-video)
- [Figma](#figma)

## Features
- **Movie Discovery**: Search and discover movies from TMDb.
- **Movie Details**: View detailed information about movies, including title, release date, rating, and overview.
- **Favorite Movies**: Add movies to your favorite list and manage them.
- **User Authentication**: Sign in and manage user profile.
- **Interactive UI**: Smooth and interactive UI with animations.

## Tech Stack
- **Kotlin**: Primary language for Android development.
- **SDK Target**: 34
- **Retrofit**: For making HTTP requests to the TMDb API.
- **Coroutines**: For managing background threads, making network calls, and handling data asynchronously.
- **Koin**: Dependency Injection framework.
- **Jetpack Components**: Including LiveData, ViewModel, Navigation Component, DataStore, and more.
- **Material Design**: For designing a modern and intuitive UI.
- **MockWebServer**: For testing HTTP requests.

## Installation
To set up the project locally, follow these steps:
1. **Clone the repository**:
    ```bash
    git clone https://github.com/Giovanni121140060/TugasbesarPAM.git
    ```

2. **Open the project**: Open the project in Android Studio.

3. **Get an API Key**: Sign up for an API key from [The Movie Database (TMDb)](https://www.themoviedb.org/documentation/api).

4. **Add API Key**: Create a `local.properties` file in the root directory and add your TMDb API key:
    ```
    TMDB_API_KEY=your_api_key
    ```

5. **Build the project**: Sync and build the project in Android Studio.

## Usage
- **Home Screen**: Browse popular and top-rated movies.
- **Search**: Use the search bar to find specific movies.
- **Movie Details**: Click on any movie to view its detailed information.
- **Favorites**: Add movies to your favorite list and access them from the favorites section.
- **Profile Management**: Sign in to manage your profile and view your favorite movies.

## Demo Video
Watch a video demonstration of the Movie Discovery app on YouTube: <br>
[![Movie Discovery App Video](https://img.youtube.com/vi/btew0reJ_9o/0.jpg)](https://youtu.be/btew0reJ_9o)

## Figma
To see the wireframe and prototype of the project, click the link below: <br>
https://www.figma.com/file/WOl1icQWkO3Uq7kd7CCmKJ/Untitled?type=design&node-id=0%3A1&mode=design&t=KL0LgU2NH4bfBjFc-1
