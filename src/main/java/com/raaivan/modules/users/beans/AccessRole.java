package com.raaivan.modules.users.beans;

import com.raaivan.modules.users.enums.AccessRoleName;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class AccessRole {
    private UUID RoleID;
    private AccessRoleName Name;
    private String Title;

    public AccessRole(){
        Name = AccessRoleName.None;
    }

    public UUID getRoleID() {
        return RoleID;
    }

    public void setRoleID(UUID roleID) {
        RoleID = roleID;
    }

    public AccessRoleName getName() {
        return Name;
    }

    public void setName(AccessRoleName name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
