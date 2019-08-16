package com.raaivan.modules.users.beans;

import com.raaivan.modules.users.enums.PhoneType;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class PhoneNumber {
    private UUID NumberID;
    private UUID UserID;
    private String Number;
    private PhoneType PhoneType;

    public PhoneNumber(){
        this.PhoneType = PhoneType.NotSet;
    }

    public UUID getNumberID() {
        return NumberID;
    }

    public void setNumberID(UUID numberID) {
        NumberID = numberID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public com.raaivan.modules.users.enums.PhoneType getPhoneType() {
        return PhoneType;
    }

    public void setPhoneType(com.raaivan.modules.users.enums.PhoneType phoneType) {
        PhoneType = phoneType;
    }
}
