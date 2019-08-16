package com.raaivan.web.api.handlers;

import com.raaivan.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class Users {
    private RVRequest request;

    @Autowired
    public void _setDependencies(RVRequest request) {
        if(this.request == null) this.request = request;
    }

    public RVJSON handle(String command){
        return (new RVJSON()).add("Bool", true).add("Int", 2).add("Command", command)
                .add("R", request.getString("ramin", "yavari"))
                .add("Location", request.getString("location", null));
    }
}
