package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Element;
import com.structurizr.view.CustomView;

import java.util.ArrayList;
import java.util.List;

final class CustomViewAnimationStepParser extends AbstractParser {

    void parse(CustomViewDslContext context, Tokens tokens) {
        // animationStep <identifier> [identifier...]

        if (!tokens.includes(1)) {
            throw new RuntimeException("Expected: animationStep <identifier> [identifier...]");
        }

        parse(context, context.getCustomView(), tokens, 1);
    }

    void parse(CustomViewAnimationDslContext context, Tokens tokens) {
        // <identifier> [identifier...]

        if (!tokens.includes(0)) {
            throw new RuntimeException("Expected: <identifier> [identifier...]");
        }

        parse(context, context.getView(), tokens, 0);
    }

    void parse(DslContext context, CustomView view, Tokens tokens, int startIndex) {
        List<CustomElement> elements = new ArrayList<>();

        for (int i = startIndex; i < tokens.size(); i++) {
            String elementIdentifier = tokens.get(i);

            Element element = context.getElement(elementIdentifier);
            if (element == null) {
                throw new RuntimeException("The element \"" + elementIdentifier + "\" does not exist");
            }

            if (element instanceof CustomElement) {
                elements.add((CustomElement)element);
            }
        }

        view.addAnimation(elements.toArray(new CustomElement[0]));
    }

}