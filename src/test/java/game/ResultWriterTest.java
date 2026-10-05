package game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultWriterTest {

    @TempDir
    Path tempDir;

    private final ResultWriter writer = new ResultWriter();

    @Test
    void writeResult_writesAllLinesInOrder() throws IOException {
        Path file = tempDir.resolve("result.txt");
        List<String> history = List.of("Игрок: Москва", "Бот: Астрахань", "Игрок: Новгород");

        writer.writeResult(history, file);

        // Читаем файл обратно и сравниваем с тем, что записывали (заодно проверяем русские буквы)
        List<String> written = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(history, written);
    }

    @Test
    void writeResult_overwritesExistingFile() throws IOException {
        Path file = tempDir.resolve("result.txt");

        writer.writeResult(List.of("Игрок: Москва", "Бот: Астрахань"), file);
        writer.writeResult(List.of("Игрок: Сочи"), file);

        List<String> written = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(List.of("Игрок: Сочи"), written);
    }

    @Test
    void writeResult_emptyHistory_createsEmptyFile() throws IOException {
        Path file = tempDir.resolve("empty.txt");

        writer.writeResult(new ArrayList<>(), file);

        assertTrue(Files.exists(file));
        assertTrue(Files.readAllLines(file, StandardCharsets.UTF_8).isEmpty());
    }

    @Test
    void writeResult_nullHistory_throwsException() {
        Path file = tempDir.resolve("result.txt");

        assertThrows(IllegalArgumentException.class, () -> writer.writeResult(null, file));
    }
}