# 🌾 AgriZone - Agricultural Management Android App

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)

A comprehensive Android application designed to assist farmers with agricultural management, data tracking, and community support.

## 📱 Features

### ✅ Implemented Features
- **🌱 Farming Calendar** - Task management and scheduling for agricultural activities
- **🗺️ Disease Map** - Interactive map showing disease outbreaks and locations
- **👨‍🌾 Expert Support** - Connect with agricultural experts for guidance
- **📊 Sensor Dashboard** - Real-time monitoring of environmental sensors
- **🛒 Marketplace** - Buy and sell agricultural products and services
- **📚 Training Modules** - Educational content and skill development
- **Real-time Sensor Monitoring**: Monitor temperature, humidity, pressure, and light levels
- **User Profile Management**: Comprehensive user profile with personal information
- **Sensor Data Visualization**: Visual cards displaying current environmental conditions
- **Simulated Data Mode**: Fallback to simulated sensor data when hardware sensors are unavailable
- **Professional UI/UX**: Modern card-based design with intuitive navigation

### 🚧 Coming Soon
- **📈 Smart Data System** - Data analytics and insights for farming decisions
- **👥 Labor Management** - Workforce management and scheduling
- **🌍 Community** - Farmer community and social networking

## 🏗️ Architecture

### Technical Stack
- **Platform**: Android Native
- **Language**: Java
- **Database**: SQLite
- **Maps**: Google Maps API
- **Build System**: Gradle
- **API**: REST APIs for weather and external services

### App Structure
```
AgriZone/
├── app/                          # Main application module
│   ├── src/main/
│   │   ├── java/com/s23010372/agrizone/
│   │   │   ├── MainActivity.java           # Splash screen
│   │   │   ├── LoginActivity.java          # User authentication
│   │   │   ├── SignUpActivity.java         # User registration
│   │   │   ├── HomeActivity.java           # Main dashboard
│   │   │   ├── DatabaseActivity.java       # SQLite database helper
│   │   │   ├── FarmingCalendarActivity.java
│   │   │   ├── DiseaseMapActivity.java
│   │   │   ├── ExpertSupportActivity.java
│   │   │   ├── SensorActivity.java
│   │   │   ├── MarketplaceActivity.java
│   │   │   ├── TrainingModulesActivity.java
│   │   │   ├── WeatherApi.java
│   │   │   └── WeatherResponse.java
│   │   ├── res/                            # Resources
│   │   │   ├── layout/                     # XML layouts
│   │   │   ├── drawable/                   # Images and graphics
│   │   │   ├── values/                     # Strings, colors, themes
│   │   │   └── xml/                        # Backup rules
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 21 or higher
- Google Maps API Key
- Weather API Key (optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/agrizone.git
   cd agrizone
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Configure API Keys**
   - Get a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com/)
   - Replace `YOUR_ACTUAL_API_KEY` in `AndroidManifest.xml` with your actual API key

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or use `./gradlew installDebug`

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Clean build
./gradlew clean
```

## 📊 Database Schema

### Users Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER PRIMARY KEY | Unique user identifier |
| username | TEXT UNIQUE | User's username |
| email | TEXT UNIQUE | User's email address |
| password | TEXT | User's password (hashed) |

### Expert Problems Table
| Column | Type | Description |
|--------|------|-------------|
| problem_id | TEXT PRIMARY KEY | Unique problem identifier |
| category | TEXT | Problem category |
| description | TEXT | Problem description |
| location | TEXT | Problem location |
| contact_number | TEXT | Contact information |
| timestamp | TEXT | Submission timestamp |
| status | TEXT | Problem status (default: 'submitted') |

## 🔐 Permissions

The app requires the following permissions:
- `INTERNET` - For API calls and online features
- `ACCESS_FINE_LOCATION` - For GPS and mapping
- `ACCESS_COARSE_LOCATION` - For location services
- `CAMERA` - For expert support photo uploads
- `READ_EXTERNAL_STORAGE` - For file management
- `WRITE_EXTERNAL_STORAGE` - For file management
- `WAKE_LOCK` - For sensor monitoring

## 🛠️ Development

### Code Style
- Follow Android coding conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and concise

### Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Testing
- Unit tests for business logic
- Integration tests for database operations
- UI tests for critical user flows

## 📱 Screenshots

[Add screenshots of key app screens here]

## 🔄 App Flow

1. **Splash Screen** → MainActivity with "Get Started" button
2. **Authentication** → LoginActivity with SQLite validation
3. **Registration** → SignUpActivity for new users
4. **Main Dashboard** → HomeActivity with feature cards
5. **Feature Navigation** → Direct access to specific modules

## 🌐 API Integration

- **Google Maps API** - Location services and mapping
- **Weather API** - Environmental data and forecasts
- **Camera API** - Photo capture for expert support
- **File System API** - Local data storage

## 📈 Roadmap

### Version 1.1
- [ ] Smart Data System implementation
- [ ] Enhanced sensor integration
- [ ] Offline mode support

### Version 1.2
- [ ] Labor Management module
- [ ] Community features
- [ ] Push notifications

### Version 2.0
- [ ] Multi-language support
- [ ] Advanced analytics
- [ ] Cloud synchronization

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Developer**: [Your Name]
- **Designer**: [Designer Name]
- **Project Manager**: [PM Name]

## 📞 Support

- **Email**: support@agrizone.com
- **Issues**: [GitHub Issues](https://github.com/yourusername/agrizone/issues)
- **Documentation**: [Wiki](https://github.com/yourusername/agrizone/wiki)

## 🙏 Acknowledgments

- Google Maps API for location services
- Android community for best practices
- Agricultural experts for domain knowledge
- Open source contributors

