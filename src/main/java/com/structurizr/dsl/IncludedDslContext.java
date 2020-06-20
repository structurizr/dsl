package com.structurizr.dsl;

import java.io.File;
import java.util.List;

final class IncludedDslContext extends DslContext {

    private List<String> lines;
    private File parentFile;
    private File file;

    IncludedDslContext(File parentFile) {
        this.parentFile = parentFile;
    }

    List<String> getLines() {
        return lines;
    }

    void setLines(List<String> lines) {
        this.lines = lines;
    }

    File getParentFile() {
        return parentFile;
    }

    File getFile() {
        return file;
    }

    void setFile(File file) {
        this.file = file;
    }

}