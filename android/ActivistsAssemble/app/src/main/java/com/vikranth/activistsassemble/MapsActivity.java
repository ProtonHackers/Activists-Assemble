package com.vikranth.activistsassemble;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
//DONE

/**
 * Creates the Map Activity called by Edit village
 */
public class MapsActivity extends AppCompatActivity {
    private GoogleMap mMap;
    /**
     * The Settings.
     */
    SharedPreferences settings;
    /**
     * The Editor.
     */
    SharedPreferences.Editor editor;
    /**
     * To call after Async task. To fix error
     */

    /**
     * This method runs every time the program starts.
     * calls setupMapIfNeeded to set up the map, initializeUserPrefrences to initialize prefrences
     * also calls search with old string.
     * Then calls fixWindowTextError();
     *
     * @param savedInstanceState previous instance of program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        initializeUserPreferences();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_maps);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("View Map Details");
        }
    }

    public static String getString(Context context, String key) {
//        Log.d("getString", key);
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    /**
     * uses shared preferences to set preference isLoggedIn to false
     */
    private void initializeUserPreferences() {
        String val = getString(this, "MapKEy");
        search("80,80");
    }

    /**
     * Original error: Keyboard pops up when user starts activity.
     * Fix: get the window and set input mode to
     * WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
     */
    private void fixWindowEditTextError() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Initiates a search
     * Using standard search code
     *
     * @param gps previous or new gps position
     */
    private LatLng search(String gps) {
        System.out.println(gps);
        List<Address> addressList = null;
        if (gps != null || !gps.equalsIgnoreCase("") || gps.length() != 0) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(gps, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList == null) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                if (addressList.size() == 0) return null;
                Address address = addressList.get(0);
                PreferenceUtils.putString(getApplicationContext(),"MapCurrent",address.getLocality());
                System.out.println(address.getLatitude() + "" + "" + address.getLongitude());
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                return latLng;
            }
        }
        return null;
    }

//    /**
//     * This gets called after the search button is clicked. It takes the string and calls
//     * search().
//     *
//     * @param view the view passed by button
//     */
//    public void onSearch(View view) {
//        EditText location_tf = (EditText) findViewById(R.id.searchAddress);
//        String location = location_tf.getText().toString();
//        search(location);
//    }

//    /**
//     * Zoom in the geogrpahy or zoom out
//     *
//     * @param view the view passed by the buttons
//     */
//    public void zoom(View view) {
//        if (view.getId() == R.id.zoomIn)
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//        if (view.getId() == R.id.zoomOut)
//            mMap.animateCamera(CameraUpdateFactory.zoomOut());
//
//    }

    /**
     * Changes the type of the graph
     */
    public void changeType() {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * Creates a new map if it is null
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    LatLng latLng = search(PreferenceUtils.getString(getApplicationContext(),"MapKey"));

                    mMap.addMarker(new MarkerOptions().position(latLng).title(
                            getString(getApplicationContext(),"MapCurrent")));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Equator"));
                }
            });
        }
    }

//    /**
//     * Creates an alert dialog and either saves exits or deletes info.
//     *
//     * @param view the view passed by button
//     */
//    public void saveInfo(View view) {
//        EditText location_tf = (EditText) findViewById(R.id.searchAddress);
//        final String location = location_tf.getText().toString();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.Overwrite_GPS))
//                .setMessage(getString(R.string.exit_w_saving))
//                .setPositiveButton(getString(R.string.Save), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (!location.isEmpty()) {
//                            editor.putString("changedMapLocation", location);
//                            editor.apply();
//                            Intent i = new Intent(getApplicationContext(), Edit_Village_info.class);
//                            startActivity(i);
//                        }
//                        callNextActivity();
//                    }
//                })
//                .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).setNeutralButton(getString(R.string.Exit), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                callNextActivity();
//            }
//        }).show();
//    }

    /**
     * Create an option menu for user to select settings
     *
     * @param menu the menu of the Log In Activty screen.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return true;
    }

    /**
     * Deals with menu items being clicked
     *
     * @param item Menu item from the menu
     * @return a boolean of if it was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
//        if(id == R.id.changeMapType){
//            changeType();
//        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calls next Activity
     *
     * @param view view passed by button
     */
    public void callNextActivity(View view) {
        callNextActivity();
    }

    /**
     * Creates a new intent of type Edit_village_info and starts it
     */
    public void callNextActivity() {
//        Intent i = new Intent(getApplicationContext(), Edit_Village_info.class);
//        startActivity(i);
    }


}
