package com.raaivan.modules.rv.beans;

import java.util.UUID;

public class MutableUUID {
    private UUID Value;

    public MutableUUID(UUID value){
        setValue(value);
    }

    public MutableUUID(){ Value = null; }

    public UUID getValue() {
        return Value;
    }

    public void setValue(UUID value) {
        Value = value;
    }

    public String toString(){
        return Value.toString();
    }
}
