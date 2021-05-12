package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;
import com.structurizr.view.View;

import java.util.HashSet;
import java.util.Set;

import static com.structurizr.dsl.StructurizrDslExpressions.RELATIONSHIP_TAG_EQUALS_EXPRESSION;
import static com.structurizr.dsl.StructurizrDslExpressions.RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION;

abstract class AbstractExpressionParser {

    final Set<Element> parseElementExpression(String expr, ViewDslContext context) {
        if (expr.contains(" && ")) {
            String[] expressions = expr.split(" && ");
            Set<Element> elements1 = evaluateElementExpression(expressions[0], context);
            Set<Element> elements2 = evaluateElementExpression(expressions[1], context);

            Set<Element> elements = new HashSet<>(elements1);
            elements.retainAll(elements2);

            return elements;
        } else if (expr.contains(" || ")) {
            String[] expressions = expr.split(" \\|\\| ");
            Set<Element> elements1 = evaluateElementExpression(expressions[0], context);
            Set<Element> elements2 = evaluateElementExpression(expressions[1], context);

            Set<Element> elements = new HashSet<>(elements1);
            elements.addAll(elements2);

            return elements;
        } else {
            return evaluateElementExpression(expr, context);
        }
    }

    abstract Set<Element> evaluateElementExpression(String expr, ViewDslContext context);

    final Set<Relationship> parseRelationshipExpression(String expr, ViewDslContext context) {
        Set<Relationship> relationships = new HashSet<>();

        if (expr.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(RELATIONSHIP_TAG_EQUALS_EXPRESSION.length()).split(",");
            View view = context.getView();
            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (hasAllTags(relationship, tags)) {
                    relationships.add(relationship);
                }
            });
        } else if (expr.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION)) {
            String[] tags = expr.substring(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.length()).split(",");
            View view = context.getView();
            context.getWorkspace().getModel().getRelationships().forEach(relationship -> {
                if (!hasAllTags(relationship, tags)) {
                    relationships.add(relationship);
                }
            });
        } else {
            throw new RuntimeException(String.format("Unexpected expression \"%s\"", expr));
        }

        return relationships;
    }

    boolean hasAllTags(ModelItem modelItem, String[] tags) {
        boolean result = true;

        for (String tag : tags) {
            result = result && modelItem.hasTag(tag.trim());
        }

        return result;
    }

}