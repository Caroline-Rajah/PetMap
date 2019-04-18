package com.caroline.lab2_myapp.util;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    /*
     * Sunshine was originally built to use OpenWeatherMap's API. However, we wanted to provide
     * a way to much more easily test the app and provide more varied weather data. After all, in
     * Mountain View (Google's HQ), it gets very boring looking at a forecast of perfectly clear
     * skies at 75°F every day... (UGH!) The solution we came up with was to host our own fake
     * weather server. With this server, there are two URL's you can use. The first (and default)
     * URL will return dynamic weather data. Each time the app refreshes, you will get different,
     * completely random weather data. This is incredibly useful for testing the robustness of your
     * application, as different weather JSON will provide edge cases for some of your methods.
     *
     */
    private static final String DYNAMIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/weather";

    private static final String TOKEN_URL = "https://api.petfinder.com/v2/oauth2/token";

    private static final String GET_ANIMALS = "https://api.petfinder.com/v2/animals";

    private static final String STATIC_WEATHER_URL =
            "https://andfun-weather.udacity.com/staticweather";

    private static final String FORECAST_BASE_URL = DYNAMIC_WEATHER_URL;

    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API. If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this training.
     */
    private static String accessToken;
    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";


    /* The query parameter allows us to provide a location string to the API */
    private static final String QUERY_PARAM = "q";

    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    private static final String FORMAT_PARAM = "mode";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";
    /* The days parameter allows us to designate how many days of weather data we want */
    private static final String DAYS_PARAM = "cnt";

    /**
     * Retrieves the proper URL to query for the weather data.
     *
     * @return URL to query weather service
     */
    public static URL getUrl() {
        String locationQuery = "Mountain View, CA";
        return buildUrlWithToken();
    }
    public static URL buildTokenUrl(){
        Uri tokenUri = Uri.parse(TOKEN_URL).buildUpon().build();

        try{
            URL TokenUrl = new URL(tokenUri.toString());
            Log.v(TAG, "URL: " + TokenUrl);
            return TokenUrl;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }



    private static URL buildUrlWithToken(){
        Uri fetchAnimalsUri = Uri.parse(GET_ANIMALS).buildUpon().build();
        try{
            URL fetchAnimalsUrl = new URL(fetchAnimalsUri.toString());
            return fetchAnimalsUrl;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static URL buildUrlWithType(String type){
        Uri fetchAnimalsUri = Uri.parse(GET_ANIMALS).buildUpon().appendQueryParameter("type",type).appendQueryParameter("page","1").build();
        try{
            URL fetchAnimalsUrl = new URL(fetchAnimalsUri.toString());
            return fetchAnimalsUrl;
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static String getResponseFromCustomHttpUrl(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String auth = ("Bearer " + accessToken);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("authorization", auth)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "52b90249-f5da-b1d8-c00a-873a59fae506")
                .build();
        Log.d("URL", request.toString());
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url,String token) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String auth = ("Bearer "+accessToken);
        Request request = new Request.Builder()
                .url("https://api.petfinder.com/v2/animals?type=dog&page=2")
                .get()
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("authorization", auth)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "52b90249-f5da-b1d8-c00a-873a59fae506")
                .build();
        Log.d("URL",request.toString());
        Response response = client.newCall(request).execute();

        return response.body().string();
        /*HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String basicAuth = "Basic " + Base64.encode(token.getBytes(),Base64.DEFAULT);
        urlConnection.setRequestProperty("Authorization",basicAuth);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }*/
    }
    public static String gettokenFromHttpUrl(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=xDyz0pfevmZASNztxDeiTqzwOJv6PLykcqvAZvam8ZfH0BeZnm&client_secret=4HQoC4clr0Q404dEddxC2lUtq7tVYCteoDT2XUJL");
        Request request = new Request.Builder()
                .url("https://api.petfinder.com/v2/oauth2/token")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "13353bee-82c5-8288-7089-7bac4307d3b9")
                .build();

        Response response = client.newCall(request).execute();
        String respose = response.body().string();
        try{
        JSONObject responseJson = new JSONObject(respose);
        accessToken = responseJson.getString("access_token");
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("token",respose);
        Log.d("Token", accessToken);
        return respose;
    }
        /*HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        JSONObject names =new JSONObject();
        try{
        names.put("grant_type","client_credentials");
        names.put("client_id","xDyz0pfevmZASNztxDeiTqzwOJv6PLykcqvAZvam8ZfH0BeZnm");
        names.put("client_secret","4HQoC4clr0Q404dEddxC2lUtq7tVYCteoDT2XUJL");}
        catch (JSONException e){
            e.printStackTrace();
        }

        String data = names.toString();
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(data);
        out.close();
        try {
            InputStream in = conn.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            Log.d("token",response);
            return response;
        } finally {
            conn.disconnect();
        }

    }*/
}
