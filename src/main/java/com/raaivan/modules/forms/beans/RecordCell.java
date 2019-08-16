package com.raaivan.modules.forms.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class RecordCell {
    private UUID ElementID;
    private String Value;

    public RecordCell(UUID elementId, String value)
    {
        ElementID = elementId;
        Value = value;
    }

    public UUID getElementID() {
        return ElementID;
    }

    public void setElementID(UUID elementID) {
        ElementID = elementID;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
