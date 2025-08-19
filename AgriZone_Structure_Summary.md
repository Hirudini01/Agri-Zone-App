# 🌾 AgriZone Android App - Structure & Flow Summary

## 📱 App Overview
AgriZone is a comprehensive agricultural management Android application built with Java, featuring multiple modules for farming assistance, data management, and community support.

## 🔄 App Flow
1. **MainActivity** (Splash Screen) → "Get Started" button
2. **LoginActivity** → User authentication with SQLite
3. **SignUpActivity** → New user registration
4. **HomeActivity** → Main dashboard with feature cards
5. **Feature Activities** → Direct access to specific modules

## 🎯 Core Features

### ✅ Implemented Features
- **Farming Calendar** - Task management and scheduling
- **Disease Map** - Interactive map with disease outbreaks
- **Expert Support** - Connect with agricultural experts
- **Sensor Dashboard** - Real-time environmental monitoring
- **Marketplace** - Buy/sell agricultural products
- **Training Modules** - Educational content

### 🚧 Coming Soon
- **Smart Data System** - Data analytics and insights
- **Labor Management** - Workforce management
- **Community** - Farmer social networking

## 🏗️ Technical Architecture

### Presentation Layer
- MainActivity (Splash screen)
- LoginActivity (Authentication)
- SignUpActivity (Registration)
- HomeActivity (Main dashboard)
- Feature-specific Activities

### Data Layer
- SQLite Database (agrizone.db)
- Users table (id, username, email, password)
- Expert problems table
- Local file storage

### External Services
- Google Maps API
- Weather API integration
- Camera & Storage access
- Location services

## 🔐 Permissions Required
- INTERNET - API calls and online features
- ACCESS_FINE_LOCATION - GPS and mapping
- ACCESS_COARSE_LOCATION - Location services
- CAMERA - Photo uploads for expert support
- READ/WRITE_EXTERNAL_STORAGE - File management
- WAKE_LOCK - Sensor monitoring

## 📊 Database Schema

### Users Table
- id (INTEGER PRIMARY KEY)
- username (TEXT UNIQUE)
- email (TEXT UNIQUE)
- password (TEXT)

### Expert Problems Table
- problem_id (TEXT PRIMARY KEY)
- category (TEXT)
- description (TEXT)
- location (TEXT)
- contact_number (TEXT)
- timestamp (TEXT)
- status (TEXT DEFAULT 'submitted')

## 🛠️ Technology Stack
- Android Native Development
- Java Programming Language
- SQLite Database
- Google Maps API
- REST APIs
- Gradle Build System

## 📁 Project Structure
```
AgriZone/
├── app/
│   ├── src/main/
│   │   ├── java/com/s23010372/agrizone/
│   │   │   ├── MainActivity.java
│   │   │   ├── LoginActivity.java
│   │   │   ├── SignUpActivity.java
│   │   │   ├── HomeActivity.java
│   │   │   ├── DatabaseActivity.java
│   │   │   ├── FarmingCalendarActivity.java
│   │   │   ├── DiseaseMapActivity.java
│   │   │   ├── ExpertSupportActivity.java
│   │   │   ├── SensorActivity.java
│   │   │   ├── MarketplaceActivity.java
│   │   │   ├── TrainingModulesActivity.java
│   │   │   ├── WeatherApi.java
│   │   │   └── WeatherResponse.java
│   │   ├── res/
│   │   │   ├── layout/ (XML layouts)
│   │   │   ├── drawable/ (Images and graphics)
│   │   │   ├── values/ (Strings, colors, themes)
│   │   │   └── xml/ (Backup rules)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
└── build.gradle.kts
```

## 🎨 UI/UX Features
- Modern gradient backgrounds
- Card-based layout design
- Bottom navigation
- Interactive feature cards
- Responsive design
- Smooth transitions and animations

## 🔧 Build Configuration
- Gradle build system
- Kotlin DSL build scripts
- ProGuard rules for release builds
- Version management with libs.versions.toml

## 📱 Hardware Support
- Ambient temperature sensor (optional)
- Relative humidity sensor (optional)
- Barometer sensor (optional)
- Light sensor (optional)

## 🌐 API Integration
- Google Maps for location services
- Weather API for environmental data
- Camera integration for photo capture
- File system access for data storage

---

*This summary provides a quick overview of the AgriZone app's structure, features, and technical implementation.* 