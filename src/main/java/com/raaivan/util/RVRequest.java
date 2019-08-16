package com.raaivan.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequestScope
public class RVRequest {
    private HttpServletRequest request;

    @Autowired
    public void _setDependencies(HttpServletRequest request){
        if(this.request == null) this.request = request;
    }

    private Map<String, String> map;
    private Authentication authentication;
    private UUID CurrentUserID;
    private UUID ApplicationID;

    @PostConstruct
    public void postConstruct(){
        this.ApplicationID = UUID.fromString("08C72552-4F2C-473F-B3B0-C2DACF8CD6A9"); //////////to be removed

        this.authentication = SecurityContextHolder.getContext().getAuthentication();

        try{
            this.CurrentUserID = UUID.fromString(((User) this.authentication.getDetails()).getUsername());
        }catch (Exception e){
            this.CurrentUserID = UUID.fromString("6B9E8414-C1EA-4E59-8AA8-34B4BCEB74E7"); //////////to be removed
        }

        Map<String, String> reqParams = new HashMap<String, String>();

        request.getParameterMap().forEach((key, values) -> {
            if(values.length == 0) reqParams.put(key.toLowerCase(), null);
            else reqParams.put(key.toLowerCase(), values[0]);
        });

        this.map = reqParams;
    }

    public boolean isAuthenticated(){
        return this.CurrentUserID != null && authentication.isAuthenticated();
    }

    public UUID getCurrentUserID(){
        return this.CurrentUserID;
    }

    public UUID getApplicationID() {
        return ApplicationID;
    }

    public int getInt(String name){
        return Integer.parseInt(map.get(name.toLowerCase()));
    }

    public String getString(String name){
        return map.get(name.toLowerCase());
    }

    public String getString(String name, String defaultValue){
        return map.getOrDefault(name.toLowerCase(), defaultValue);
    }

    public String getString(String name, boolean decode){
        String val = map.get(name.toLowerCase());
        return decode && val != null ? Base64.decode(val) : val;
    }

    public String getString(String name, boolean decode, String defaultValue){
        String val = map.getOrDefault(name.toLowerCase(), defaultValue);
        return decode && val != null && !val.equals(defaultValue) ? Base64.decode(val) : val;
    }
}
