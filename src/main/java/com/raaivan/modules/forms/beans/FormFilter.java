package com.raaivan.modules.forms.beans;

import org.joda.time.DateTime;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class FormFilter {
    private UUID OwnerID;
    private UUID ElementID;
    private String Text;
    private List<String> TextItems;
    private Boolean Or;
    private Boolean Exact;
    private DateTime DateFrom;
    private DateTime DateTo;
    private Double FloatFrom;
    private Double FloatTo;
    private Boolean Bit;
    private UUID Guid;
    private List<UUID> GuidItems;

    public FormFilter()
    {
        TextItems = new ArrayList<>();
        GuidItems = new ArrayList<>();
    }

    public UUID getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(UUID ownerID) {
        OwnerID = ownerID;
    }

    public UUID getElementID() {
        return ElementID;
    }

    public void setElementID(UUID elementID) {
        ElementID = elementID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public List<String> getTextItems() {
        return TextItems;
    }

    public void setTextItems(List<String> textItems) {
        TextItems = textItems;
    }

    public Boolean getOr() {
        return Or;
    }

    public void setOr(Boolean or) {
        Or = or;
    }

    public Boolean getExact() {
        return Exact;
    }

    public void setExact(Boolean exact) {
        Exact = exact;
    }

    public DateTime getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(DateTime dateFrom) {
        DateFrom = dateFrom;
    }

    public DateTime getDateTo() {
        return DateTo;
    }

    public void setDateTo(DateTime dateTo) {
        DateTo = dateTo;
    }

    public Double getFloatFrom() {
        return FloatFrom;
    }

    public void setFloatFrom(Double floatFrom) {
        FloatFrom = floatFrom;
    }

    public Double getFloatTo() {
        return FloatTo;
    }

    public void setFloatTo(Double floatTo) {
        FloatTo = floatTo;
    }

    public Boolean getBit() {
        return Bit;
    }

    public void setBit(Boolean bit) {
        Bit = bit;
    }

    public UUID getGuid() {
        return Guid;
    }

    public void setGuid(UUID guid) {
        Guid = guid;
    }

    public List<UUID> getGuidItems() {
        return GuidItems;
    }

    public void setGuidItems(List<UUID> guidItems) {
        GuidItems = guidItems;
    }
}
