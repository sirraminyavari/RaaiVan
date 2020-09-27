package com.raaivan.web.api;

import com.raaivan.util.RVJSON;
import com.raaivan.web.api.handlers.RV;
import com.raaivan.web.api.handlers.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Map;

@CrossOrigin
@RestController
@ApplicationScope
public class API {
    private Users users;
    private RV rv;

    @Autowired
    public void _setDependencies(Users users, RV rv){
        this.users = users;
        this.rv = rv;
    }

    @RequestMapping(path = "/api/{handler}/{command}", produces = "application/json; charset=UTF-8")
    public Map<String, Object> executeCommand(@PathVariable String handler, @PathVariable String command){
        return handle(handler, command).toMap();
    }

    @RequestMapping(path = "/api/xml/{handler}/{command}", produces = { "application/xml; charset=UTF-8", "text/xml; charset=UTF-8" })
    public Map<String, Object> executeCommandXML(@PathVariable String handler, @PathVariable String command){
        return handle(handler, command).toMap();
    }

    public RVJSON _handle(String handlerName, String command){
        handlerName = handlerName.toLowerCase();
        command = command.toLowerCase();

        switch (handlerName) {
            case "users": return users.handle(command);
            case "rv": return  rv.handle(command);
            default: return (new RVJSON()).add("ErrorText", "HandlerNotFound");
        }
    }

    public RVJSON handle(String handlerName, String command){
        return _handle(handlerName, command);
    }
}
