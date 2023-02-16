package com.structurizr.dsl;

import com.structurizr.Workspace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract class AbstractViewParser extends AbstractParser {

    private static final Log log = LogFactory.getLog(AbstractViewParser.class);

    protected String generateViewKey(Workspace workspace, String prefix) {
        int counter = 1;
        String key = prefix + "-" + counter;

        while (hasViewWithKey(workspace, key)) {
            counter++;
            key = prefix + "-" + counter;
        }

        log.warn(key + " is an automatically generated view key - you will likely lose manual layout information when using automatically generated view keys.");
        return key;
    }

    protected boolean hasViewWithKey(Workspace workspace, String key) {
        return workspace.getViews().getViews().stream().anyMatch(view -> view.getKey().equals(key));
    }

}