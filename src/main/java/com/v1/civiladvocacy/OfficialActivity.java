package com.v1.civiladvocacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfficialActivity extends AppCompatActivity
{
    private ConstraintLayout constrainedLayout;
    private ConstraintLayout content;
    private TextView position;
    private TextView identity;
    private TextView side;
    private TextView office;
    private TextView office2;
    private TextView email;
    private TextView email2;
    private TextView website;
    private TextView website2;
    private TextView number;
    private TextView number2;
    private TextView address;
    private ImageView font;
    private ImageView logo;
    private ImageView facebook;
    private ImageView twitter;
    private ImageView youtube;
    private ImageView google;
    private Channel convertTwitter;
    private Channel convertYoutube;
    private Channel convertGoogle;
    private Official converter;
    private static final String TAG = "OfficialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        allcontent();
        allLocation();
        updateSystem();
    }

    void allcontent()
    {
        constrainedLayout = findViewById(R.id.constrainedLayout);
        content = findViewById(R.id.content);

        address = findViewById(R.id.address);
        position = findViewById(R.id.position);
        identity = findViewById(R.id.identity);
        side = findViewById(R.id.side);
        office = findViewById(R.id.office);
        office2 = findViewById(R.id.office2);
        email = findViewById(R.id.email);
        email2 = findViewById(R.id.email2);
        website = findViewById(R.id.website);
        website2 = findViewById(R.id.website2);
        number = findViewById(R.id.number);
        number2 = findViewById(R.id.number2);
        font = findViewById(R.id.font);
        logo = findViewById(R.id.logo);
        
        twitter = findViewById(R.id.twitter);
        youtube = findViewById(R.id.youtube);
        google = findViewById(R.id.main);
    }

    void allLocation()
    {
        if(getIntent().hasExtra("location"))
            address.setText(getIntent().getStringExtra("location"));
        else
            address.setText("");
    }

    void updateSystem()
    {
        if(getIntent().hasExtra("official"))
        {
            converter = (Official) getIntent().getSerializableExtra("official");
            ArrayList<Channel> channels;
            assert converter != null;
            position.setText(converter.getPosition());
            identity.setText(converter.getIdentity());
            side.setText(converter.getSide());
            if(!converter.getOffice().equals(""))
            {
                office.setText(converter.getOffice().trim());
                Linkify.addLinks(office, Linkify.ALL);
                office.setLinkTextColor(getColor(R.color.white));
            }
            else
            {
                office2.setVisibility(View.GONE);
                office.setVisibility(View.GONE);
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                number2.setLayoutParams(params);
            }

            if(!converter.getNumbers().equals(""))
            {
                number.setText(converter.getNumbers());
                number.setLinkTextColor(getColor(R.color.white));
                Linkify.addLinks(number, Linkify.ALL);
            }
            else
            {
                number2.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
            }

            if(!converter.getEmails().equals(""))
            {
                email.setText(converter.getEmails());
                email.setLinkTextColor(getColor(R.color.white));
                Linkify.addLinks(email, Linkify.ALL);
            }
            else
            {
                email2.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
            }

            if(!converter.getWebsites().equals(""))
            {
                website.setText(converter.getWebsites());
                website.setLinkTextColor(getColor(R.color.white));
                Linkify.addLinks(website, Linkify.ALL);
            }
            else
            {
                website2.setVisibility(View.GONE);
                website.setVisibility(View.GONE);
            }


            if(converter.getSide().trim().toLowerCase().contains("republican"))
                handleRepublican();
            else if(converter.getSide().trim().toLowerCase().contains("democratic"))
                handleDemocrat();
            else
                handleneutral();



            mainImage(converter.getImagewebsite().trim());

            channels = converter.getChannels();

            if( channels.size() > 0 )
            {
                for(Channel single_channel : channels )
                {
                    if(single_channel.getName().equals("Twitter"))
                    {
                        convertTwitter = single_channel;
                        twitter.setVisibility(View.VISIBLE);

                    }
                    if(single_channel.getName().equals("GooglePlus"))
                    {
                        convertGoogle = single_channel;
                        google.setVisibility(View.VISIBLE);
                    }
                    if(single_channel.getName().equals("YouTube"))
                    {
                        convertYoutube = single_channel;
                        youtube.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
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


    public void clickTwitter(View v)
    {
        Intent intent;
        String id = convertTwitter.getId();
        try
        {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e)
        {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + id));
        }
        startActivity(intent);

    }



    public void clickGoogle(View v)
    {
        String id = convertGoogle.getId();
        Intent intent;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", id);
            startActivity(intent);

        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + id)));

        }
    }

    public void clickYoutube(View v) {
        String id = convertYoutube.getId();
        Intent intent;
        try
        {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + id));
            startActivity(intent);

        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + id)));

        }
    }

////
 ///   public void youTubeClicked(View v) {
    ///    String name = <the official’s youtube id from download>;
      //  Intent intent = null;
      //  try {
        ///    intent = new Intent(Intent.ACTION_VIEW);
    ///         intent.setPackage("com.google.android.youtube");
///         intent.setData(Uri.parse("https://www.youtube.com/" + name));
    ///          startActivity(intent);
///        } catch (ActivityNotFoundException e)
    ///       { startActivity(new Intent(Intent.ACTION_VIEW,
    ///             Uri.parse("https://www.youtube.com/" + name)));
    ///    }














    void handleDemocrat()
    {
        address.setBackgroundResource(R.color.dark_blue);
        logo.setImageResource(R.drawable.dem_logo);
        constrainedLayout.setBackgroundResource(R.color.blue);
        content.setBackgroundResource(R.drawable.dem_details_bg);
        getWindow().setNavigationBarColor(getColor(R.color.blue));
    }

    void handleRepublican()
    {
        address.setBackgroundResource(R.color.dark_red);
        logo.setImageResource(R.drawable.rep_logo);
        constrainedLayout.setBackgroundResource(R.color.red);
        content.setBackgroundResource(R.drawable.rep_details_bg);
        getWindow().setNavigationBarColor(getColor(R.color.red));
    }
    void handleneutral()
    {
        address.setBackgroundResource(R.color.colorPrimaryDark);
        logo.setImageResource(R.drawable.help);
        constrainedLayout.setBackgroundResource(R.color.dark_grey);
        content.setBackgroundResource(R.drawable.np_details_bg);
        font.setBackgroundResource(R.drawable.dp_background_non);
        getWindow().setNavigationBarColor(getColor(R.color.dark_grey));
    }

    void mainImage(String URL)
    {
        if(URL.equals(""))
        {
            font.setImageResource(R.drawable.missing);
        }
        else
        {
            Picasso.get()
                    .load(URL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.brokenimage)
                    .into(font);
        }
    }

    public void zoomImage(View v)
    {
        if(!converter.getImagewebsite().equals(""))
        {
            Intent i = new Intent(this, PhotoActivity.class);
            i.putExtra("location", address.getText());
            i.putExtra("official",converter);
            startActivity(i);
        }
        else
            Toast.makeText(this,"Profile is not find",Toast.LENGTH_SHORT).show();

        
    }

    @Override
    protected void onPause()
    {
        super.onPause();

    }
}
