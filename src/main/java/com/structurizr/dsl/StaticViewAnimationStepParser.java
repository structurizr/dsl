package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.view.StaticView;

import java.util.ArrayList;
import java.util.List;

final class StaticViewAnimationStepParser extends AbstractParser {

    void parse(StaticViewDslContext context, Tokens tokens) {
        // animationStep <identifier> [identifier...]

        if (!tokens.includes(1)) {
            throw new RuntimeException("Expected: animationStep <identifier> [identifier...]");
        }

        parse(context, context.getView(), tokens, 1);
    }

    void parse(StaticViewAnimationDslContext context, Tokens tokens) {
        // <identifier> [identifier...]

        if (!tokens.includes(0)) {
            throw new RuntimeException("Expected: <identifier> [identifier...]");
        }

        parse(context, context.getView(), tokens, 0);
    }

    void parse(DslContext context, StaticView view, Tokens tokens, int startIndex) {
        // <identifier> [identifier...]

        List<Element> elements = new ArrayList<>();

        for (int i = startIndex; i < tokens.size(); i++) {
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