package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.DeploymentView;

final class DeploymentViewParser extends AbstractViewParser {

    private static final String GRAMMAR = "deployment <*|software system identifier> <environment> [key] [description] {";

    private static final String VIEW_TYPE = "Deployment";

    private static final int SCOPE_IDENTIFIER_INDEX = 1;
    private static final int ENVIRONMENT_INDEX = 2;
    private static final int KEY_INDEX = 3;
    private static final int DESCRIPTION_INDEX = 4;

    DeploymentView parse(DslContext context, Tokens tokens) {
        // deployment <*|software system identifier> <environment|environment name> [key] [description] {

        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(ENVIRONMENT_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Workspace workspace = context.getWorkspace();
        String key = "";
        String scopeIdentifier = tokens.get(SCOPE_IDENTIFIER_INDEX);
        String environment = tokens.get(ENVIRONMENT_INDEX);
        if (context.getElement(environment) != null && context.getElement(environment) instanceof DeploymentEnvironment) {
            environment = context.getElement(environment).getName();
        }

        // check that the deployment environment exists in the model
        final String env = environment;
        if (context.getWorkspace().getModel().getDeploymentNodes().stream().noneMatch(dn -> dn.getEnvironment().equals(env))) {
            throw new RuntimeException("The environment \"" + environment + "\" does not exist");
        }

        String description = "";
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        DeploymentView view;

        if ("*".equals(scopeIdentifier)) {
            if (tokens.includes(KEY_INDEX)) {
                key = tokens.get(KEY_INDEX);
            } else {
                key = workspace.getViews().generateViewKey(VIEW_TYPE);
            }
            validateViewKey(key);

            view = workspace.getViews().createDeploymentView(key, description);
        } else {
            Element element = context.getElement(scopeIdentifier);
            if (element == null) {
                throw new RuntimeException("The software system \"" + scopeIdentifier + "\" does not exist");
            }

            if (element instanceof SoftwareSystem) {
                if (tokens.includes(KEY_INDEX)) {
                    key = tokens.get(KEY_INDEX);
                } else {
                    key = workspace.getViews().generateViewKey(VIEW_TYPE);
                }
                validateViewKey(key);

                view = workspace.getViews().createDeploymentView((SoftwareSystem)element, key, description);
            } else {
                throw new RuntimeException("The element \"" + scopeIdentifier + "\" is not a software system");
            }
        }

        view.setEnvironment(environment);

        return view;
    }

}