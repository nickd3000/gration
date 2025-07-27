package com.physmo.messageSource;

import com.physmo.message.Msg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryMessageSource implements MessageSource {

    String path;
    
    public DirectoryMessageSource(String path) {
        this.path = path;    
    }
    
    @Override
    public Optional<Msg<?>> poll() {
        Path directoryPath = Paths.get(path);
        File directory = directoryPath.toFile();

        if (!directory.exists() || !directory.isDirectory()) {
            return Optional.empty();
        }

        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            return Optional.empty();
        }

        List<File> outputFiles = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) continue;
            outputFiles.add(file);
        }

        return Optional.of(new Msg<>(outputFiles));
    }
}
