package com.structurizr.dsl;

import com.structurizr.model.*;

import java.util.HashSet;
import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.*;

final class DeploymentViewExpressionParser extends AbstractExpressionParser {

    Set<Element> evaluateElementExpression(String expr, ViewDslContext context) {
        Set<Element> elements = new HashSet<>();

        if (expr.toLowerCase().startsWith(ELEMENT_TYPE_EQUALS_EXPRESSION)) {
            String type = expr.substring(ELEMENT_TYPE_EQUALS_EXPRESSION.length());
            switch (type.toLowerCase()) {
                case "deploymentnode":
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof DeploymentNode).forEach(elements::add);
                    break;
                case "infrastructurenode":
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof InfrastructureNode).forEach(elements::add);
                    break;
                case "softwaresystem":
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof SoftwareSystem).forEach(elements::add);
                    break;
                case "softwaresysteminstance":
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof SoftwareSystemInstance).forEach(elements::add);
                    break;
                case "container":
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Container).forEach(elements::add);
                    break;
                case "containerinstance":
                    context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof ContainerInstance).forEach(elements::add);
                    break;
                default:
                    throw new RuntimeException("The element type of \"" + type + "\" is not valid for this view");
            }
        } else if (expr.toLowerCase().startsWith(ELEMENT_TAG_EQUALS_EXPRESSION.toLowerCase())) {
            String[] tags = expr.substring(ELEMENT_TAG_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (hasAllTags(element, tags)) {
                    elements.add(element);
                }
            });
        } else if (expr.toLowerCase().startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (!hasAllTags(element, tags)) {
                    elements.add(element);
                }
            });
        } else {
            throw new RuntimeException(String.format("Unsupported expression \"%s\"", expr));
        }

        return elements;
    }

}