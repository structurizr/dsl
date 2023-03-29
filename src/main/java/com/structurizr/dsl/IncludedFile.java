package com.structurizr.dsl;

import java.io.File;
import java.util.List;

final class IncludedFile {

    private final File file;
    private final List<String> lines;

    IncludedFile(File file, List<String> lines) {
        this.file = file;
        this.lines = lines;
    }

    List<String> getLines() {
        return lines;
    }

    File getFile() {
        return file;
    }

}