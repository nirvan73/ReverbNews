# ReverbNews

**ReverbNews** is an intelligent, AI-powered news application designed for modern Android devices. Built using Jetpack Compose and Kotlin, it delivers personalized and real-time news articles based on user-friendly, natural language queries. With offline bookmarking, clean in-app reading, and an adaptive, sleek UI, it offers a seamless and engaging reading experience based on your interests.


<p align="center">
  <img src="https://github.com/user-attachments/assets/358d7842-c1e8-42ce-9c70-037f665cb781" width="250" alt="ReverbNews Logo"/>
</p>

---

## ✨ Features

- 🔍 **Gemini-powered Smart Search**: Search news using natural queries like _"latest tech news in India"_.
- 📰 **Top Headlines**: Stay up-to-date with category-wise breaking news.
- 💾 **Offline Bookmarks and Favorites**: Save and manage favorite articles offline using Room.
- 🌐 **In-App WebView**: Open full news articles within the app—no external browser needed.
- 📷 **Fast Image Loading**: Optimized news article thumbnails using Coil.
- 📱 **Adaptive Layouts**: Smooth experience on all screen sizes.
- 🔗 **Easy Sharing**: Share articles via link.
- ⚡ **Clean MVVM Architecture**: Maintainable and scalable project structure.

---

## 🛠️ Tech Stack

| Technology / Library       | Description |
|----------------------------|-------------|
| **Kotlin**                 | Primary language used for Android development. |
| **Jetpack Compose**        | Modern declarative UI toolkit to build native Android UI with less code. |
| **Hilt**                   | Dependency Injection framework to simplify dependency management in the MVVM architecture. |
| **Retrofit**               | Type-safe HTTP client used for making network requests to the GNews API. |
| **Room Database**          | Local database for storing bookmarked and favourite articles and enabling offline access. |
| **WebView**                | Opens full news articles inside the app using an embedded browser experience. |
| **Coil**                   | Fast, lightweight image loading library with support for image caching. |
| **Gemini API (Google AI)** | Converts user-friendly queries into smart keywords to improve news search results. |
| **Adaptive Layouts**       | Ensures responsive and consistent UI across various screen sizes and devices. |
| **Lottie Animation**       | Integrates vector-based animations to enhance user experience and feedback. |

---

## 🤖 AI-Powered Smart Search

Using the [Gemini API](https://deepmind.google/technologies/gemini/), users can type natural language queries like:

> *"What are the top sports headlines this weekend?"*

Gemini intelligently processes the input and transforms it into optimized search keywords to fetch accurate news results.

---

## 🎬 Demo Video

<a href="https://www.youtube.com/watch?v=IWVVz1S2bdE">
  <img src="https://github.com/user-attachments/assets/2ff569c1-2811-4c5d-9eef-554db389a321" style="width:100%;" alt="Watch Demo"/>
</a>

---

## 🚀 Setup

Setup and run ReverbNews app on your local machine by following these steps:-

1. **Clone the repository**:

   ```bash
   git clone https://github.com/nirvan73/ReverbNews.git

### 2. Open the Project in [Android Studio](https://developer.android.com/studio)

- Launch **Android Studio**.
- Go to **File > Open**.
- Navigate to the `ReverbNews` folder.
- Click **OK** to open the project.
- Let **Gradle** sync and finish building the project.

### 3. Add API Keys

To enable news search and AI features:

- Get your **GNews API Key** from [GNews](https://gnews.io).
- Get your **Gemini API Key** from [Google Gemini](https://makersuite.google.com/app).
- Open your `local.properties` file and add the following:

```properties
GNEWS_API_KEY=your_gnews_api_key
GEMINI_API_KEY=your_gemini_api_key
```
- Build and run the project in your Android Device locally or on emulator.
---

## 📝 License

This project is licensed under the [MIT License](LICENSE) – see the LICENSE file for details.

