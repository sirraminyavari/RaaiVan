package com.raaivan.modules.forms.beans;

import com.raaivan.util.Base64;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = BeanDefinition.SCOPE_PROTOTYPE)
public class PollAbstractValue {
    private String TextValue;
    private Double NumberValue;
    private Boolean BitValue;
    private Integer Count;

    public String getTextValue() {
        return TextValue;
    }

    public void setTextValue(String textValue) {
        TextValue = textValue;
    }

    public Double getNumberValue() {
        return NumberValue;
    }

    public void setNumberValue(Double numberValue) {
        NumberValue = numberValue;
    }

    public Boolean getBitValue() {
        return BitValue;
    }

    public void setBitValue(Boolean bitValue) {
        BitValue = bitValue;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public Boolean hasValue()
    {
        return !StringUtils.isBlank(TextValue) || NumberValue != null || BitValue != null;
    }

    public Object getValue(boolean encode)
    {
        if (!StringUtils.isBlank(TextValue)) return encode ? Base64.encode(TextValue) : TextValue;
        else if (NumberValue != null) return NumberValue;
        else if (BitValue != null) return BitValue;
        else return null;
    }
}
