package com.v1.civiladvocacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class PhotoActivity extends AppCompatActivity
{

    private TextView address;
    private TextView position;
    private TextView identity;
    private TextView side;
    private ImageView logo;
    private ImageView font;
    private static final String TAG = "PhotoActivity";
    private Official converter;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        allcontent();
        allLocation();
        updateSystem();
    }



    void allLocation()
    {
        if(getIntent().hasExtra("address"))
            address.setText(getIntent().getStringExtra("address"));

        else
            address.setText("");
    }

    void updateSystem()
    {
        if(getIntent().hasExtra("official"))
        {
            converter = (Official) getIntent().getSerializableExtra("official");
            assert converter != null;
            position.setText(converter.getPosition());
            identity.setText(converter.getIdentity());
            side.setText(converter.getSide());

            if(converter.getSide().trim().toLowerCase().contains("democratic"))
                handleDemocrat();
            else if(converter.getSide().trim().toLowerCase().contains("republican"))
                handleRepublican();
            else
                handleneutral();

            mainImage(converter.getImagewebsite().trim());
        }

    }
    void allcontent()
    {
        constraintLayout = findViewById(R.id.constrainedLayout);
        address = findViewById(R.id.address);
        position = findViewById(R.id.position);
        identity = findViewById(R.id.identity);
        side = findViewById(R.id.side);
        font = findViewById(R.id.font);
        logo = findViewById(R.id.logo);
    }



    void mainImage(String WEBSITE)
    {
        Log.d(TAG, "mainImage: "+WEBSITE);



        Picasso.get()
                .load(WEBSITE)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(font);
    }

    public void clickLogo(View v)
    {
        String dem_WEBSITE = "https://democrats.org";
        String rep_WEBSITE = "https://www.gop.com";

        if(converter.getSide().toLowerCase().trim().contains("democratic"))
        {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(dem_WEBSITE));
            startActivity(i);
        }
        else if(converter.getSide().toLowerCase().trim().contains("republican"))
        {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(rep_WEBSITE));
            startActivity(i);

        }
    }
    void handleDemocrat()
    {
        address.setBackgroundResource(R.color.dark_blue);
        logo.setImageResource(R.drawable.dem_logo);
        constraintLayout.setBackgroundResource(R.color.blue);
        getWindow().setNavigationBarColor(getColor(R.color.blue));
    }

    void handleRepublican()
    {
        address.setBackgroundResource(R.color.dark_red);
        logo.setImageResource(R.drawable.rep_logo);
        constraintLayout.setBackgroundResource(R.color.red);
        getWindow().setNavigationBarColor(getColor(R.color.red));
    }
    void handleneutral()
    {
        address.setBackgroundResource(R.color.extra_dark_grey);
        logo.setImageResource(R.drawable.help);
        constraintLayout.setBackgroundResource(R.color.dark_grey);
        getWindow().setNavigationBarColor(getColor(R.color.dark_grey));
    }

    @Override
    protected void onPause()
    {
        super.onPause();

    }

}
