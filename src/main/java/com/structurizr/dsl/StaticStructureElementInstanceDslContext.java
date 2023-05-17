package com.structurizr.dsl;

import com.structurizr.model.StaticStructureElementInstance;

import javax.annotation.Nonnull;

abstract class StaticStructureElementInstanceDslContext extends ModelItemDslContext {

    @Nonnull
    abstract StaticStructureElementInstance getElementInstance();

}
