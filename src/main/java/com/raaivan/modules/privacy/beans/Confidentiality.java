package com.raaivan.modules.privacy.beans;

import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class Confidentiality {
    private UUID ObjectID;
    private UUID LevelID;

    public UUID getObjectID() {
        return ObjectID;
    }

    public void setObjectID(UUID objectID) {
        ObjectID = objectID;
    }

    public UUID getLevelID() {
        return LevelID;
    }

    public void setLevelID(UUID levelID) {
        LevelID = levelID;
    }
}
