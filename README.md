# Rewatch App

Rewatch is a video streaming application built using Jetpack Compose and Firebase. Inspired by YouTube, this app provides an engaging platform for users to stream, upload, like, comment on videos, and subscribe to channels. Developed as a final-year graduation project, it highlights modern Android development practices and Firebase integration.

## Features

- **Video Streaming**: Users can watch videos seamlessly with an intuitive and responsive UI.
- **Video Uploading**: Anyone can upload videos to the platform, making it a space for content creators.
- **Like, Comment, Subscribe**: Interact with videos by liking, commenting, and subscribing to channels.
- **Deep Link Integration**: NavDeepLink is implemented to provide direct navigation to specific content.
- **Firebase Backend**: Secure and scalable backend for authentication, storage, and real-time data updates.

## Tech Stack

- **Frontend**: Kotlin, Jetpack Compose
- **Backend**: Firebase Authentication, Firebase Firestore, Firebase Storage
- **Deep Linking**: NavDeepLink for seamless navigation

## Screenshots

<div style="display: flex; justify-content: space-around;">
  <img src="https://github.com/user-attachments/assets/5fe1e8a8-a818-4256-8c99-f9a09cf26c48" alt="Splash Screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/de09571b-bc2f-4bc7-998d-1e488402db78" alt="Main Screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/0ed3e56f-e20b-46e0-9cb8-32926f548784" alt="Splash Screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/479cfda6-f5d3-4cee-a59c-3e275bf8dad8" alt="Main Screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/fe1a8eae-0833-4373-a77c-be36d0e95094" alt="Main Screen" width="45%" />
</div>

## Installation

1. Clone this repository:
    ```bash
    git clone https://github.com/yourusername/Re-watchui.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle to download all dependencies.
4. Set up Firebase:
   - Add your `google-services.json` file to the `app/` directory.
   - Configure Firebase Authentication, Firestore, and Storage in your Firebase console.
5. Build and run the app on an emulator or physical device.

## Usage

- **Stream Videos**: Open the app and browse through the available videos.
- **Upload Videos**: Navigate to the upload section, select a video file, and submit it.
- **Interact**: Like videos, leave comments, and subscribe to your favorite channels.
- **Deep Linking**: Use specific URLs to navigate directly to video content.

## Project Structure

- `ui/`: Contains all Jetpack Compose UI components.
- `data/`: Manages Firebase integration for authentication, Firestore, and Storage.
- `navigation/`: Handles navigation and deep linking logic.
- `viewmodel/`: Contains ViewModel classes for state management.

## Future Enhancements

- Add video recommendations based on user preferences.
- Implement video categories and tags for better discoverability.
- Enable live streaming capabilities.
- Optimize video upload and streaming performance.

## Credits

This project was created by **Sahil Karwasra** as part of the final-year graduation project. Special thanks to the mentors and peers who provided guidance and feedback.

## License

This project is licensed under the [MIT License](LICENSE).

---

Feel free to contribute to this project by reporting issues or submitting pull requests!
