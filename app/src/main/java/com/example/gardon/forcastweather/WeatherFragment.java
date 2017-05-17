package com.example.gardon.forcastweather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    ImageView weatherIcon;
    Double lat, log;
    String town;
    RelativeLayout weather_layout;
    TextView day1, day2, day3;
    TextView celius1, celius2, celius3;
    ImageView status1, status2, status3;
    RelativeLayout lay1,lay2,lay3;

    Handler handler;

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (ImageView) rootView.findViewById(R.id.weather_icon);
        weather_layout = (RelativeLayout) rootView.findViewById(R.id.weather_layout);
        day1 = (TextView) rootView.findViewById(R.id.day1);
        day2 = (TextView) rootView.findViewById(R.id.day2);
        day3 = (TextView) rootView.findViewById(R.id.day3);
        celius1 = (TextView) rootView.findViewById(R.id.celius1);
        celius2 = (TextView) rootView.findViewById(R.id.celius2);
        celius3 = (TextView) rootView.findViewById(R.id.celius3);
        status1 = (ImageView) rootView.findViewById(R.id.status_icon1);
        status2 = (ImageView) rootView.findViewById(R.id.status_icon2);
        status3 = (ImageView) rootView.findViewById(R.id.status_icon3);
        lay1 = (RelativeLayout) rootView.findViewById(R.id.day1_layout);
        lay2 = (RelativeLayout) rootView.findViewById(R.id.day2_layout);
        lay3 = (RelativeLayout) rootView.findViewById(R.id.day3_layout);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
//        updateWeatherData1(new CityPreference(getActivity()).getCity());
        updateWeatherData(lat, log);
    }

    //    private void updateWeatherData1(final String city){
//        new Thread(){
//            public void run(){
//                final JSONObject json = RemoteFetch1.getJSON(getActivity(), city);
//                if(json == null){
//                    handler.post(new Runnable(){
//                        public void run(){
////                            Toast.makeText(getActivity(),
////                                    getActivity().getString(R.string.place_not_found),
////                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } else {
//                    handler.post(new Runnable(){
//                        public void run(){
//                            renderWeather1(json);
//                        }
//                    });
//                }
//            }
//        }.start();
//    }
    private void updateWeatherData(final double latitute, final double longitute) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), latitute, longitute);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
//                            Toast.makeText(getActivity(),
//                                    getActivity().getString(R.string.place_not_found),
//                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                            renderWeather1(json, 1);
                            renderWeather1(json, 2);
                            renderWeather1(json, 3);
                        }
                    });
                }
            }
        }.start();
    }

    //    private void renderWeather1(JSONObject json){
