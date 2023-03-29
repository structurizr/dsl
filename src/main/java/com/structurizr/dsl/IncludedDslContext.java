package com.structurizr.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final class IncludedDslContext extends DslContext {

    private final File parentFile;
    private final List<IncludedFile> files = new ArrayList<>();

    IncludedDslContext(File parentFile) {
        this.parentFile = parentFile;
    }

    File getParentFile() {
        return parentFile;
    }

    void addFile(File file, List<String> lines) {
        this.files.add(new IncludedFile(file, lines));
    }

    List<IncludedFile> getFiles() {
        return new ArrayList<>(files);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[0];
    }

}