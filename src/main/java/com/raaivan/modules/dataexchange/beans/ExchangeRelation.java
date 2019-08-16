package com.raaivan.modules.dataexchange.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class ExchangeRelation {
    private String SourceTypeAdditionalID;
    private String SourceAdditionalID;
    private UUID SourceID;
    private String DestinationTypeAdditionalID;
    private String DestinationAdditionalID;
    private UUID DestinationID;
    private Boolean Bidirectional;

    public String getSourceTypeAdditionalID() {
        return SourceTypeAdditionalID;
    }

    public void setSourceTypeAdditionalID(String sourceTypeAdditionalID) {
        SourceTypeAdditionalID = sourceTypeAdditionalID;
    }

    public String getSourceAdditionalID() {
        return SourceAdditionalID;
    }

    public void setSourceAdditionalID(String sourceAdditionalID) {
        SourceAdditionalID = sourceAdditionalID;
    }

    public UUID getSourceID() {
        return SourceID;
    }

    public void setSourceID(UUID sourceID) {
        SourceID = sourceID;
    }

    public String getDestinationTypeAdditionalID() {
        return DestinationTypeAdditionalID;
    }

    public void setDestinationTypeAdditionalID(String destinationTypeAdditionalID) {
        DestinationTypeAdditionalID = destinationTypeAdditionalID;
    }

    public String getDestinationAdditionalID() {
        return DestinationAdditionalID;
    }

    public void setDestinationAdditionalID(String destinationAdditionalID) {
        DestinationAdditionalID = destinationAdditionalID;
    }

    public UUID getDestinationID() {
        return DestinationID;
    }

    public void setDestinationID(UUID destinationID) {
        DestinationID = destinationID;
    }

    public Boolean getBidirectional() {
        return Bidirectional;
    }

    public void setBidirectional(Boolean bidirectional) {
        Bidirectional = bidirectional;
    }
}
