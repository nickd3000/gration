package com.physmo.messageSource;

import com.physmo.message.Msg;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileMessageSource implements MessageSource<List<File>> {

    String path;
    
    public FileMessageSource(String path) {
        this.path = path;    
    }
    
    @Override
    public Optional<Msg<List<File>>> poll() {
        return Optional.of(new Msg<>(get()));
    }


    public List<File> get() {
        Path directoryPath = Paths.get(path);
        File directory = directoryPath.toFile();
        List<File> outputFiles = new ArrayList<>();

        if (!directory.exists() || !directory.isDirectory()) {
            return outputFiles;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return outputFiles;
        }

        for (File file : files) {
            if (file.isDirectory()) continue;
            outputFiles.add(file);
        }

        return outputFiles;
    }
}
