package com.cmpe277.petMap.database;

import android.util.Log;

import com.cmpe277.petMap.viewmodel.PetFinderResponse;

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
        String image = null;
        if(petJson.getJSONArray("photos").length()>0) {
            image = petJson.getJSONArray("photos").getJSONObject(0).getString("large");

            image = image.replaceAll("\\\\", "");
            Log.d("Image ",image);
        }
        petEntity pet = new petEntity(id,date,name,image);
        if(petJson.getJSONObject("breeds")!=null && petJson.getJSONObject("breeds").getString("primary")!=null){
            pet.setBreed(petJson.getJSONObject("breeds").getString("primary"));
        }
        if(petJson.getString("age")!=null){
            pet.setAge(petJson.getString("age"));
        }
        pet.setDescription(petJson.getString("description"));
        pet.setGender(petJson.getString("gender"));
        pet.setSize(petJson.getString("size"));
        JSONObject address = petJson.getJSONObject("contact").getJSONObject("address");
        String add ="";
        if(address.getString("address1")!=null){
            add+=address.getString("address1");
        }
        if(address.getString("address2")!=null){
            add+=","+address.getString("address2");
        }
        if(address.getString("city")!=null){
            add+=","+address.getString("city");
        }
        if(address.getString("state")!=null){
            add+=","+address.getString("state");
        }
        if(address.getString("postcode")!=null){
            add+=","+address.getString("postcode");
        }
        pet.setAddress(add);
        pet.setEmail(petJson.getJSONObject("contact").getString("email"));
        pet.setPhone(petJson.getJSONObject("contact").getString("phone"));

        // Create the weather entry object
        return pet;
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
