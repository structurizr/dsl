package com.structurizr.dsl;

import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.ModelView;
import com.structurizr.view.View;

import java.util.HashMap;
import java.util.Map;

final class AutoLayoutParser extends AbstractParser {

    private static final int DEFAULT_RANK_SEPARATION = 300;
    private static final int DEFAULT_NODE_SEPARATION = 300;

    private static final int RANK_DIRECTION_INDEX = 1;
    private static final int RANK_SEPARATION_INDEX = 2;
    private static final int NODE_SEPARATION_INDEX = 3;

    private static Map<String, AutomaticLayout.RankDirection> RANK_DIRECTIONS = new HashMap<>();

    static {
        RANK_DIRECTIONS.put("tb", AutomaticLayout.RankDirection.TopBottom);
        RANK_DIRECTIONS.put("bt", AutomaticLayout.RankDirection.BottomTop);
        RANK_DIRECTIONS.put("lr", AutomaticLayout.RankDirection.LeftRight);
        RANK_DIRECTIONS.put("rl", AutomaticLayout.RankDirection.RightLeft);
    }

    void parse(ModelViewDslContext context, Tokens tokens) {
        // autoLayout [rankDirection] [rankSeparation] [nodeSeparation]
        ModelView view = context.getView();
        if (view != null) {
            AutomaticLayout.RankDirection rankDirection = AutomaticLayout.RankDirection.TopBottom;
            int rankSeparation = DEFAULT_RANK_SEPARATION;
            int nodeSeparation = DEFAULT_NODE_SEPARATION;

            if (tokens.includes(RANK_DIRECTION_INDEX)) {
                String rankDirectionAsString = tokens.get(RANK_DIRECTION_INDEX);

                if (RANK_DIRECTIONS.containsKey(rankDirectionAsString)) {
                    rankDirection = RANK_DIRECTIONS.get(rankDirectionAsString);
                } else {
                    throw new RuntimeException("Valid rank directions are: tb|bt|lr|rl");
                }
            }

            if (tokens.includes(RANK_SEPARATION_INDEX)) {
                String rankSeparationAsString = tokens.get(RANK_SEPARATION_INDEX);

                try {
                    rankSeparation = Integer.parseInt(rankSeparationAsString);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Rank separation must be positive integer in pixels");
                }
            }

            if (tokens.includes(NODE_SEPARATION_INDEX)) {
                String nodeSeparationAsString = tokens.get(NODE_SEPARATION_INDEX);

                try {
                    nodeSeparation = Integer.parseInt(nodeSeparationAsString);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Node separation must be positive integer in pixels");
                }
            }

            view.enableAutomaticLayout(rankDirection, rankSeparation, nodeSeparation);
        }
    }

}