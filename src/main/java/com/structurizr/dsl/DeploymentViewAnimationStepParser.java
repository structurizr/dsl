package com.structurizr.dsl;

import com.structurizr.model.ContainerInstance;
import com.structurizr.model.Element;
import com.structurizr.model.InfrastructureNode;
import com.structurizr.model.StaticStructureElementInstance;
import com.structurizr.view.DeploymentView;

import java.util.ArrayList;
import java.util.List;

final class DeploymentViewAnimationStepParser extends AbstractParser {

    void parse(DeploymentViewDslContext context, Tokens tokens) {
        // animationStep <identifier> [identifier...]

        if (!tokens.includes(1)) {
            throw new RuntimeException("Expected: animationStep <identifier> [identifier...]");
        }

        parse(context, context.getView(), tokens, 1);
    }

    void parse(DeploymentViewAnimationDslContext context, Tokens tokens) {
        // animationStep <identifier> [identifier...]

        if (!tokens.includes(0)) {
            throw new RuntimeException("Expected: <identifier> [identifier...]");
        }

        parse(context, context.getView(), tokens, 0);
    }

    void parse(DslContext context, DeploymentView view, Tokens tokens, int startIndex) {
        List<StaticStructureElementInstance> staticStructureElementInstances = new ArrayList<>();
        List<InfrastructureNode> infrastructureNodes = new ArrayList<>();

        for (int i = startIndex; i < tokens.size(); i++) {
            String identifier = tokens.get(i);

            Element element = context.getElement(identifier);
            if (element == null) {
                throw new RuntimeException("The element \"" + identifier + "\" does not exist");
            }

            if (element instanceof StaticStructureElementInstance) {
                staticStructureElementInstances.add((StaticStructureElementInstance)element);
            }

            if (element instanceof InfrastructureNode) {
                infrastructureNodes.add((InfrastructureNode)element);
            }
        }

        if (!(staticStructureElementInstances.isEmpty() && infrastructureNodes.isEmpty())) {
            view.addAnimation(staticStructureElementInstances.toArray(new StaticStructureElementInstance[0]), infrastructureNodes.toArray(new InfrastructureNode[0]));
        }
    }

}