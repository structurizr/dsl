package com.structurizr.dsl;

import com.structurizr.model.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.ELEMENT_TYPE_EQUALS_EXPRESSION;

final class DeploymentViewExpressionParser extends AbstractExpressionParser {

    @Override
    protected Set<Element> evaluateElementTypeExpression(String expr, DslContext context) {
        Set<Element> elements = new LinkedHashSet<>();

        String type = expr.substring(ELEMENT_TYPE_EQUALS_EXPRESSION.length());
        switch (type.toLowerCase()) {
            case "custom":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof CustomElement).forEach(elements::add);
                break;
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

        return elements;
    }

    protected Set<Element> findAfferentCouplings(Element element) {
        Set<Element> elements = new LinkedHashSet<>();

        elements.addAll(findAfferentCouplings(element, CustomElement.class));
        elements.addAll(findAfferentCouplings(element, DeploymentNode.class));
        elements.addAll(findAfferentCouplings(element, InfrastructureNode.class));
        elements.addAll(findAfferentCouplings(element, SoftwareSystemInstance.class));
        elements.addAll(findAfferentCouplings(element, ContainerInstance.class));

        return elements;
    }

    protected Set<Element> findEfferentCouplings(Element element) {
        Set<Element> elements = new LinkedHashSet<>();

        elements.addAll(findEfferentCouplings(element, CustomElement.class));
        elements.addAll(findEfferentCouplings(element, DeploymentNode.class));
        elements.addAll(findEfferentCouplings(element, InfrastructureNode.class));
        elements.addAll(findEfferentCouplings(element, SoftwareSystemInstance.class));
        elements.addAll(findEfferentCouplings(element, ContainerInstance.class));

        return elements;
    }

}