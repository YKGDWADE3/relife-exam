package com.tw.relife;

public class PojoReturnValue {
    String name;

    public PojoReturnValue setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
