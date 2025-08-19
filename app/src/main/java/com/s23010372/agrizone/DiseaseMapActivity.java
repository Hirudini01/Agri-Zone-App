package com.s23010372.agrizone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiseaseMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Spinner spinnerFilter;
    private MaterialCardView ivBack;
    private CardView btnStats;
    private FloatingActionButton fabMyLocation;
    private FloatingActionButton fabLayerToggle;
    private FloatingActionButton fabRefresh;
    private CardView infoPanel;
    private TextView tvInfoTitle;
    private TextView tvInfoDetails;
    private EditText etSearchLocation;
    private ImageView btnSearchAction;
    private final String API_KEY = "AIzaSyBE17D9KvLvxfSf1G1ivwbGffn1XIXl8qY";
    private final String CITY = "Colombo";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable full screen mode
        enableFullScreenMode();

        setContentView(R.layout.activity_disease_map);

        try {
            // Initialize views
            mapView = findViewById(R.id.mapView);
            spinnerFilter = findViewById(R.id.spinnerFilter);
            ivBack = findViewById(R.id.ivBack);
            btnStats = findViewById(R.id.btnStats);
            fabMyLocation = findViewById(R.id.fabMyLocation);
            fabLayerToggle = findViewById(R.id.fabLayerToggle);
            fabRefresh = findViewById(R.id.fabRefresh);
            infoPanel = findViewById(R.id.infoPanel);
            tvInfoTitle = findViewById(R.id.tvInfoTitle);
            tvInfoDetails = findViewById(R.id.tvInfoDetails);
            etSearchLocation = findViewById(R.id.etSearchLocation);
            btnSearchAction = findViewById(R.id.btnSearchAction);

            // Show a toast to confirm navigation worked
            Toast.makeText(this, "🌾 Disease Tracking & Map Screen - Full Screen Mode", Toast.LENGTH_SHORT).show();

            // Setup back button
            if (ivBack != null) {
                ivBack.setOnClickListener(v -> finish());
            }

            // Setup stats button
            if (btnStats != null) {
                btnStats.setOnClickListener(v -> {
                    Toast.makeText(this, "📊 Disease Statistics", Toast.LENGTH_SHORT).show();
                    showDiseaseStats();
                });
            }

            // Setup spinner
            if (spinnerFilter != null) {
                setupSpinner();
            }

            // Setup floating action buttons
            setupFloatingActionButtons();

            // Setup search functionality
            setupSearchBar();

            // Initialize map
            if (mapView != null) {
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(this);
            } else {
                // Fallback if map view is not found
                Toast.makeText(this, "Map view not found. Please check the layout file.", Toast.LENGTH_LONG).show();
                if (infoPanel != null && tvInfoTitle != null && tvInfoDetails != null) {
                    infoPanel.setVisibility(View.VISIBLE);
                    tvInfoTitle.setText("⚠️ Map Error");
                    tvInfoDetails.setText("Map view could not be initialized. Please restart the app.");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing Disease Map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void enableFullScreenMode() {
        // Hide status bar and navigation bar for immersive experience
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController =
            WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        if (windowInsetsController != null) {
            // Hide the status bar
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
            // Configure behavior when swiping to show hidden bars
            windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }

        // Keep screen on for better map interaction
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {
            googleMap = map;

            // Set map properties for full screen experience
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(false); // Disable default zoom controls
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(false); // Disable default toolbar
            googleMap.getUiSettings().setMyLocationButtonEnabled(false); // Use custom FAB instead
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setTiltGesturesEnabled(true);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            // Request location permission if not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);

                // Do NOT lock camera to user location on every location change
                // Remove googleMap.setOnMyLocationChangeListener
                // User can drag and move map freely
            } else {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
                // Default location if permission not granted
                LatLng colombo = new LatLng(6.9271, 79.8612);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(colombo, 12));
                showInfoPanel("🌾 Disease Tracking Map", "Showing default location (Colombo). Enable location for more features.");
            }

            // Add initial markers
            addAllDiseaseMarkers();

            // Show initial info panel
            showInfoPanel("🌾 Disease Tracking Map", "Tap on markers to view detailed information about disease outbreaks");

            // Set marker click listener
            googleMap.setOnMarkerClickListener(marker -> {
                // Professional info panel with emoji and color
                String title = marker.getTitle();
                String snippet = marker.getSnippet();
                if (title.contains("Banana")) {
                    showInfoPanel("🍌 " + title, snippet);
                } else if (title.contains("Rice")) {
                    showInfoPanel("🌾 " + title, snippet);
                } else if (title.contains("Coconut")) {
                    showInfoPanel("🥥 " + title, snippet);
                } else {
                    showInfoPanel("🦠 " + title, snippet);
                }
                return true;
            });

            // Toggle search/filter bar visibility on map click, keep Disease Tracker text visible
            googleMap.setOnMapClickListener(latLng -> {
                // Hide info panel
                if (infoPanel != null) {
                    infoPanel.setVisibility(View.GONE);
                }
                // Toggle search and filter bar only
                LinearLayout headerLayout = findViewById(R.id.headerLayout);
                if (headerLayout != null) {
                    // Get MaterialCardView containers for search and filter bars
                    View searchBarCard = headerLayout.findViewById(R.id.etSearchLocation).getParent() instanceof View
                        ? (View) headerLayout.findViewById(R.id.etSearchLocation).getParent() : null;
                    View filterBarCard = headerLayout.findViewById(R.id.spinnerFilter).getParent() instanceof View
                        ? (View) headerLayout.findViewById(R.id.spinnerFilter).getParent() : null;
                    if (searchBarCard != null && filterBarCard != null) {
                        boolean currentlyVisible = searchBarCard.getVisibility() == View.VISIBLE;
                        int newVisibility = currentlyVisible ? View.GONE : View.VISIBLE;
                        searchBarCard.setVisibility(newVisibility);
                        filterBarCard.setVisibility(newVisibility);
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error loading map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            showInfoPanel("⚠️ Map Loading Error", "Please check your internet connection and ensure you have a valid Google Maps API key configured.");
        }
    }

    private void setupFloatingActionButtons() {
        // My Location FAB
        if (fabMyLocation != null) {
            fabMyLocation.setOnClickListener(v -> {
                if (googleMap != null) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                        if (googleMap.getMyLocation() != null) {
                            LatLng userLatLng = new LatLng(
                                googleMap.getMyLocation().getLatitude(),
                                googleMap.getMyLocation().getLongitude()
                            );
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                            showInfoPanel("📍 Your Location", "You are here. Explore disease outbreaks nearby.");
                            Toast.makeText(this, "📍 Showing your live location", Toast.LENGTH_SHORT).show();
                        } else {
                            LatLng colombo = new LatLng(6.9271, 79.8612);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(colombo, 12));
                            showInfoPanel("🌾 Disease Tracking Map", "Live location not available, showing default.");
                            Toast.makeText(this, "📍 Live location not available, showing default", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        LatLng colombo = new LatLng(6.9271, 79.8612);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(colombo, 12));
                        showInfoPanel("🌾 Disease Tracking Map", "Permission not granted, showing default location.");
                        Toast.makeText(this, "📍 Permission not granted, showing default location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Layer Toggle FAB
        if (fabLayerToggle != null) {
            fabLayerToggle.setOnClickListener(v -> {
                if (googleMap != null) {
                    int currentMapType = googleMap.getMapType();
                    if (currentMapType == GoogleMap.MAP_TYPE_NORMAL) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        Toast.makeText(this, "🛰️ Satellite View", Toast.LENGTH_SHORT).show();
                    } else {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        Toast.makeText(this, "🗺️ Normal View", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Refresh FAB
        if (fabRefresh != null) {
            fabRefresh.setOnClickListener(v -> {
                Toast.makeText(this, "🔄 Refreshing disease data...", Toast.LENGTH_SHORT).show();
                refreshDiseaseData();
            });
        }
    }

    private void showDiseaseStats() {
        // Show info panel with statistics
        if (infoPanel != null && tvInfoTitle != null && tvInfoDetails != null) {
            infoPanel.setVisibility(View.VISIBLE);
            tvInfoTitle.setText("📊 Disease Statistics");
            tvInfoDetails.setText("• Total Active Cases: 12\n• Affected Areas: 8\n• Most Common: Banana Wilt\n• Risk Level: Medium\n• Last Updated: Today");
        }
    }

    private void refreshDiseaseData() {
        // Simulate refreshing data
        if (googleMap != null) {
            googleMap.clear();
            addAllDiseaseMarkers();
            Toast.makeText(this, "✅ Data refreshed successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this, R.array.disease_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDisease = parent.getItemAtPosition(position).toString();
                filterDiseases(selectedDisease);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void filterDiseases(String diseaseType) {
        if (googleMap != null) {
            // Clear existing markers
            googleMap.clear();
            
            // Add markers based on selected disease type
            if (diseaseType.equals("All Diseases")) {
                addAllDiseaseMarkers();
                showInfoPanel("🌾 All Diseases", "Showing all disease outbreaks across the region");
            } else if (diseaseType.equals("Banana Wilt")) {
                addBananaWiltMarkers();
                showInfoPanel("🍌 Banana Wilt", "Fusarium wilt affecting banana plantations");
            } else if (diseaseType.equals("Rice Blast")) {
                addRiceBlastMarkers();
                showInfoPanel("🌾 Rice Blast", "Magnaporthe oryzae affecting rice crops");
            } else {
                addDefaultMarkers();
                showInfoPanel("🦠 Disease Alert", "Other crop diseases detected");
            }
            
            Toast.makeText(this, "🔍 Filtered: " + diseaseType, Toast.LENGTH_SHORT).show();
        }
    }

    private void showInfoPanel(String title, String details) {
        if (infoPanel != null && tvInfoTitle != null && tvInfoDetails != null) {
            infoPanel.setVisibility(View.VISIBLE);
            tvInfoTitle.setText(title);
            tvInfoDetails.setText(details);
            // Optionally, set background color or icon based on disease type for professional look
        }
    }

    private void addAllDiseaseMarkers() {
        // Add markers for all diseases with custom titles
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(6.9271, 79.8612))
            .title("🍌 Colombo - Banana Wilt")
            .snippet("Severity: High | Cases: 5"));
        
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(8.3114, 80.4037))
            .title("🌾 Anuradhapura - Rice Blast")
            .snippet("Severity: Medium | Cases: 3"));
        
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(7.8731, 80.7718))
            .title("🥥 Kurunegala - Coconut Root Wilt")
            .snippet("Severity: Low | Cases: 2"));
        
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(6.0535, 80.2210))
            .title("🍌 Galle - Banana Wilt")
            .snippet("Severity: Medium | Cases: 2"));
    }

    private void addBananaWiltMarkers() {
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(6.9271, 79.8612))
            .title("🍌 Colombo - Banana Wilt")
            .snippet("Severity: High | Cases: 5"));
        
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(6.0535, 80.2210))
            .title("🍌 Galle - Banana Wilt")
            .snippet("Severity: Medium | Cases: 2"));
    }

    private void addRiceBlastMarkers() {
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(8.3114, 80.4037))
            .title("🌾 Anuradhapura - Rice Blast")
            .snippet("Severity: Medium | Cases: 3"));
        
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(7.8731, 80.7718))
            .title("🌾 Kurunegala - Rice Blast")
            .snippet("Severity: Low | Cases: 1"));
    }

    private void addDefaultMarkers() {
        googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(6.9271, 79.8612))
            .title("🦠 Disease Alert")
            .snippet("Tap for more information"));
    }

    private void setupSearchBar() {
        // Setup search EditText with action listener
        if (etSearchLocation != null) {
            etSearchLocation.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            });
        }

        // Setup search button click listener
        if (btnSearchAction != null) {
            btnSearchAction.setOnClickListener(v -> performSearch());
        }
    }

    private void performSearch() {
        if (etSearchLocation != null && googleMap != null) {
            String searchQuery = etSearchLocation.getText().toString().trim();

            if (searchQuery.isEmpty()) {
                Toast.makeText(this, "Please enter a location to search", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hide keyboard and clear focus
            etSearchLocation.clearFocus();

            // Perform location search
            searchLocation(searchQuery);
        }
    }

    private void searchLocation(String query) {
        // Convert query to lowercase for case-insensitive search
        String lowerQuery = query.toLowerCase();
        LatLng searchResult = null;
        String resultName = "";

        // Predefined locations for Sri Lankan cities and agricultural areas
        switch (lowerQuery) {
            case "colombo":
            case "colombo district":
                searchResult = new LatLng(6.9271, 79.8612);
                resultName = "Colombo";
                break;
            case "kandy":
            case "kandy district":
                searchResult = new LatLng(7.2906, 80.6337);
                resultName = "Kandy";
                break;
            case "galle":
            case "galle district":
                searchResult = new LatLng(6.0535, 80.2210);
                resultName = "Galle";
                break;
            case "anuradhapura":
            case "anuradhapura district":
                searchResult = new LatLng(8.3114, 80.4037);
                resultName = "Anuradhapura";
                break;
            case "kurunegala":
            case "kurunegala district":
                searchResult = new LatLng(7.8731, 80.7718);
                resultName = "Kurunegala";
                break;
            case "matara":
            case "matara district":
                searchResult = new LatLng(5.9549, 80.5550);
                resultName = "Matara";
                break;
            case "jaffna":
            case "jaffna district":
                searchResult = new LatLng(9.6615, 80.0255);
                resultName = "Jaffna";
                break;
            case "batticaloa":
            case "batticaloa district":
                searchResult = new LatLng(7.7102, 81.6924);
                resultName = "Batticaloa";
                break;
            case "trincomalee":
            case "trincomalee district":
                searchResult = new LatLng(8.5874, 81.2152);
                resultName = "Trincomalee";
                break;
            case "hambantota":
            case "hambantota district":
                searchResult = new LatLng(6.1241, 81.1185);
                resultName = "Hambantota";
                break;
            case "rice farm":
            case "paddy field":
                searchResult = new LatLng(8.3114, 80.4037); // Anuradhapura rice area
                resultName = "Rice Farming Area - Anuradhapura";
                break;
            case "tea plantation":
            case "tea estate":
                searchResult = new LatLng(6.9497, 80.7891); // Nuwara Eliya tea region
                resultName = "Tea Plantation - Nuwara Eliya";
                break;
            case "coconut farm":
            case "coconut plantation":
                searchResult = new LatLng(7.8731, 80.7718); // Kurunegala coconut area
                resultName = "Coconut Plantation - Kurunegala";
                break;
            default:
                // If no exact match, search for partial matches
                if (lowerQuery.contains("rice") || lowerQuery.contains("paddy")) {
                    searchResult = new LatLng(8.3114, 80.4037);
                    resultName = "Rice Farming Area";
                } else if (lowerQuery.contains("tea")) {
                    searchResult = new LatLng(6.9497, 80.7891);
                    resultName = "Tea Plantation Area";
                } else if (lowerQuery.contains("coconut")) {
                    searchResult = new LatLng(7.8731, 80.7718);
                    resultName = "Coconut Farming Area";
                } else if (lowerQuery.contains("banana")) {
                    searchResult = new LatLng(6.9271, 79.8612);
                    resultName = "Banana Plantation Area";
                } else {
                    Toast.makeText(this, "⚠️ Location not found. Try: Colombo, Kandy, Galle, etc.", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }

        if (searchResult != null) {
            // Animate camera to search result
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchResult, 12));

            // Remove previous search marker if needed (optional)
            // Add a temporary marker for the searched location
            googleMap.addMarker(new MarkerOptions()
                .position(searchResult)
                .title("📍 " + resultName)
                .snippet("Search Result"))
                .showInfoWindow();

            // Show info panel with search result
            showInfoPanel("🔍 Search Result", "Found: " + resultName + "\nLat: " +
                String.format("%.4f", searchResult.latitude) + ", Lng: " +
                String.format("%.4f", searchResult.longitude));

            Toast.makeText(this, "📍 Found: " + resultName, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (googleMap != null) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
