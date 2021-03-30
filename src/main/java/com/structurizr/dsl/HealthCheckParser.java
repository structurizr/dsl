package com.structurizr.dsl;

import com.structurizr.model.StaticStructureElementInstance;

class HealthCheckParser extends AbstractParser {

    private static final String GRAMMAR = "healthCheck <name> <url> [interval] [timeout]";

    private final static int NAME_INDEX = 1;
    private final static int URL_INDEX = 2;
    private final static int INTERVAL_INDEX = 3;
    private final static int TIMEOUT_INDEX = 4;

    private final static int DEFAULT_INTERVAL = 60;
    private final static long DEFAULT_TIMEOUT = 0;

    void parse(StaticStructureElementInstanceDslContext context, Tokens tokens) {
        // healthCheck <name> <url> [interval] [timeout]

        if (tokens.hasMoreThan(TIMEOUT_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(URL_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String name = tokens.get(NAME_INDEX);
        String url = tokens.get(URL_INDEX);
        int interval = DEFAULT_INTERVAL;
        long timeout = DEFAULT_TIMEOUT;

        if (tokens.includes(INTERVAL_INDEX)) {
            try {
                interval = Integer.parseInt(tokens.get(INTERVAL_INDEX));

                if (interval < 1) {
                    throw new RuntimeException("The interval must be a positive integer (number of seconds)");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("The interval of \"" + tokens.get(INTERVAL_INDEX) + "\" is not valid - it must be a positive integer (number of seconds)");
            }
        }

        if (tokens.includes(TIMEOUT_INDEX)) {
            try {
                timeout = Integer.parseInt(tokens.get(TIMEOUT_INDEX));

                if (timeout < 0) {
                    throw new RuntimeException("The timeout must be zero or a positive integer (number of milliseconds)");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("The timeout of \"" + tokens.get(TIMEOUT_INDEX) + "\" is not valid - it must be zero or a positive integer (number of milliseconds)");
            }
        }

        StaticStructureElementInstance elementInstance = context.getElementInstance();
        elementInstance.addHealthCheck(name, url, interval, timeout);
    }

}