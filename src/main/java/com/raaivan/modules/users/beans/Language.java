package com.raaivan.modules.users.beans;

import com.raaivan.modules.users.enums.LanguageLevel;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Language {
    private UUID ID;
    private UUID UserID;
    private UUID LanguageID;
    private String LanguageName;
    private LanguageLevel Level;

    public Language(){
        Level = LanguageLevel.None;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }

    public UUID getLanguageID() {
        return LanguageID;
    }

    public void setLanguageID(UUID languageID) {
        LanguageID = languageID;
    }

    public String getLanguageName() {
        return LanguageName;
    }

    public void setLanguageName(String languageName) {
        LanguageName = languageName;
    }

    public LanguageLevel getLevel() {
        return Level;
    }

    public void setLevel(LanguageLevel level) {
        Level = level;
    }
}
