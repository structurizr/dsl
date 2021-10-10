package com.structurizr.dsl;

import com.structurizr.model.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.ELEMENT_TYPE_EQUALS_EXPRESSION;

final class StaticViewExpressionParser extends AbstractExpressionParser {

    @Override
    protected Set<Element> evaluateElementTypeExpression(String expr, DslContext context) {
        Set<Element> elements = new LinkedHashSet<>();

        String type = expr.substring(ELEMENT_TYPE_EQUALS_EXPRESSION.length());
        switch (type.toLowerCase()) {
            case "custom":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof CustomElement).forEach(elements::add);
                break;
            case "person":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Person).forEach(elements::add);
                break;
            case "softwaresystem":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof SoftwareSystem).forEach(elements::add);
                break;
            case "container":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Container).forEach(elements::add);
                break;
            case "component":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof Component).forEach(elements::add);
                break;
            default:
                throw new RuntimeException("The element type of \"" + type + "\" is not valid for this view");
        }

        return elements;
    }

    protected Set<Element> findAfferentCouplings(Element element) {
        Set<Element> elements = new LinkedHashSet<>();

        elements.addAll(findAfferentCouplings(element, CustomElement.class));
        elements.addAll(findAfferentCouplings(element, Person.class));
        elements.addAll(findAfferentCouplings(element, SoftwareSystem.class));
        elements.addAll(findAfferentCouplings(element, Container.class));
        elements.addAll(findAfferentCouplings(element, Component.class));

        return elements;
    }

    protected Set<Element> findEfferentCouplings(Element element) {
        Set<Element> elements = new LinkedHashSet<>();

        elements.addAll(findEfferentCouplings(element, CustomElement.class));
        elements.addAll(findEfferentCouplings(element, Person.class));
        elements.addAll(findEfferentCouplings(element, SoftwareSystem.class));
        elements.addAll(findEfferentCouplings(element, Container.class));
        elements.addAll(findEfferentCouplings(element, Component.class));

        return elements;
    }

}