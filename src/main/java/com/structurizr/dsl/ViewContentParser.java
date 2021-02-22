package com.structurizr.dsl;

import com.structurizr.model.*;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ElementView;
import com.structurizr.view.StaticView;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

abstract class ViewContentParser extends AbstractParser {

    private static final String WILDCARD = "*";

    protected Set<Relationship> findRelationships(ViewDslContext context, String sourceElementIdentifier, String destinationElementIdentifier) {
        Set<Element> sourceElements = new HashSet<>();
        Set<Element> destinationElements = new HashSet<>();
        Set<Relationship> relationships = new HashSet<>();

        if (sourceElementIdentifier.equals(WILDCARD)) {
            sourceElements.addAll(context.getView().getElements().stream().map(ElementView::getElement).collect(Collectors.toSet()));
        } else {
            Element sourceElement = context.getElement(sourceElementIdentifier);
            if (sourceElement == null) {
                throw new RuntimeException("The element \"" + sourceElementIdentifier + "\" does not exist");
            }

            if (!context.getView().isElementInView(sourceElement)) {
                throw new RuntimeException("The element \"" + sourceElementIdentifier + "\" does not exist in the view");
            }

            sourceElements.add(sourceElement);
        }

        if (destinationElementIdentifier.equals(WILDCARD)) {
            destinationElements.addAll(context.getView().getElements().stream().map(ElementView::getElement).collect(Collectors.toSet()));
        } else {
            Element destinationElement = context.getElement(destinationElementIdentifier);
            if (destinationElement == null) {
                throw new RuntimeException("The element \"" + destinationElementIdentifier + "\" does not exist");
            }

            if (!context.getView().isElementInView(destinationElement)) {
                throw new RuntimeException("The element \"" + destinationElementIdentifier + "\" does not exist in the view");
            }

            destinationElements.add(destinationElement);
        }

        for (Element sourceElement : sourceElements) {
            for (Relationship relationship : sourceElement.getRelationships()) {
                if (destinationElements.contains(relationship.getDestination())) {
                    relationships.add(relationship);
                }
            }
        }

        return relationships;
    }

}