package com.gomar.parcial2_00011616.Entity.Deserializer;

import com.gomar.parcial2_00011616.Response.NewsResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NewsDeserializer implements JsonDeserializer<NewsResponse> {
@Override
public NewsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)throws JsonParseException {
        NewsResponse notice=new NewsResponse();

        JsonObject repoJsonObject=json.getAsJsonObject();
        notice.set_id(repoJsonObject.get("_id").getAsString());
        notice.setTitle(repoJsonObject.get("title").getAsString());
        notice.setBody(repoJsonObject.get("body").getAsString());
        notice.setGame(repoJsonObject.get("game").getAsString());
        notice.setCreated_date(repoJsonObject.get("created_date").getAsString());
        if(repoJsonObject.get("coverImage")!=null){
        notice.setCoverImage(repoJsonObject.get("coverImage").getAsString());
        }else{
        notice.setCoverImage("http://");
        }
        if(repoJsonObject.get("description")!=null){
        notice.setDescription(repoJsonObject.get("description").getAsString());
        }else{
        notice.setDescription("--*--");
        }

        return notice;
        }
        }