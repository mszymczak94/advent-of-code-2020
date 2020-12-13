package com.szymczak.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileReader {
    public static String[] read(File file) throws IOException {
        return Files.readAllLines(file.toPath()).toArray(new String[0]);
    }
}