//        try {
//            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
//                    ", " +
//                    json.getJSONObject("sys").getString("country"));
//
//            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//            JSONObject main = json.getJSONObject("main");
//            detailsField.setText(
//                    details.getString("description").toUpperCase(Locale.US) +
//                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
//                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");
//
//            currentTemperatureField.setText(
//                    String.format("%.2f", main.getDouble("temp"))+ " ℃");
//
//            DateFormat df = DateFormat.getDateTimeInstance();
//            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
//            updatedField.setText("Last update: " + updatedOn);
//
//            setWeatherIcon(details.getInt("id"),
//                    json.getJSONObject("sys").getLong("sunrise") * 1000,
//                    json.getJSONObject("sys").getLong("sunset") * 1000);
//
//        }catch(Exception e){
//            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
//        }
//    }
    private void renderWeather(JSONObject json) {
        try {
            JSONObject name = json.getJSONObject("city");
            cityField.setText(town + ", " + name.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    name.getString("country"));
            JSONObject today = json.getJSONArray("list").getJSONObject(0);
            JSONObject details = today.getJSONArray("weather").getJSONObject(0);
            JSONObject main = today.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp")) + " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(today.getLong("dt") * 1000));
            Date date = new Date(today.getLong("dt") * 1000);
            String day = android.text.format.DateFormat.format("EEEE", date).toString();
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"));

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void renderWeather1(JSONObject json, int i) {
        try {
            int id = 5;
            switch (i) {
                case 1:
                    id = 5;
                    break;
                case 2:
                    id = 13;
                    break;
                case 3:
                    id = 21;
                    break;
            }
            JSONObject today = json.getJSONArray("list").getJSONObject(id);
            JSONObject details = today.getJSONArray("weather").getJSONObject(0);
            JSONObject main = today.getJSONObject("main");
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(today.getLong("dt") * 1000));
            Date date = new Date(today.getLong("dt") * 1000);
            String day = android.text.format.DateFormat.format("EEEE", date).toString();
            switch (i) {
                case 1:
                    celius1.setText(
                            String.format("%.2f", main.getDouble("temp")) + " ℃");
                    day1.setText(day);
                    break;
                case 2:
                    celius2.setText(
                            String.format("%.2f", main.getDouble("temp")) + " ℃");
                    day2.setText(day);
                    break;
                case 3:
                    celius3.setText(
                            String.format("%.2f", main.getDouble("temp")) + " ℃");
                    day3.setText(day);
                    break;
            }
            setWeatherIcon1(details.getInt("id"), i);

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId) {
        int id = actualId / 100;
        Bitmap bImage = null;
        Bitmap bBackground = null;
        String icon = "";
        switch (id) {
            case 2:
                icon = getActivity().getString(R.string.weather_thunder);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.weather_thunder);
                weather_layout.setBackgroundResource(R.drawable.thunder);
                break;
            case 3:
                icon = getActivity().getString(R.string.weather_drizzle);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.drizzle);
                weather_layout.setBackgroundResource(R.drawable.drizzle_layout);
                break;
            case 7:
                icon = getActivity().getString(R.string.weather_foggy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.fog_day);
                weather_layout.setBackgroundResource(R.drawable.fog_layout);
                break;
            case 8:
                icon = getActivity().getString(R.string.weather_cloudy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.clouds);
                weather_layout.setBackgroundResource(R.drawable.cloud_layout);
                break;
            case 6:
                icon = getActivity().getString(R.string.weather_snowy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.snow);
                weather_layout.setBackgroundResource(R.drawable.snow_layout);
                break;
            case 5:
                icon = getActivity().getString(R.string.weather_rainy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.rain);
                weather_layout.setBackgroundResource(R.drawable.rain_layout);
                break;
        }
        weatherIcon.setImageBitmap(bImage);
    }

    private void setWeatherIcon1(int actualId, int i) {
        int id = actualId / 100;
        Bitmap bImage = null;
        int idBackground = 0;
        String icon = "";
        switch (id) {
            case 2:
                icon = getActivity().getString(R.string.weather_thunder);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.weather_thunder);
                idBackground = R.drawable.thunder;
                break;
            case 3:
                icon = getActivity().getString(R.string.weather_drizzle);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.drizzle);
                idBackground = R.drawable.drizzle_layout;
                break;
            case 7:
                icon = getActivity().getString(R.string.weather_foggy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.fog_day);
                idBackground = R.drawable.fog_layout;
                break;
            case 8:
                icon = getActivity().getString(R.string.weather_cloudy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.clouds);
                idBackground = R.drawable.cloud_layout;
                break;
            case 6:
                icon = getActivity().getString(R.string.weather_snowy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.snow);
                idBackground = R.drawable.snow_layout;
                break;
            case 5:
                icon = getActivity().getString(R.string.weather_rainy);
                bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.rain);
                idBackground = R.drawable.rain_layout;
                break;
        }
        switch (i) {
            case 1:
                status1.setImageBitmap(bImage);
                lay1.setBackgroundResource(idBackground);
                break;
            case 2:
                status2.setImageBitmap(bImage);
                lay2.setBackgroundResource(idBackground);
                break;
            case 3:
                status3.setImageBitmap(bImage);
                lay3.setBackgroundResource(idBackground);
                break;
        }
    }

    public void changeCity(String city) {
        town = city.toUpperCase(Locale.US);
    }

    public void startCor(double latitute, double longitute) {
        updateWeatherData(latitute, longitute);
    }
}