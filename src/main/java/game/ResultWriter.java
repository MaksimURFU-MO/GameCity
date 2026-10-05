package game;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResultWriter {

    private static final String DEFAULT_FILE_NAME = "game_result.txt";

    public void writeResult(List<String> history) throws IOException {
        writeResult(history, Path.of(DEFAULT_FILE_NAME));
    }

    public void writeResult(List<String> history, Path filePath) throws IOException {
        if (history == null) {
            throw new IllegalArgumentException("История игры не может быть null");
        }

        Files.write(filePath, history, StandardCharsets.UTF_8);
    }
}