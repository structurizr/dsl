package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.*;

abstract class AbstractExpressionParser {

    private static final String WILDCARD = "*";

    final Set<ModelItem> parseExpression(String expr, DslContext context) {
        if (expr.contains(" && ")) {
            String[] expressions = expr.split(" && ");
            Set<ModelItem> modelItems1 = evaluateExpression(expressions[0], context);
            Set<ModelItem> modelItems2 = evaluateExpression(expressions[1], context);

            Set<ModelItem> modelItems = new HashSet<>(modelItems1);
            modelItems.retainAll(modelItems2);

            return modelItems;
        } else if (expr.contains(" || ")) {
            String[] expressions = expr.split(" \\|\\| ");
            Set<ModelItem> modelItems1 = evaluateExpression(expressions[0], context);
            Set<ModelItem> modelItems2 = evaluateExpression(expressions[1], context);

            Set<ModelItem> elements = new HashSet<>(modelItems1);
            elements.addAll(modelItems2);

            return elements;
        } else {
            return evaluateExpression(expr, context);
        }
    }

    private Set<ModelItem> evaluateExpression(String expr, DslContext context) {
        Set<ModelItem> modelItems = new LinkedHashSet<>();

        if (expr.toLowerCase().startsWith(ELEMENT_TYPE_EQUALS_EXPRESSION)) {
            modelItems.addAll(evaluateElementTypeExpression(expr, context));
        } else if (expr.toLowerCase().startsWith(ELEMENT_TAG_EQUALS_EXPRESSION.toLowerCase())) {
            String[] tags = expr.substring(ELEMENT_TAG_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (hasAllTags(element, tags)) {
                    modelItems.add(element);
                }
            });
        } else if (expr.toLowerCase().startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getElements().forEach(element -> {
                if (!hasAllTags(element, tags)) {
                    modelItems.add(element);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(RELATIONSHIP_TAG_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (hasAllTags(relationship, tags)) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (!hasAllTags(relationship, tags)) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_SOURCE_EQUALS_EXPRESSION)) {
            String identifier = expr.substring(RELATIONSHIP_SOURCE_EQUALS_EXPRESSION.length());
            Element source = context.getElement(identifier);

            if (source == null) {
                throw new RuntimeException("The element \"" + identifier + "\" does not exist");
            }

            Set<Element> sourceElements = new HashSet<>();
            if (source instanceof ElementGroup) {
                sourceElements.addAll(((ElementGroup)source).getElements());
            } else {
                sourceElements.add(source);
            }

            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (sourceElements.contains(relationship.getSource())) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION)) {
            String identifier = expr.substring(RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION.length());
            Element destination = context.getElement(identifier);

            if (destination == null) {
                throw new RuntimeException("The element \"" + identifier + "\" does not exist");
            }

            Set<Element> destinationElements = new HashSet<>();
            if (destination instanceof ElementGroup) {
                destinationElements.addAll(((ElementGroup) destination).getElements());
            } else {
                destinationElements.add(destination);
            }

            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (destinationElements.contains(relationship.getDestination())) {
                    modelItems.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_EQUALS_EXPRESSION)) {
            expr = expr.substring(RELATIONSHIP_EQUALS_EXPRESSION.length());
            String sourceIdentifier = expr.split("->")[0].trim();
            String destinationIdentifier = expr.split("->")[1].trim();

            String sourceExpression = RELATIONSHIP_SOURCE_EQUALS_EXPRESSION + sourceIdentifier;
            String destinationExpression = RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION + destinationIdentifier;

            if (WILDCARD.equals(sourceIdentifier) && WILDCARD.equals(destinationIdentifier)) {
                modelItems.addAll(context.getWorkspace().getModel().getRelationships());
            } else if (WILDCARD.equals(destinationIdentifier)) {
                modelItems.addAll(parseExpression(sourceExpression, context));
            } else if (WILDCARD.equals(sourceIdentifier)) {
                modelItems.addAll(parseExpression(destinationExpression, context));
            } else {
                modelItems.addAll(parseExpression(sourceExpression + " && " + destinationExpression, context));
            }
        } else {
            if (expr.startsWith(ELEMENT_EQUALS_EXPRESSION)) {
                expr = expr.substring(ELEMENT_EQUALS_EXPRESSION.length());
            }

            modelItems.addAll(parseIdentifierExpression(expr, context));
        }

        return modelItems;
    }

    protected abstract Set<Element> evaluateElementTypeExpression(String expr, DslContext context);

    private boolean hasAllTags(ModelItem modelItem, String[] tags) {
        boolean result = true;

        for (String tag : tags) {
            result = result && modelItem.hasTag(tag.trim());
        }

        return result;
    }

    protected abstract Set<Element> findAfferentCouplings(Element element);

    protected <T extends Element> Set<Element> findAfferentCouplings(Element element, Class<T> typeOfElement) {
        Set<Element> elements = new HashSet<>();

        Set<Relationship> relationships = element.getModel().getRelationships();
        relationships.stream().filter(r -> r.getDestination().equals(element) && typeOfElement.isInstance(r.getSource()))
                .map(Relationship::getSource)
                .forEach(elements::add);

        return elements;
    }

    protected abstract Set<Element> findEfferentCouplings(Element element);

    protected <T extends Element> Set<Element> findEfferentCouplings(Element element, Class<T> typeOfElement) {
        Set<Element> elements = new HashSet<>();

        Set<Relationship> relationships = element.getModel().getRelationships();
        relationships.stream().filter(r -> r.getSource().equals(element) && typeOfElement.isInstance(r.getDestination()))
                .map(Relationship::getDestination)
                .forEach(elements::add);

        return elements;
    }

    protected Set<ModelItem> parseIdentifierExpression(String expr, DslContext context) {
        Set<ModelItem> modelItems = new HashSet<>();

        // see if this a relationship first
        Relationship relationship = context.getRelationship(expr);
        if (relationship != null) {
            modelItems.add(relationship);
            return modelItems;
        }

        String identifier = expr;
        boolean includeAfferentCouplings = false;
        boolean includeEfferentCouplings = false;


        if (identifier.startsWith(ELEMENT_COUPLINGS_EXPRESSION)) {
            includeAfferentCouplings = true;
            identifier = identifier.substring(ELEMENT_COUPLINGS_EXPRESSION.length());
        }
        if (identifier.endsWith(ELEMENT_COUPLINGS_EXPRESSION)) {
            includeEfferentCouplings = true;
            identifier = identifier.substring(0, identifier.length() - ELEMENT_COUPLINGS_EXPRESSION.length());
        }

        identifier = identifier.trim();

        Element element = context.getElement(identifier);
        if (element == null) {
            throw new RuntimeException("The element/relationship \"" + expr + "\" does not exist");
        }

        if (element instanceof ElementGroup) {
            ElementGroup group = (ElementGroup)element;
            for (Element e : group.getElements()) {
                modelItems.add(e);

                if (includeAfferentCouplings) {
                    modelItems.addAll(findAfferentCouplings(e));
                }

                if (includeEfferentCouplings) {
                    modelItems.addAll(findEfferentCouplings(e));
                }
            }
        } else {
            modelItems.add(element);

            if (includeAfferentCouplings) {
                modelItems.addAll(findAfferentCouplings(element));
            }

            if (includeEfferentCouplings) {
                modelItems.addAll(findEfferentCouplings(element));
            }
        }

        return modelItems;
    }

}