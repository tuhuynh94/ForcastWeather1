package com.example.gardon.forcastweather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = savedInstanceState;
        setContentView(R.layout.activity_main);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city) {
        WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);
        new CityPreference(this).setCity(city);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        boolean check = mGoogleApiClient.isConnecting();
        boolean check2 = mGoogleApiClient.isConnected();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    double mLat, mLong;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLat = mLastLocation.getLatitude();
            mLong = mLastLocation.getLongitude();
        }
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(mLat, mLong, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WeatherFragment web = new WeatherFragment();
        if (addresses.size() > 0) {
            String city = addresses.get(0).getSubAdminArea();
            web.changeCity(city);
            new CityPreference(this).setCity(city);
        }
        if (state == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, web).commit();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
