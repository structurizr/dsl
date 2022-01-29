package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

class ExampleTests extends AbstractTests {

    @Test
    void test_examples() throws Exception {
        new StructurizrDslParser().parse(new File("examples/getting-started/workspace.dsl"));
        new StructurizrDslParser().parse(new File("examples/big-bank-plc/workspace.dsl"));
        new StructurizrDslParser().parse(new File("examples/amazon-web-services/workspace.dsl"));
        new StructurizrDslParser().parse(new File("examples/financial-risk-system/workspace.dsl"));
    }

}