package com.raaivan.modules.forms.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class PollAbstract {
    private Integer TotalCount;
    private UUID ElementID;
    private List<PollAbstractValue> Values;
    private Double Min;
    private Double Max;
    private Double Avg;
    private Double Var;
    private Double StDev;

    public PollAbstract() {
        Values = new ArrayList<>();
    }

    public Integer getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(Integer totalCount) {
        TotalCount = totalCount;
    }

    public UUID getElementID() {
        return ElementID;
    }

    public void setElementID(UUID elementID) {
        ElementID = elementID;
    }

    public List<PollAbstractValue> getValues() {
        return Values;
    }

    public void setValues(List<PollAbstractValue> values) {
        Values = values;
    }

    public Double getMin() {
        return Min;
    }

    public void setMin(Double min) {
        Min = min;
    }

    public Double getMax() {
        return Max;
    }

    public void setMax(Double max) {
        Max = max;
    }

    public Double getAvg() {
        return Avg;
    }

    public void setAvg(Double avg) {
        Avg = avg;
    }

    public Double getVar() {
        return Var;
    }

    public void setVar(Double var) {
        Var = var;
    }

    public Double getStDev() {
        return StDev;
    }

    public void setStDev(Double stDev) {
        StDev = stDev;
    }
}
