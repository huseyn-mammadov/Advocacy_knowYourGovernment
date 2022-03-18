package com.v1.civiladvocacy;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class OfficialLoader extends AsyncTask<String, Void, ArrayList<Official>> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private static final String TAG = "OfficialLoader";


    OfficialLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected ArrayList<Official> doInBackground(String... strings) {
        ArrayList<Official> finalData;
        String temp = strings[0];
        String API_TOKEN = "AIzaSyDDGUn-PnFZBvZD5RQjCRo33UrTQd6ZAsU";
        String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=" + API_TOKEN + "&address=" + temp;

        String data = websiteparser(DATA_URL);
        finalData = firstParser(data);
        return finalData;
    }

    private String websiteparser(String URL) {
        Uri uri = Uri.parse(URL);
        String urlToUse = uri.toString();

        StringBuilder stringbuilder = new StringBuilder();
        try {
            java.net.URL url = new URL(urlToUse);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String indices;
            while ((indices = reader.readLine()) != null)
                stringbuilder.append(indices).append('\n');
        } catch (Exception e) {
            return stringbuilder.toString();
        }
        return stringbuilder.toString();
    }

    private ArrayList<Official> firstParser(String data) {
        ArrayList<Official> allConverter = new ArrayList<>();
        allLocation(data);
        Official official;

        try {
            JSONObject converter = new JSONObject(data);
            JSONArray offices = (JSONArray) converter.get("offices");
            JSONArray officials = (JSONArray) converter.get("officials");
            for (int i = 0; i < offices.length(); i++) {
                JSONObject office = (JSONObject) offices.get(i);
                JSONObject officialIndices = (JSONObject) offices.get(i);
                JSONArray index = officialIndices.getJSONArray("officialIndices");

                for (int j = 0; j < index.length(); j++) {
                    Official official_intermediate;
                    JSONObject officialData_JSON = (JSONObject) officials.get(index.getInt(j));
                    official_intermediate = official(officialData_JSON);
                    official = official_intermediate;

                    official.setPosition(office.getString("name"));
                    allConverter.add(official);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "firstParser: "+e);

        }
        return allConverter;
    }

    private Official official(JSONObject officialData_JSON) {
        Official converter = new Official();

        converter.setIdentity(identityparser(officialData_JSON));
        converter.setSide(sideparser(officialData_JSON));
        converter.setOffice(officeparser(officialData_JSON));
        converter.setWebsites(websiteparser(officialData_JSON));
        converter.setEmails(emailparser(officialData_JSON));
        converter.setNumbers(numberparser(officialData_JSON));
        converter.setImagewebsite(imageparser(officialData_JSON));
        converter.setChannels(channelAll(officialData_JSON));
        return converter;
    }

    private String identityparser(JSONObject officialData_json) {
        String name = "";
        try {
            name = officialData_json.getString("name");
        } catch (Exception e) {
            Log.d(TAG, "identityparser: "+e);


        }

        return name;
    }

    private String officeparser(JSONObject officialData_json) {
        String finalAddress = "";
        try {
            JSONArray addresses = (JSONArray) officialData_json.get("address");
            JSONObject address = (JSONObject) addresses.get(0);
            String indices1 = locationFirst(address);
            String indices2 = locationSecond(address);
            String city = cityparser(address);
            String state = stateparser(address);
            String zip = zipcodeparser(address);
            finalAddress = indices1 + ", " + (indices2.equals("") ? indices2 + "" : indices2 + ", ") + city + ", " + state + ", " + zip;
        } catch (Exception e) {
            Log.d(TAG, "officeparser: "+e);


        }

        return finalAddress;
    }

    private String locationFirst(JSONObject address) {
        String indices1 = "";
        try {
            indices1 = address.getString("indices1");
        } catch (Exception e) {
            Log.d(TAG, "locationFirst: "+e);


        }
        return indices1;
    }

    private String locationSecond(JSONObject address) {
        String indices2 = "";
        try {
            indices2 = address.getString("indices2");
        } catch (Exception e) {
            Log.d(TAG, "locationSecond: "+e);


        }
        return indices2;
    }

    private String cityparser(JSONObject address) {
        String city = "";
        try {
            city = address.getString("city");
        } catch (Exception e) {
            Log.d(TAG, "cityparser: "+e);


        }
        return city;
    }

    private String stateparser(JSONObject address) {
        String state = "";
        try {
            state = address.getString("state");
        } catch (Exception e) {
            Log.d(TAG, "stateparser: "+e);


        }
        return state;
    }

    private String zipcodeparser(JSONObject address) {
        String zip = "";
        try {
            zip = address.getString("zip");
        } catch (Exception e) {
            Log.d(TAG, "zipcodeparser: "+e );


        }
        return zip;
    }

    private String websiteparser(JSONObject officialData_json) {
        String URL = "";

        try {
            JSONArray urls = (JSONArray) officialData_json.get("urls");
            URL = urls.get(0).toString();
        } catch (Exception e) {
            Log.d(TAG, "websiteparser: "+e);


        }

        return URL;
    }

    private String emailparser(JSONObject officialData_json) {
        String email = "";

        try {
            JSONArray urls = (JSONArray) officialData_json.get("emails");
            email = urls.get(0).toString();
        } catch (Exception e) {
            Log.d(TAG, "emailparser: "+e);


        }

        return email.toLowerCase();
    }

    private String numberparser(JSONObject officialData_json) {
        String phone = "";

        try {
            JSONArray urls = (JSONArray) officialData_json.get("phones");
            phone = urls.get(0).toString();
        } catch (Exception e) {
            Log.d(TAG, "numberparser: "+e);


        }

        return phone;
    }

    private String sideparser(JSONObject officialData_json) {
        String party = "";
        try {
            party = officialData_json.getString("party");
        } catch (Exception e) {
            Log.d(TAG, "sideparser: "+e);


        }

        return "( " + party + " )";
    }

    private ArrayList<Channel> channelAll(JSONObject officialData_json) {
        ArrayList<Channel> tempList = new ArrayList<>();
        Channel converter;
        try {
            JSONArray channels = (JSONArray) officialData_json.get("channels");
            for (int i = 0; i < channels.length(); i++) {
                JSONObject channel = (JSONObject) channels.get(i);
                converter = new Channel(channel.getString("type"), channel.getString("id"));
                tempList.add(converter);
            }
        } catch (Exception e) {
            Log.d(TAG, "channelAll: "+e);


        }
        return tempList;
    }

    private String imageparser(JSONObject officialData_json) {
        String photoURL = "";
        try {
            photoURL = officialData_json.getString("photoUrl");
        } catch (Exception e) {
            Log.d(TAG, "imageparser: "+e);


        }
        return photoURL;
    }

    private void allLocation(String data) {
        TextView address = mainActivity.findViewById(R.id.address);
        try {
            JSONObject normalizedInput = new JSONObject(data);
            normalizedInput = normalizedInput.getJSONObject("normalizedInput");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");
            String locationText = (city.equals("") ? "" : city + ", ") + (zip.equals("") ? state : state + ", ") + (zip.equals("") ? "" : zip);
            address.setText(locationText);
        } catch (Exception e) {
            Log.d(TAG, "allLocation: "+e);


        }
    }

    @Override
    protected void onPostExecute(ArrayList<Official> officials) {
        mainActivity.updateOfficialData(officials);
        super.onPostExecute(officials);
    }
}