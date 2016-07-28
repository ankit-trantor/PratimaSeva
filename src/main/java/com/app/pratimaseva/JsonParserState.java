package com.app.pratimaseva;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dharmik on 24-07-2016.
 */
public class JsonParserState {
    public static ArrayList<String> Parse(String json){
        try {
            ArrayList<String> StateList = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(json);
            JSONArray cast = jsonResponse.getJSONArray("states");
            for (int i=0; i<cast.length(); i++) {
                JSONObject actor = cast.getJSONObject(i);
                StateList.add(actor.getString("state_name"));
            }
            return StateList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
