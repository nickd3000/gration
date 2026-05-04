package com.physmo.messageSource;

import com.physmo.message.Msg;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileMessageSource implements MessageSource<List<File>> {

    String path;
    private Map<String, Long> seenFiles = new HashMap<>();

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
        List<File> newFiles = new ArrayList<>();

        if (!directory.exists() || !directory.isDirectory()) {
            return newFiles;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return newFiles;
        }

        Set<String> currentFilePaths = new HashSet<>();
        for (File file : files) {
            if (file.isDirectory()) continue;
            
            String absolutePath = file.getAbsolutePath();
            long fileSize = file.length();
            currentFilePaths.add(absolutePath);
            
            if (!seenFiles.containsKey(absolutePath) || !seenFiles.get(absolutePath).equals(fileSize)) {
                newFiles.add(file);
                seenFiles.put(absolutePath, fileSize);
            }
        }

        // Remove deleted files from tracking
        seenFiles.keySet().retainAll(currentFilePaths);

        return newFiles;
    }
}
