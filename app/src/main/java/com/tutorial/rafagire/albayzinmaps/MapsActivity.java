package com.tutorial.rafagire.albayzinmaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    Bundle bundle;
    boolean fromSearchActivity;

    LatLng position;
    String markerTitle;
    Marker marker;
    Button bSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        bSelect = (Button) findViewById(R.id.button_map_position);


        bundle = this.getIntent().getExtras();
        if((bundle != null) && (bundle.getString("markerTitle")!=null))
            fromSearchActivity = true;

        if (fromSearchActivity) {
            markerTitle = bundle.getString("markerTitle");
            position = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
            bSelect.setText(getString(R.string.button_map_open_GoogleMaps));
        }
        else{
            markerTitle = "Albayzin";
            position = new LatLng(37.1812136,-3.5931174);
        }

        bSelect.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Albayzin and move the camera
        marker = mMap.addMarker(new MarkerOptions().position(position).title(markerTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
        //marker.showInfoWindow();

        //mMap.setOnMapClickListener();
        //mMap.setOnMapLongClickListener();
        //mMap.setOnMarkerClickListener();
        //mMap.setOnMarkerDragListener();
        if(!fromSearchActivity) {
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMapClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(fromSearchActivity){
            Uri uriIntent = Uri.parse("geo:" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "?q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "(" + marker.getTitle() + ")" + "&z=16");
            // Prefiero no abrir directamente la navegacion hasta el sitio, porque si el usuario no quisiera dar permiso, ya no puede ver el lugar exacto.
            // Ademas, el mapa cuenta con dos botones para pedir expresamente la navegacion hasta el sitio
            //Uri uriIntent = Uri.parse("google.navigation:q=" + marker.getPosition().latitude + "," + marker.getPosition().longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, uriIntent);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }
        else {
            if (!marker.getTitle().equals("Albayzin")) {
                Intent intent = new Intent(v.getContext(), AddSiteActivity.class);

                Bundle bundle = new Bundle();
                bundle.putBoolean("newSite", true);
                bundle.putDouble("latitude", marker.getPosition().latitude);
                bundle.putDouble("longitude", marker.getPosition().longitude);

                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            } else {
                Toast.makeText(v.getContext(), getString(R.string.toast_map_no_marker), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();

        //mMap.addMarker(new MarkerOptions()
        //      .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador))
        //    .anchor(0.0f, 1.0f)
        //  .position(latLng));
        marker = mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(latLng).title(getString(R.string.marker_map)));
        marker.showInfoWindow();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();

        //mMap.addMarker(new MarkerOptions()
        //      .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador))
        //    .anchor(0.0f, 1.0f)
        //  .position(latLng));
        marker = mMap.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(latLng).title(getString(R.string.marker_map)));
        marker.showInfoWindow();
    }
}
