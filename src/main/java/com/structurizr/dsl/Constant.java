package com.structurizr.dsl;

class Constant {

    private String name;
    private String value;

    Constant(String name, String value) {
        this.name = name;
        this.value = value;
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }

}