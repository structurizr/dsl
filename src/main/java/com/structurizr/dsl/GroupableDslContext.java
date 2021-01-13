package com.structurizr.dsl;

import com.structurizr.util.StringUtils;

abstract class GroupableDslContext extends DslContext {

    private String group;

    GroupableDslContext() {
        this(null);
    }

    GroupableDslContext(String group) {
        this.group = group;
    }

    boolean hasGroup() {
        return !StringUtils.isNullOrEmpty(group);
    }

    String getGroup() {
        return group;
    }

}