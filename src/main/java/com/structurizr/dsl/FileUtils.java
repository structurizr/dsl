package com.structurizr.dsl;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FileUtils {

    private static final String STRUCTURIZR_DSL_FILE_EXTENSION = ".dsl";

    @Nonnull
    static List<File> findFiles(@Nonnull File path) {
        List<File> files = new ArrayList<>();
        if (path.isDirectory()) {
            files = findFilesInDirectory(path);
        } else {
            files.add(path);
        }

        return files;
    }

    @Nonnull
    private static List<File> findFilesInDirectory(@Nonnull File directory) {
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
