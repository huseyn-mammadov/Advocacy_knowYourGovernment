package com.v1.civiladvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private SwipeRefreshLayout mysw;
    private RecyclerView myrecycler;
    private TextView address, error;
    private ArrayList<Official> all = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private static final String TAG = "MainActivity";

    private static int code = 789;
    private LocationManager locationManager;
    private Criteria criteria;

    private String currentLatLon, geoCodedLatLon;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allcontent();

        mysw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                allLocation();
                convertLatLon();

                if (!currentLatLon.equals(""))
                {
                    if(networkChecker())
                    {
                        new OfficialLoader(MainActivity.this).execute(geoCodedLatLon);
                    }
                    else
                    {
                        noNetworkDialog("Error");

                    }
                }
                else
                {
                    LocationDialog("Please try again",0);
                }
                mysw.setRefreshing(false);
            }
        });

        officialAdapter = new OfficialAdapter(all,this);
        myrecycler.setAdapter(officialAdapter);
        myrecycler.setLayoutManager(new LinearLayoutManager(this));

        allLocation();
        convertLatLon();

        if (!currentLatLon.equals(""))
        {
            if(networkChecker())
            {
                new OfficialLoader(this).execute(geoCodedLatLon);
            }
            else
            {
                noNetworkDialog("Sorry ");

            }
        }
    }

    public boolean networkChecker()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void allcontent()
    {
        mysw = findViewById(R.id.mysw);
        myrecycler = findViewById(R.id.myrecycler);
        address = findViewById(R.id.address);
        error = findViewById(R.id.error);
        currentLatLon = "";
        geoCodedLatLon = "";
    }

    public void allLocation()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();


        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, code);
        }
        else
        {
             detectLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == code)
        {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PERMISSION_GRANTED)
            {
                detectLocation();
                if(networkChecker())
                {
                    LocationDialog("",1);
                    allLocation();
                    convertLatLon();
                    new OfficialLoader(this).execute(geoCodedLatLon);
                }
                else
                {
                    noNetworkDialog("Please type your zip code correctly");
                }
                return;
            }
        }

        LocationDialog("please try again",0);

        address.setText("No Network connection error");



    }
    public void detectLocation()
    {
        String bestProvider = locationManager.getBestProvider(criteria, true);
        assert bestProvider != null;
        @SuppressLint("MissingPermission") Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null)
        {
            currentLatLon = String.format(Locale.getDefault(),  "%.4f, %.4f", currentLocation.getLatitude(), currentLocation.getLongitude());
            address.setText(currentLatLon);
        }
        else
        {
            address.setText("Please make sure you have internet connection");
        }
    }

    public void convertLatLon()
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses;

            String loc = currentLatLon;
            if (!loc.trim().isEmpty())
            {
                String[] latLon = loc.split(",");
                double lat = Double.parseDouble(latLon[0]);
                double lon = Double.parseDouble(latLon[1]);

                addresses = geocoder.getFromLocation(lat, lon, 1);

                if(!addresses.get(0).getPostalCode().equals(""))
                {
                    geoCodedLatLon = addresses.get(0).getPostalCode();
                }
                else if(!addresses.get(0).getLocality().equals(""))
                {
                    geoCodedLatLon = addresses.get(0).getLocality();
                }
                Toast.makeText(this, "in Progress" + addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
            }

        }
        catch (IOException e)
        {
            Log.d(TAG, "convertLatLon: "+e);

        }

    }

    private void LocationDialog(String message, int dismissFlag)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();

        if (dismissFlag == 0)
        {
            builder.setIcon(R.drawable.ic_location_error);
            builder.setTitle("Allow process");
            builder.setMessage(message);

            dialog = builder.create();
            dialog.show();
        }
        else if (dismissFlag == 1)
            dialog.dismiss();
    }

    public void noNetworkDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.ic_network_error);

        builder.setTitle("Connection is lost");
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void updateOfficialData(ArrayList<Official> allConverter)
    {
        all.clear();
        if(allConverter.size()!=0)
        {
            all.addAll(allConverter);
            error.setVisibility(View.GONE);
        }
        else
        {
            address.setText("unavailable");
            error.setVisibility(View.VISIBLE);
        }
        officialAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:
                if(networkChecker())
                {
                    search();
                }
                else
                    noNetworkDialog("connection is lost");
                break;
            case R.id.about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            default:
                Toast.makeText(this,"check later",Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void search()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);
        builder.setIcon(R.drawable.ic_search_accent);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String search = et.getText().toString().trim();
                if(search.equals(""))
                    Toast.makeText(MainActivity.this,"Please type",Toast.LENGTH_SHORT).show();
                else {
                    address.setText("");
                    new OfficialLoader(MainActivity.this).execute(search);
                }

            }
        });
       builder.setNegativeButton("Undo", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(MainActivity.this,"We are going back",Toast.LENGTH_SHORT).show();
           }
       });

       builder.setTitle("Enter a city, State, Zip");


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view)
    {
        int position = myrecycler.getChildAdapterPosition(view);
        Official converter = all.get(position);

        Intent i = new Intent(this,OfficialActivity.class);
        i.putExtra("address", address.getText());
        i.putExtra("official",converter);
        startActivity(i);

    }




}
