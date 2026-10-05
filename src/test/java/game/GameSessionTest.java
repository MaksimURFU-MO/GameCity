package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class GameSessionTest {

    private GameSession session;

    // заглушки для класса максима
    private static class FakeDatabase extends CityDataBase {
        @Override
        public boolean cityExists(String cityName) {
            return cityName.equalsIgnoreCase("Астрахань");
        }
    }

    private static class FakeBot extends Bot {
        public FakeBot() { super(null); }
        @Override
        public Optional<String> chooseCity(char letter, GameState state) {
            return Optional.of("Москва"); // Бот всегда отвечает Москва
        }
    }

    private static class FakeNextLetter extends NextLetter {
        @Override
        public char lastSymbol(String city) {
            return 'а'; // Для простоты тестов всегда возвращаем 'а'
        }
    }

    @BeforeEach
    public void setUp() {
        // Этот метод запускается перед каждым тестом, создавая чистую игру
        session = new GameSession(new FakeDatabase(), new FakeNextLetter(), new FakeBot());
    }

    @Test
    public void testHelpCommand() {
        String response = session.handleMessage("\\help");
        assertTrue(response.contains("Правила просты"));
        assertFalse(session.getState().isStarted(), "Команда help не должна запускать игру");
    }

    @Test
    public void testGameStartsOnFirstMessage() {
        String response = session.handleMessage("привет");
        assertTrue(session.getState().isStarted());
        assertTrue(response.contains("Начинаем! Мой город: Москва"));
        assertEquals('а', session.getState().getExpectedLetter());
    }

    @Test
    public void testGiveUpCommand() {
        session.handleMessage(""); // Стартуем игру
        String response = session.handleMessage("сдаюсь");
        assertEquals("Ура! Я победил! Ещё разик?", response);
        assertFalse(session.getState().isStarted(), "Игра должна завершиться после сдачи");
    }

    @Test
    public void testWrongCityValidation() {
        session.handleMessage(""); // Стартуем игру (бот называет Москву, ждет А)

        // Вводим город, которого нет в FakeDatabase
        String response = session.handleMessage("Абакан");
        assertEquals("Нет такого города!", response);
    }

    @Test
    public void testCorrectCityValidation() {
        session.handleMessage(""); // Стартуем (бот называет Москву, ждет на А)

        // Астрахань есть в нашей фейковой базе данных
        String response = session.handleMessage("Астрахань");

        // Так как наш FakeBot всегда отвечает Москва, проверяем, что ход перешел к нему
        assertEquals("Москва", response);
        assertTrue(session.getState().isUsed("Астрахань"), "Город игрока должен записаться в историю");
    }
}