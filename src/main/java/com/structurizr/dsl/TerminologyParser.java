package com.structurizr.dsl;

final class TerminologyParser extends AbstractParser {

    private final static int TERM_INDEX = 1;

    void parseEnterprise(DslContext context, Tokens tokens) {
        // enterprise <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: enterprise <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setEnterprise(tokens.get(TERM_INDEX));
    }

    void parsePerson(DslContext context, Tokens tokens) {
        // person <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: person <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setPerson(tokens.get(TERM_INDEX));
    }

    void parseSoftwareSystem(DslContext context, Tokens tokens) {
        // softwareSystem <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: softwareSystem <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setSoftwareSystem(tokens.get(TERM_INDEX));
    }

    void parseContainer(DslContext context, Tokens tokens) {
        // container <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: container <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setContainer(tokens.get(TERM_INDEX));
    }

    void parseComponent(DslContext context, Tokens tokens) {
        // component <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: component <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setComponent(tokens.get(TERM_INDEX));
    }

    void parseDeploymentNode(DslContext context, Tokens tokens) {
        // deploymentNode <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: deploymentNode <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setDeploymentNode(tokens.get(TERM_INDEX));
    }

    void parseInfrastructureNode(DslContext context, Tokens tokens) {
        // infrastructureNode <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: infrastructureNode <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setInfrastructureNode(tokens.get(TERM_INDEX));
    }

    void parseRelationship(DslContext context, Tokens tokens) {
        // relationship <term>
        if (!tokens.includes(TERM_INDEX)) {
            throw new RuntimeException("Expected: relationship <term>");
        }

        context.getWorkspace().getViews().getConfiguration().getTerminology().setRelationship(tokens.get(TERM_INDEX));
    }

}