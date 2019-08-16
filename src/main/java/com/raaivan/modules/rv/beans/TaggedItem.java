package com.raaivan.modules.rv.beans;

import com.raaivan.modules.rv.enums.TagContextType;
import com.raaivan.modules.rv.enums.TaggedItemType;
import com.raaivan.util.PublicMethods;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class TaggedItem {
    private UUID ContextID;
    private UUID TaggedID;
    private TagContextType ContextType;
    private TaggedItemType TaggedType;

    public TaggedItem()
    {
        ContextType = TagContextType.None;
        TaggedType = TaggedItemType.None;
    }

    public TaggedItem(UUID contextId, UUID taggedId, TagContextType contextType, TaggedItemType taggedType)
    {
        ContextID = contextId;
        TaggedID = taggedId;
        ContextType = contextType;
        TaggedType = taggedType;
    }

    public UUID getContextID() {
        return ContextID;
    }

    public void setContextID(UUID contextID) {
        ContextID = contextID;
    }

    public UUID getTaggedID() {
        return TaggedID;
    }

    public void setTaggedID(UUID taggedID) {
        TaggedID = taggedID;
    }

    public TagContextType getContextType() {
        return ContextType;
    }

    public void setContextType(TagContextType contextType) {
        ContextType = contextType;
    }

    public TaggedItemType getTaggedType() {
        return TaggedType;
    }

    public void setTaggedType(TaggedItemType taggedType) {
        TaggedType = taggedType;
    }
}
