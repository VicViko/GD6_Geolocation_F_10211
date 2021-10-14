package com.example.gd6_geolocation_f_10211;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap=mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new
                Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });

    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent (@NonNull Style loadedMapStyle){
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponentOptions customLocationComponentOptions =
                    LocationComponentOptions.builder(this)
                            .pulseEnabled(true)
                            .build();

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this,loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);




        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permission, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explantion,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted){
            mapboxMap.getStyle(new Style.OnStyleLoaded(){
                @Override
                public void onStyleLoaded(@NonNull Style style){

                    enableLocationComponent(style);
                }
            });
        }else{
            Toast.makeText(this, R.string.user_location_permission_not_granted,
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle OutState){
        super.onSaveInstanceState(OutState);
        mapView.onSaveInstanceState(OutState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_style, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_streets:
                mapboxMap.setStyle(Style.MAPBOX_STREETS);
                return true;
            case R.id.menu_dark:
                mapboxMap.setStyle(Style.DARK);
                return true;
            case R.id.menu_light:
                mapboxMap.setStyle(Style.LIGHT);
                return true;
            case R.id.menu_outdoors:
                mapboxMap.setStyle(Style.OUTDOORS);
                return true;
            case R.id.menu_satellite:
                mapboxMap.setStyle(Style.SATELLITE);
                return true;
            case R.id.menu_satellite_streets:
                mapboxMap.setStyle(Style.SATELLITE_STREETS);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}