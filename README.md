# BitNews App

## Objective
The *BitNews App* is a mobile news portal designed to allow users to discover and read news articles from various sources. Users can save articles to their bookmarks, share links, and view articles in a web view. This project demonstrates the integration of various Android components and architectures, including MVVM, Hilt for dependency injection, and managing WebView functionality with added features like a loading overlay.

### Skills Learned

- Implementation of **WebView** to allow users to view news articles without leaving the app.
- **Bookmark Feature**: Ability to save favorite articles locally in the database.
- **Sharing Feature**: Sharing article links with other apps (messaging apps, social media).
- Integration of **MVVM Architecture** with Hilt for dependency injection.
- Implementation of a **Loading Overlay** during WebView loading processes.
- Building a **Search View** to allow users to search for news articles by keywords.
- Display of queried results on the home screen based on search input.
- Creation of an **Auto-Scrolling Banner** for the top headlines on the home screen.
- Error handling and UI feedback to improve user experience when articles fail to load.
- **Repository Pattern** for data fetching and management.
- Use of **Coroutines** for efficient handling of network requests and local database operations.
  
## Features

1. **WebView Integration**: Users can read news articles directly in the app via WebView with a loading overlay to improve UX.
2. **Bookmark Feature**: Users can save or remove articles from bookmarks. The saved articles are stored in the local database and accessible offline.
3. **Share Feature**: Users can share the link of the current news article to any messaging or social media app.
4. **Search Functionality**: A search view allows users to query articles based on keywords, and the results are displayed on the home screen.
5. **Auto-Scrolling Carousel Banner**: Displays top headlines on the home screen in a rotating carousel, with smooth transitions every 5 seconds.
6. **Error Handling and User Feedback**: When there is an issue with retrieving articles, the app provides clear error messages.
7. **Bottom Navigation**: Easy navigation between the news listing and bookmarked articles.
8. **Loading Overlay in WebView**: A visual indicator shows when the WebView is loading content, improving UX.

## Optional Features

1. **Dark Mode Support**: Adds theme management, allowing users to switch between light and dark modes.
2. **Improved Search Functionality**: Utilize search filters for narrowing down results by category, date, or source.
3. **Offline Access**: Cached news articles for offline reading.
4. **Push Notifications for New Articles**: Notifications alert users of breaking news articles.
5. **Localization Support**: Multi-language support for a broader audience.

## Screenshots

- **Home Screen with Auto-Scrolling Carousel**  
  <img src="https://github.com/Avwaveaf/screenshots/blob/main/bitnews_home.png" alt="Home Carousel" width="250"/>  
  *Auto-scrolling carousel of top headlines.*

- **WebView with Loading Overlay**  
  <img src="https://github.com/Avwaveaf/screenshots/blob/main/bitnews_webvbiew_loading_overlay.png" alt="WebView with Loading" width="250"/>  
  *Loading overlay displayed while WebView loads content.*

- **Search Feature**  
  <img src="https://github.com/Avwaveaf/screenshots/blob/main/bitnews_search_view.png" alt="Search Feature" width="250"/>
  <img src="https://github.com/Avwaveaf/screenshots/blob/main/bitnews_queried_news_screen_after_searchview.png" alt="Search Feature" width="250"/>  
  *Search bar with the result displayed on the home screen.*

- **Bookmark List**  
  <img src="https://github.com/Avwaveaf/screenshots/blob/main/bitnews_bookmark_view.png" alt="Bookmark List" width="250"/>  
  *A list of saved bookmarked articles.*

- **Article Detail with Share Button**  
  <img src="https://github.com/Avwaveaf/screenshots/blob/main/bitnews_webview_display%20with%20floating%20button.png" alt="Article Detail with Share Button" width="250"/>  
  *Share button allows users to share the article link with others.*

## Steps

1. Integrate **WebView** to allow users to read articles without leaving the app.
2. Implement **Bookmark Feature** for saving articles to a local database.
3. Add **Share Functionality** to allow users to share article links.
4. Build **Search View** to query news articles based on user input.
5. Add an **Auto-Scrolling Carousel Banner** for top headlines.
6. Implement **Loading Overlay** in WebView for improved user experience.
7. Utilize **MVVM Architecture** with Hilt for dependency injection and **Coroutines** for background tasks.
8. Implement **Error Handling** for network-related issues and provide appropriate user feedback.
9. Maintain **Bottom Navigation** for switching between the main news feed and bookmarked articles.

