package com.icu.maplibre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generateMbtiles();

        Mapbox.getInstance(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View rootView = inflater.inflate(R.layout.activity_main, null);
        setContentView(rootView);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.getMapAsync(mapboxMap -> {

            // online
            mapboxMap.setStyle("https://demotiles.maplibre.org/style.json");
            // offline
//            mapboxMap.setStyle(new Style.Builder().fromUri("asset://styles/offline.json"));

            // settings
            mapboxMap.getUiSettings().setAttributionEnabled(true);
            mapboxMap.getUiSettings().setLogoEnabled(true);
//            mapboxMap.setCameraPosition(new CameraPosition.Builder().target(new LatLng(39.414, 115.686)).zoom(10.0).build());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    public void generateMbtiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        // Copy mbtiles from assets to files dir where we can use it

        String fileName = "beijing_maptiler.mbtiles";
        File file = new File(getExternalFilesDir(null), fileName);
        if (file.exists()) {
            Log.e("generateMbtiles", "mbtiles exists at " + file.getAbsolutePath());
        } else {
            Log.e("generateMbtiles", "go in to else");
            try {
                InputStream inputStream = getResources().getAssets().open(fileName); // activity.getAssets().open("tm_world_borders.mbtiles");
                OutputStream outputStream = new FileOutputStream(file.getAbsolutePath());
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("generateMbtiles", "go in to else and catch");
            }
        }
        Log.e("generateMbtiles", "Saved mbtiles to " + file.getAbsolutePath() + ", size: " + file.length());
    }
}