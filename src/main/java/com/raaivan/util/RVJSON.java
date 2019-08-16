package com.raaivan.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class RVJSON extends JSONObject {
    public RVJSON(){
        super();
    }

    public RVJSON(String json){
        super(json);
    }

    public RVJSON add(String key, Object value){
        if(value instanceof ArrayList) value = new JSONArray((ArrayList) value);
        else if(value instanceof List) value = new JSONArray((List) value);
        else if(value instanceof Object[]) value = new JSONArray((Object[]) value);

        super.put(key, value);

        return this;
    }

    public static RVJSON fromString(String json){
        try {
            return new RVJSON(json);
        }catch (Exception ex){
            return new RVJSON();
        }
    }
}
