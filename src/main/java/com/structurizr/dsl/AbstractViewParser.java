package com.structurizr.dsl;

import com.structurizr.Workspace;
import com.structurizr.view.FilteredView;
import com.structurizr.view.SystemLandscapeView;
import com.structurizr.view.View;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractViewParser extends AbstractParser {

    protected String generateViewKey(Workspace workspace, String prefix) {
        int counter = 1;
        String key = prefix + "-" + counter;

        while (hasViewWithKey(workspace, key)) {
            counter++;
            key = prefix + "-" + counter;
        }

        return key;
    }

    protected boolean hasViewWithKey(Workspace workspace, String key) {
        Set<View> views = new HashSet<>();
        views.addAll(workspace.getViews().getCustomViews());
        views.addAll(workspace.getViews().getSystemLandscapeViews());
        views.addAll(workspace.getViews().getSystemContextViews());
        views.addAll(workspace.getViews().getContainerViews());
        views.addAll(workspace.getViews().getComponentViews());
        views.addAll(workspace.getViews().getDynamicViews());
        views.addAll(workspace.getViews().getDeploymentViews());

        Collection<FilteredView> filteredViews = workspace.getViews().getFilteredViews();

        return
                views.stream().anyMatch(view -> view.getKey().equals(key)) ||
                filteredViews.stream().anyMatch(view -> view.getKey().equals(key));
    }

}