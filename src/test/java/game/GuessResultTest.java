package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuessResultTest {

    @Test
    public void testEmptyMessage() {
        String message = GuessResult.EMPTY.getMessage('а');
        assertEquals("Назови город на букву \"А\".", message);
    }

    @Test
    public void testWrongLetterMessage() {
        String message = GuessResult.WRONG_LETTER.getMessage('м');
        assertTrue(message.contains("Конкретно сейчас нужен город на букву М"));
    }

    @Test
    public void testNotFoundMessage() {
        String message = GuessResult.NOT_FOUND.getMessage('к');
        assertEquals("Нет такого города!", message);
    }
}