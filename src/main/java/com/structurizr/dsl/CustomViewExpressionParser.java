package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Element;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.ELEMENT_TYPE_EQUALS_EXPRESSION;

final class CustomViewExpressionParser extends AbstractExpressionParser {

    @Override
    protected Set<Element> evaluateElementTypeExpression(String expr, DslContext context) {
        Set<Element> elements = new LinkedHashSet<>();

        String type = expr.substring(ELEMENT_TYPE_EQUALS_EXPRESSION.length());
        switch (type.toLowerCase()) {
            case "custom":
                context.getWorkspace().getModel().getElements().stream().filter(e -> e instanceof CustomElement).forEach(elements::add);
                break;
            default:
                throw new RuntimeException("The element type of \"" + type + "\" is not valid for this view");
        }

        return elements;
    }

    protected Set<Element> findAfferentCouplings(Element element) {
        Set<Element> elements = new LinkedHashSet<>();

        elements.addAll(findAfferentCouplings(element, CustomElement.class));

        return elements;
    }

    protected Set<Element> findEfferentCouplings(Element element) {
        Set<Element> elements = new LinkedHashSet<>();

        elements.addAll(findEfferentCouplings(element, CustomElement.class));

        return elements;
    }

}