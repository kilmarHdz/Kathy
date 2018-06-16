package com.gomar.parcial2_00011616.API.Deserializer;

import com.gomar.parcial2_00011616.API.Response.FavoriteResponse;
import com.gomar.parcial2_00011616.API.Response.NewsResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavDeserializer implements JsonDeserializer<FavoriteResponse> {

    @Override
    public FavoriteResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        final NewsResponse[] favListResponse = context.deserialize(object.get("favoriteNews"),NewsResponse[].class);
        final List<String> favoritesNewsID = new ArrayList<>();

        for(NewsResponse response:favListResponse){
            favoritesNewsID.add(response.get_id());
        }

        return new FavoriteResponse(favoritesNewsID);
    }
}
