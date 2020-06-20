package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.view.StaticView;

import java.util.ArrayList;
import java.util.List;

final class StaticViewAnimationStepParser extends AbstractParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    void parse(StaticViewDslContext context, Tokens tokens) {
        // animationStep <identifier> [identifier...]

        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: animationStep <identifier> [identifier...]");
        }

        StaticView view = context.getView();
        List<Element> elements = new ArrayList<>();

        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String elementIdentifier = tokens.get(i);

            Element element = context.getElement(elementIdentifier);
            if (element == null) {
                throw new RuntimeException("The element \"" + elementIdentifier + "\" does not exist");
            }

            elements.add(element);
        }

        view.addAnimation(elements.toArray(new Element[0]));
    }

}