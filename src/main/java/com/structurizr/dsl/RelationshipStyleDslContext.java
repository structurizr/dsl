package com.structurizr.dsl;

import com.structurizr.view.RelationshipStyle;

final class RelationshipStyleDslContext extends DslContext {

    private RelationshipStyle style;

    RelationshipStyleDslContext(RelationshipStyle style) {
        this.style = style;
    }

    RelationshipStyle getStyle() {
        return style;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.RELATIONSHIP_STYLE_THICKNESS_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_COLOR_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_COLOUR_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_LINE_STYLE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_ROUTING_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_FONT_SIZE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_WIDTH_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_POSITION_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_OPACITY_TOKEN,
                StructurizrDslTokens.PROPERTIES_TOKEN
        };
    }

}