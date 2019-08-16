package com.raaivan.modules.dataexchange.beans;

import com.raaivan.modules.users.beans.Password;
import com.raaivan.util.RVBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class ExchangeUser {
    private String UserName;
    private String NewUserName;
    private String FirstName;
    private String LastName;
    private String DepartmentID;
    private Boolean IsManager;
    private String Email;
    private Boolean ResetPassword;
    private Password Password;

    public ExchangeUser(){
        this.Password = RVBeanFactory.getBean(Password.class);
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNewUserName() {
        return NewUserName;
    }

    public void setNewUserName(String newUserName) {
        NewUserName = newUserName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public Boolean getManager() {
        return IsManager;
    }

    public void setManager(Boolean manager) {
        IsManager = manager;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Boolean getResetPassword() {
        return ResetPassword;
    }

    public void setResetPassword(Boolean resetPassword) {
        ResetPassword = resetPassword;
    }

    public com.raaivan.modules.users.beans.Password getPassword() {
        return Password;
    }

    public void setPassword(com.raaivan.modules.users.beans.Password password) {
        Password = password;
    }
}
