package com.v1.civiladvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;



public class AboutActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    public void openclick(View v)
    {
        String civic_WEBSITE = "https://developers.google.com/civic-information/";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(civic_WEBSITE));
        startActivity(i);
    }


}
