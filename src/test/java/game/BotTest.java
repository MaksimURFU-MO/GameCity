package game;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class BotTest {

    // заглушка базы данных, чтобы не читать реальный файл в тестах
    private static class FakeCityDatabase extends CityDataBase {
        @Override
        public List<String> getCitiesByLetter(char letter) {
            if (letter == 'м') {
                return List.of("Москва", "Минск", "Мурманск");
            }
            return List.of();
        }
    }

    @Test
    public void testBotChoosesCityCorrectly() {
        // Поддельный Random, который всегда возвращает индекс 0 (первый элемент)
        Random predictableRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        FakeCityDatabase fakeDb = new FakeCityDatabase();
        Bot bot = new Bot(fakeDb, predictableRandom);
        GameState state = new GameState();

        Optional<String> result = bot.chooseCity('м', state);

        assertTrue(result.isPresent());
        //так как random вернул 0, бот обязан выбрать Москву
        assertEquals("Москва", result.get());
    }

    @Test
    public void testBotReturnsEmptyWhenNoCitiesLeft() {
        FakeCityDatabase fakeDb = new FakeCityDatabase();
        Bot bot = new Bot(fakeDb, new Random());
        GameState state = new GameState();

        //городов на Я в нашей фейковой базе нет
        Optional<String> result = bot.chooseCity('я', state);

        assertTrue(result.isEmpty(), "Бот должен сдаться, если городов нет");
    }
}