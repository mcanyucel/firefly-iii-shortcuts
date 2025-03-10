# Firefly III Shortcuts

A modern Android application that simplifies daily financial transactions
with [Firefly III](https://www.firefly-iii.org/), the popular open-source personal finance manager.

## Features

- **Quick Transaction Shortcuts**: Create and use shortcuts for your common financial transactions
- **Offline Support**: Full offline capabilities with local data caching
- **Clean UI**: Modern Material 3 design with intuitive navigation
- **Secure**: Your financial data stays on your device with secure API token storage
- **Fast Synchronization**: Efficient data syncing with Firefly III server

## Screenshots

*TBA*

## Installation

### Google Play

*TBA*

### Manual Installation

1. Download the latest APK from
   the [Releases](https://github.com/mustafacanyucel/fireflyiii-shortcuts/releases) page
2. Enable installation from unknown sources in your device settings
3. Install the downloaded APK

## Setup

1. **Firefly III Server**: You need a working Firefly III instance. Self-host or use a hosted
   option.
2. **API Token**: Generate a Personal Access Token in your Firefly III instance.
3. **Configure the App**:
    - Enter your Firefly III server URL
    - Add your Personal Access Token
    - Test the connection and sync your data

## Application Architecture

This application follows modern Android development practices and architecture:

- **MVVM Architecture**: Clear separation of UI, business logic, and data
- **Clean Architecture**: Domain-driven design with clear boundaries between layers
- **Repository Pattern**: Unified data access layer with offline first approach
- **Kotlin**: 100% Kotlin with coroutines and flow for asynchronous operations
- **Jetpack Components**: Room, ViewModel, LiveData, Navigation, and more

### Tech Stack

- **Kotlin**: Modern, concise language for Android development
- **Coroutines + Flow**: For asynchronous operations and reactive streams
- **Hilt**: Dependency injection for clean, modular code
- **Room**: Local database for offline support
- **Retrofit**: Type-safe HTTP client for API communication
- **Material 3**: Modern Android UI components

## Building from Source

### Prerequisites

- Android Studio (latest stable version)
- Kotlin 1.7.0+
- Android SDK 33+
- JDK 11+

### Build Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/mustafacanyucel/fireflyiii-shortcuts.git
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```

5. Install the debug build:
   ```bash
   ./gradlew installDebug
   ```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please make sure your code follows the project's coding style and includes appropriate tests.

### Development Guidelines

- Follow Kotlin coding conventions
- Write unit tests for new code
- Update documentation for significant changes
- Ensure backward compatibility

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Related Projects

- [Firefly III](https://github.com/firefly-iii/firefly-iii): The core personal finance manager
- [Firefly III API Docs](https://api-docs.firefly-iii.org/): Documentation for the Firefly III API

## Acknowledgements

- Thanks to [James Cole](https://github.com/JC5) for creating Firefly III
- All contributors to the Firefly III ecosystem
- The Android open-source community for their amazing libraries and tools

## Contact

Mustafa Can Yücel - [@mustafacanyucel](https://github.com/mustafacanyucel)

Project
Link: [https://github.com/mustafacanyucel/fireflyiii-shortcuts](https://github.com/mustafacanyucel/fireflyiii-shortcuts)

---


If this app helps you manage your finances better, consider donating to Firefly III or supporting
this project!