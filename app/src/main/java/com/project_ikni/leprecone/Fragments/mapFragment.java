package com.project_ikni.leprecone.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project_ikni.leprecone.DB.DBHelp;
import com.project_ikni.leprecone.GPS.GPSTracker;
import com.project_ikni.leprecone.R;
import com.project_ikni.leprecone.addMarkerActivity;


public class mapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    GoogleMap map;
    DBHelp db;
    Cursor cursor;
    SupportMapFragment mapFragment;

    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;

    public mapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        gpsTracker = new GPSTracker(getContext());
        mLocation = gpsTracker.getLocation();

        latitude = mLocation.getLatitude();
        longitude = mLocation.getLongitude();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        float zoomLevel = 16.0f;
        db = new DBHelp(getActivity());
        getMarkers(map);
        LatLng myLocation = new LatLng(latitude, longitude);
        LatLng myLocation1 = new LatLng(49.7856943, 31.5148367);
        if (mLocation == null) {
            map.addMarker(new MarkerOptions().position(myLocation1).title("Я сдесь...").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation1, 6.6f));
        } else {
            map.addMarker(new MarkerOptions().position(myLocation).title("Я сдесь...").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoomLevel));
        }
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                Intent i = new Intent(mapFragment.getActivity(), addMarkerActivity.class);
                                i.putExtra("key", latLng.toString());

                                getActivity().startActivity(i);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Створити мітку?").setPositiveButton("Так", dialogClickListener)
                        .setNegativeButton("Ні", dialogClickListener).show();
            }
        });
    }

    private void getMarkers(GoogleMap map) {
        db = new DBHelp(getActivity());
        cursor = db.get_all_coordinate();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String position = cursor.getString(cursor.getColumnIndex("latLng"));
                String about = cursor.getString(cursor.getColumnIndex("about"));
                String title = cursor.getString(cursor.getColumnIndex("title"));

                if (position == null) {
                    Toast.makeText(mapFragment.getActivity(), "Не вдалось завантажити усі мітки", Toast.LENGTH_SHORT).show();
                    cursor.moveToNext();
                } else {
                    String[] latlong = position.split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng location = new LatLng(latitude, longitude);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(location)
                            .title(title)
                            .snippet(about));
                    marker.setTag(latlong);
                    cursor.moveToNext();
                }

            }
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        Toast.makeText(getContext(), latLng.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }


}
