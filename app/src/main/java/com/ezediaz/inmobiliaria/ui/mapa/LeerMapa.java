package com.ezediaz.inmobiliaria.ui.mapa;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LeerMapa implements OnMapReadyCallback {
    private LatLng inmobiliria;
    private Context context;
    private GoogleMap map;

    public LeerMapa( Context context) {
        this.inmobiliria= new LatLng(-33.26824141270706, -66.30452484756408);
        this.context= context;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map= googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.addMarker(new MarkerOptions().position(inmobiliria))
                .setTitle("Inmobiliaria Baigorria-Diaz");
        CameraPosition camUlp= new CameraPosition.Builder()
                .target(inmobiliria)
                .zoom(17)
                .bearing(0)
                .tilt(0)
                .build();
        CameraUpdate caULP= CameraUpdateFactory.newCameraPosition(camUlp);
        map.animateCamera(caULP);
    }
}