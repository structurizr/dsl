package com.structurizr.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FileUtils {

    private static final String STRUCTURIZR_DSL_FILE_EXTENSION = ".dsl";

    static List<File> findFiles(File path) {
        List<File> files = new ArrayList<>();
        if (path.isDirectory()) {
            files = findFilesInDirectory(path);
        } else {
            files.add(path);
        }

        return files;
    }

    private static List<File> findFilesInDirectory(File directory) {
        List<File> files = new ArrayList<>();

        File[] filesInDirectory = directory.listFiles();
        if (filesInDirectory == null || filesInDirectory.length == 0) {
            return files;
        }

        Arrays.sort(filesInDirectory);

        for (File file : filesInDirectory) {
            if (!file.isDirectory() && file.getName().endsWith(STRUCTURIZR_DSL_FILE_EXTENSION)) {
                files.add(file);
            }

            if (file.isDirectory()) {
                files.addAll(findFilesInDirectory(file));
            }
        }

        return files;
    }

}
