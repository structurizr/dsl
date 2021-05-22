package com.structurizr.dsl;

import com.structurizr.model.Relationship;
import com.structurizr.view.View;

import static com.structurizr.dsl.StructurizrDslExpressions.*;

abstract class ViewContentParser extends AbstractParser {

    protected static final String WILDCARD = "*";
    protected static final String ELEMENT_WILDCARD = "element==*";
    protected static final String RELATIONSHIP_WILDCARD = "relationship==*";

    protected boolean isExpression(String token) {
        token = token.toLowerCase();

        return
                token.startsWith(ELEMENT_TYPE_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(ELEMENT_TAG_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(ELEMENT_TAG_NOT_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(RELATIONSHIP) || token.endsWith(RELATIONSHIP) || token.contains(RELATIONSHIP) ||
                token.endsWith(ELEMENT_EQUALS_EXPRESSION) ||
                token.startsWith(RELATIONSHIP_TAG_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(RELATIONSHIP_TAG_NOT_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(RELATIONSHIP_SOURCE_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(RELATIONSHIP_DESTINATION_EQUALS_EXPRESSION.toLowerCase()) ||
                token.startsWith(RELATIONSHIP_EQUALS_EXPRESSION);
    }

    protected void removeRelationshipFromView(Relationship relationship, View view) {
        view.remove(relationship);
    }

}