package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;
import com.structurizr.model.StaticStructureElement;

final class RefParser extends AbstractParser {

    private static final String GRAMMAR = "!ref <identifier|canonical name>";

    private final static int IDENTIFIER_INDEX = 1;

    ModelItem parse(DslContext context, Tokens tokens) {
        // !ref <identifier|canonical name>

        if (tokens.hasMoreThan(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String s = tokens.get(IDENTIFIER_INDEX);

        ModelItem modelItem;

        if (s.contains("://")) {
            modelItem = context.getWorkspace().getModel().getElementWithCanonicalName(s);
        } else {
            modelItem = context.getElement(s);

            if (modelItem == null) {
                modelItem = context.getRelationship(s);
            }
        }

        if (modelItem == null) {
            throw new RuntimeException("An element/relationship referenced by \"" + s + "\" could not be found");
        }

        if (context instanceof GroupableDslContext && modelItem instanceof StaticStructureElement) {
            GroupableDslContext groupableDslContext = (GroupableDslContext)context;
            StaticStructureElement staticStructureElement = (StaticStructureElement)modelItem;
            if (groupableDslContext.hasGroup()) {
                staticStructureElement.setGroup(groupableDslContext.getGroup().getName());
            }
        }

        return modelItem;
    }

}