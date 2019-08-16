package com.raaivan.web.api.handlers;

import com.raaivan.modules.users.UsersDAO;
import com.raaivan.modules.users.beans.User;
import com.raaivan.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class RV {
    private PublicMethods publicMethods;
    private RVRequest request;
    private RVAuthentication rvAuthentication;
    private UsersDAO usersDAO;

    @Autowired
    public void _setDependencies(PublicMethods publicMethods, RVRequest request, RVAuthentication rvAuthentication, UsersDAO usersDAO) {
        if(this.publicMethods == null) this.publicMethods = publicMethods;
        if(this.request == null) this.request = request;
        if(this.rvAuthentication == null) this.rvAuthentication = rvAuthentication;
        if(this.usersDAO == null) this.usersDAO = usersDAO;
    }

    public RVJSON handle(String command) {
        switch (command) {
            case "login":
                return login(request.getString("username", true),
                        request.getString("password", true));
            default:
                return (new RVJSON()).add("Authenticated", request.isAuthenticated()).add("UserID", request.getCurrentUserID())
                        .add("Command", command);
        }

        /*
        return (new RVJSON()).add("Bool", true).add("Int", 2).add("Command", command)
                .add("R", request.getString("ramin", "yavari"))
                .add("Location", request.getString("location", null));
        */
    }

    private RVJSON login(String username, String password) {
        /*
        boolean sent = publicMethods.sendEmail(appId, "sir.raminyavari@gmail.com", "test",
                "this email has been sent for test purposes", "sales@raaivan.ir", "Hrgn35#7",
                "rv java version", "mail.raaivan.ir", 587, false);
        System.out.println("Email Result: " + sent);
        */

        try {
            File f = new ClassPathResource("/static").getFile();
            System.out.println(f.getAbsolutePath());
        }catch (Exception ex){
            System.out.println(ex.toString());
        }


        List<UUID> userIds = new ArrayList<>();
        userIds.add(request.getCurrentUserID());

        List<String> usernames = new ArrayList<>();
        usernames.add("admin");

        List<User> users = usersDAO.getUsers(request.getApplicationID(), userIds);

        List<UUID> ids = usersDAO.getUserIDs(request.getApplicationID(), usernames);

        if (users.size() > 0) return users.get(0).toJson()
                .add("IDs", ids.stream().map(UUID::toString).collect(Collectors.joining(",")))
                .add("URL_UserName", request.getString("username", false));
        else return (new RVJSON()).add("Error", ":(");

        /*
        boolean authenticated = rvAuthentication.authenticate(username, password);
        return (new RVJSON()).add("Authenticated", authenticated).add("UserName", username)
                .add("UsersCount", 12).add("Sha1", PublicMethods.sha1("admin"))
                .add("Salted", UserUtilities.encodePassword("y4e9fuwyu0fu9", "1BDFjZVPSa3qyXQNwKQyaQ=="));
        */
    }
}