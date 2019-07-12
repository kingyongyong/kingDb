package com.sqy.kingdb.bean;

public class Condition {
    private String key;
    private Object value;

    public Condition(String key, Object value) {
        this.key = key;
        if(value instanceof Boolean){
            boolean newResult = (boolean) value;
            value = newResult ? "1" : "0";
        }
        this.value = value;
    }

    public Condition() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if(value instanceof Boolean){
            boolean newResult = (boolean) value;
            value = newResult ? "1" : "0";
        }
        this.value = value;
    }
}
