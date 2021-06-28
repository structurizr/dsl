package com.structurizr.dsl;

import com.structurizr.model.GroupableElement;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Person;

final class PersonDslContext extends GroupableElementDslContext {

    private Person person;

    PersonDslContext(Person person) {
        this.person = person;
    }

    Person getPerson() {
        return person;
    }

    @Override
    ModelItem getModelItem() {
        return getPerson();
    }

    @Override
    GroupableElement getElement() {
        return person;
    }

}