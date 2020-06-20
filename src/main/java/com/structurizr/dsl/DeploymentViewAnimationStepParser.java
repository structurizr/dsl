package com.structurizr.dsl;

import com.structurizr.model.ContainerInstance;
import com.structurizr.model.Element;
import com.structurizr.model.InfrastructureNode;
import com.structurizr.view.DeploymentView;

import java.util.ArrayList;
import java.util.List;

final class DeploymentViewAnimationStepParser extends AbstractParser {

    private static final int FIRST_IDENTIFIER_INDEX = 1;

    void parse(DeploymentViewDslContext context, Tokens tokens) {
        // animationStep <identifier> [identifier...]

        if (!tokens.includes(FIRST_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: animationStep <identifier> [identifier...]");
        }

        DeploymentView view = context.getView();
        List<ContainerInstance> containerInstances = new ArrayList<>();
        List<InfrastructureNode> infrastructureNodes = new ArrayList<>();

        for (int i = FIRST_IDENTIFIER_INDEX; i < tokens.size(); i++) {
            String identifier = tokens.get(i);

            Element element = context.getElement(identifier);
            if (element == null) {
                throw new RuntimeException("The element \"" + identifier + "\" does not exist");
            }

            if (element instanceof ContainerInstance) {
                containerInstances.add((ContainerInstance)element);
            }

            if (element instanceof InfrastructureNode) {
                infrastructureNodes.add((InfrastructureNode)element);
            }
        }

        if (!containerInstances.isEmpty()) {
            view.addAnimation(containerInstances.toArray(new ContainerInstance[0]), infrastructureNodes.toArray(new InfrastructureNode[0]));
        }
    }

}