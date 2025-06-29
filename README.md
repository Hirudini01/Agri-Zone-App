# AgriZone Android App

## Disease Tracking Crash Fix

The app was crashing when clicking on the Disease Tracking feature from the home screen. This has been fixed by:

1. **Adding proper error handling** to prevent crashes when views are not found
2. **Adding null checks** throughout the DiseaseMapActivity
3. **Improving exception handling** for Google Maps initialization
4. **Fixing Material Design theme compatibility** - Changed all activities to use `Theme.AgriZone` (Material Design theme) instead of `Theme.AppCompat.Light.NoActionBar`

### Theme Fix Details

The main issue was that the `DiseaseMapActivity` was using `Theme.AppCompat.Light.NoActionBar` which is not compatible with Material Design components like `MaterialCardView`. The error was:

```
java.lang.IllegalArgumentException: The style on this component requires your app theme to be Theme.MaterialComponents (or a descendant).
```

**Solution:** Updated all activities in `AndroidManifest.xml` to use `android:theme="@style/Theme.AgriZone"` which is based on `Theme.Material3.Dark.NoActionBar` and supports Material Design components.

## Google Maps API Key Setup

To make the Disease Tracking feature work properly, you need to:

1. **Get a Google Maps API Key:**
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the Maps SDK for Android
   - Create credentials (API Key)

2. **Update the API Key:**
   - Open `app/src/main/AndroidManifest.xml`
   - Find the line: `<meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_ACTUAL_API_KEY" />`
   - Replace `YOUR_ACTUAL_API_KEY` with your actual Google Maps API key

3. **Build and Run:**
   - Clean and rebuild the project
   - The Disease Tracking feature should now work without crashing

## Features Fixed

- ✅ Disease Tracking Map navigation from home screen
- ✅ Material Design theme compatibility
- ✅ Error handling for missing or invalid API keys
- ✅ Fallback messages when map fails to load
- ✅ Null safety for all UI components
- ✅ Proper lifecycle management for MapView

## Troubleshooting

If you still experience crashes:

1. **Check your internet connection** - Google Maps requires internet access
2. **Verify your API key** - Make sure it's valid and has Maps SDK for Android enabled
3. **Check device compatibility** - Ensure your device supports Google Play Services
4. **Clear app data** - Sometimes cached data can cause issues
5. **Verify theme compatibility** - Ensure all activities use Material Design compatible themes

## Dependencies

The app uses the following key dependencies:
- Google Play Services Maps: 18.2.0
- Material Design Components: 1.12.0
- AndroidX AppCompat
- Retrofit for API calls 