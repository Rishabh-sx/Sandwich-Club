package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = null;

        final String KEY_NAME = "name";
        final String KEY_MAIN_NAME = "mainName";
        final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
        final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
        final String KEY_DESCRIPTION = "description";
        final String KEY_IMAGE_URL = "image";
        final String KEY_INGREDIENTS = "ingredients";


        try {

            //rootJsonObject is the root of the JSON.
            JSONObject jsonObj, nameObj;

            sandwich = new Sandwich();

            jsonObj = new JSONObject(json);

            nameObj = jsonObj.getJSONObject(KEY_NAME);

            sandwich.setMainName(nameObj.optString(KEY_MAIN_NAME));
            sandwich.setAlsoKnownAs(jsonArrayValuesToList(nameObj.getJSONArray(KEY_ALSO_KNOWN_AS)));

            sandwich.setPlaceOfOrigin(jsonObj.optString(KEY_PLACE_OF_ORIGIN));
            sandwich.setDescription(jsonObj.optString(KEY_DESCRIPTION));
            sandwich.setImage(jsonObj.optString(KEY_IMAGE_URL));
            sandwich.setIngredients(jsonArrayValuesToList(jsonObj.getJSONArray(KEY_INGREDIENTS)));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    /**
     * Method parse json array and returns list
     *
     * @param jsonArray
     * @return myList
     */
    private static List<String> jsonArrayValuesToList(JSONArray jsonArray) {
        List<String> stringList = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                stringList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringList;
    }
}