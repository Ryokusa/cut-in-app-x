package com.ryokusasa.cut_in_app.AnimObj;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
//gsonによるデシリアライズ用
//参考 https://ja.getdocs.org/gson-list/
//代わりがあったので未使用
public class AnimObjDeserializer implements JsonDeserializer<AnimObj> {
    private Gson gson;
    private Map<String, Class<? extends AnimObj>> map = new HashMap<>();

    public AnimObjDeserializer(){
        this.gson = new Gson();
        this.map = new HashMap<>();
    }

    public void registerType(String type, Class<? extends AnimObj> animObjClass){
        map.put(type, animObjClass);
    }

    public AnimObj deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject animObjObject = json.getAsJsonObject();
        JsonElement animObjTypeElement = animObjObject.get("TAG");
        Class<? extends AnimObj> animObjType = map.get(animObjTypeElement.getAsString());
        return gson.fromJson(animObjObject, animObjType);
    }
}
