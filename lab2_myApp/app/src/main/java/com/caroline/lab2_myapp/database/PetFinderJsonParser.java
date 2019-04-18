package com.caroline.lab2_myapp.database;

import android.util.Log;

import com.caroline.lab2_myapp.viewmodel.PetFinderResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

class PetFinderJsonParser {

    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String OWM_LIST = "list";

    private static petEntity[] fromJson(final JSONObject forecastJson) throws JSONException {
        Log.d("JSON",forecastJson.toString());
        JSONArray jsonPetArray = forecastJson.getJSONArray("animals");

        petEntity[] petEntities = new petEntity[jsonPetArray.length()];

        /*
         * OWM returns daily forecasts based upon the local time of the city that is being asked
         * for, which means that we need to know the GMT offset to translate this data properly.
         * Since this data is also sent in-order and the first day is always the current day, we're
         * going to take advantage of that to get a nice normalized UTC date for all of our weather.
         */
        //long normalizedUtcStartDay = SunshineDateUtils.getNormalizedUtcMsForToday();
        Log.d("Result JSON Length",""+jsonPetArray.length());
        for (int i = 0; i < jsonPetArray.length(); i++) {
            // Get the JSON object representing the day
            JSONObject petJson = jsonPetArray.getJSONObject(i);

            // Create the weather entry object
            //long dateTimeMillis = normalizedUtcStartDay + SunshineDateUtils.DAY_IN_MILLIS * i;
            petEntity pet = fromJsonObj(petJson);

            petEntities[i] = pet;
        }
        return petEntities;
    }
    private static petEntity fromJsonObj(final JSONObject petJson
                                         ) throws JSONException {
        // We ignore all the datetime values embedded in the JSON and assume that
        // the values are returned in-order by day (which is not guaranteed to be correct).

        long id = petJson.getLong("id");
        String name = petJson.getString("name");
        String date = petJson.getString("published_at");
        String image = petJson.getString("full");
        image = image.replaceAll("\\\\","");
        Log.d("Image ",image);


        // Create the weather entry object
        return new petEntity(id,date,name,image);
    }

    private static boolean hasHttpError(JSONObject forecastJson) throws JSONException {
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    return false;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    // Location invalid
                default:
                    // Server probably down
                    return true;
            }
        }
        return false;
    }

    public PetFinderResponse parse(String jsonPetsResponse) throws JSONException {
        JSONObject petJson = new JSONObject(jsonPetsResponse);

        // Is there an error?
        if (hasHttpError(petJson)) {
            return null;
        }

        petEntity[] petEntities = fromJson(petJson);

        return new PetFinderResponse(petEntities);
    }
}
