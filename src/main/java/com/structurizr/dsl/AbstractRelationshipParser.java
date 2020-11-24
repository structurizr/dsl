package com.structurizr.dsl;

import com.structurizr.model.*;

abstract class AbstractRelationshipParser extends AbstractParser {

    protected Relationship createRelationship(Element sourceElement, String description, String technology, String[] tags, Element destinationElement) {
        Relationship relationship = null;

        if (sourceElement instanceof StaticStructureElement && destinationElement instanceof StaticStructureElement) {
            relationship = ((StaticStructureElement)sourceElement).uses((StaticStructureElement)destinationElement, description, technology, null, tags);
        } else if (sourceElement instanceof DeploymentNode && destinationElement instanceof DeploymentNode) {
            relationship = ((DeploymentNode)sourceElement).uses((DeploymentNode)destinationElement, description, technology, null, tags);
        } else if (sourceElement instanceof InfrastructureNode && destinationElement instanceof DeploymentElement) {
            relationship = ((InfrastructureNode)sourceElement).uses((DeploymentElement)destinationElement, description, technology, null, tags);
        } else if (sourceElement instanceof StaticStructureElementInstance && destinationElement instanceof InfrastructureNode) {
            relationship = ((StaticStructureElementInstance)sourceElement).uses((InfrastructureNode)destinationElement, description, technology, null, tags);
        } else {
            throw new RuntimeException("A relationship between \"" + sourceElement.getCanonicalName() + "\" and \"" + destinationElement.getCanonicalName() + "\" is not permitted");
        }

        return relationship;
    }

}