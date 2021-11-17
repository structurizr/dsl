package com.structurizr.dsl;

import com.structurizr.model.Enterprise;
import com.structurizr.model.Location;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelDslContextTests extends AbstractTests {

    @Test
    void end_DoesNothing_WhenNoPeopleAreMarkedAsInternal() {
        ModelDslContext context = new ModelDslContext();
        context.setWorkspace(workspace);

        Person user1 = workspace.getModel().addPerson("Name 1");
        Person user2 = workspace.getModel().addPerson("Name 2");
        assertEquals(Location.Unspecified, user1.getLocation());
        assertEquals(Location.Unspecified, user2.getLocation());

        context.end();
        assertEquals(Location.Unspecified, user1.getLocation());
        assertEquals(Location.Unspecified, user2.getLocation());
    }

    @Test
    void end_MarksAllOtherPeopleAsExternal_WhenSomePeopleAreMarkedAsInternal() {
        ModelDslContext context = new ModelDslContext();
        context.setWorkspace(workspace);
        workspace.getModel().setEnterprise(new Enterprise("Name"));

        Person user1 = workspace.getModel().addPerson("Name 1");
        Person user2 = workspace.getModel().addPerson("Name 2");
        user2.setLocation(Location.Internal);
        assertEquals(Location.Unspecified, user1.getLocation());
        assertEquals(Location.Internal, user2.getLocation());

        context.end();
        assertEquals(Location.External, user1.getLocation());
        assertEquals(Location.Internal, user2.getLocation());
    }

    @Test
    void end_DoesNothing_WhenNoSoftwareSystemsAreMarkedAsInternal() {
        ModelDslContext context = new ModelDslContext();
        context.setWorkspace(workspace);

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Name 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Name 2");
        assertEquals(Location.Unspecified, softwareSystem1.getLocation());
        assertEquals(Location.Unspecified, softwareSystem2.getLocation());

        context.end();
        assertEquals(Location.Unspecified, softwareSystem1.getLocation());
        assertEquals(Location.Unspecified, softwareSystem2.getLocation());
    }

    @Test
    void end_MarksAllOtherSoftwareSystemsAsExternal_WhenSomeSoftwareSystemsAreMarkedAsInternal() {
        ModelDslContext context = new ModelDslContext();
        context.setWorkspace(workspace);
        workspace.getModel().setEnterprise(new Enterprise("Name"));

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Name 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Name 2");
        softwareSystem1.setLocation(Location.Internal);
        assertEquals(Location.Internal, softwareSystem1.getLocation());
        assertEquals(Location.Unspecified, softwareSystem2.getLocation());

        context.end();
        assertEquals(Location.Internal, softwareSystem1.getLocation());
        assertEquals(Location.External, softwareSystem2.getLocation());
    }

}