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

}