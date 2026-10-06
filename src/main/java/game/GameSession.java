package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameSession {

    public static final String HELP_TEXT = String.join("\n",
            "Я бот для игры в города!",
            "Правила просты: я называю город, а ты называешь другой город,",
            "начинающийся на последнюю (значащую) букву моего.",
            "Команды:",
            "  \\help — показать это сообщение ещё раз",
            "  подскажи — попросить подсказку",
            "  сдаюсь — сдаться и начать заново",
            "  \\exit - выход из игры"
    );

    private final CityDataBase database;
    private final NextLetter letterUtils;
    private final Bot bot;
    private final GameState state = new GameState();

    public GameSession(CityDataBase database, NextLetter letterUtils, Bot bot) {
        this.database = database;
        this.letterUtils = letterUtils;
        this.bot = bot;
    }

    public String handleMessage(String rawMessage) {
        String message = rawMessage == null ? "" : rawMessage.trim();

        if (message.equalsIgnoreCase("\\help") || message.equalsIgnoreCase("/help")) {
            return HELP_TEXT;
        }

        if (!state.isStarted()) {
            return startGame();
        }

        String normalized = message.toLowerCase();
        if (normalized.equals("сдаюсь")) {
            return giveUp();
        }
        if (normalized.equals("подскажи") || normalized.equals("подсказка")) {
            return giveHint();
        }
        return handleCityGuess(message);
    }

    private String startGame() {
        state.start();

        // 1. Собираем все играбельные буквы в список
        List<Character> letters = new ArrayList<>();
        for (char letter = 'а'; letter <= 'я'; letter++) {
            if (letter != 'ь' && letter != 'ъ' && letter != 'ы') {
                letters.add(letter);
            }
        }

        // 2. Перемешиваем буквы, чтобы старт всегда был случайным
        Collections.shuffle(letters);

        // 3. Ищем первый попавшийся город на случайную букву
        for (char letter : letters) {
            Optional<String> city = bot.chooseCity(letter, state);
            if (city.isPresent()) {
                state.addCity(city.get());
                state.setExpectedLetter(letterUtils.lastSymbol(city.get()));
                return HELP_TEXT + "\n\nНачинаем! Мой город: " + city.get();
            }
        }
        throw new IllegalStateException("В базе городов нет ни одного города");
    }

    private String giveUp() {
        state.reset();
        return "Ура! Я победил! Ещё разик?";
    }

    private String giveHint() {
        char letter = state.getExpectedLetter();
        Optional<String> hintCity = bot.chooseCity(letter, state);
        if (hintCity.isEmpty()) {
            return "Даже я не знаю больше городов на букву \"" + Character.toUpperCase(letter)
                    + "\". Похоже, ты выиграл! Сыграем ещё?";
        }
        String city = hintCity.get();
        int prefixLength = Math.min(2, Math.max(1, city.length() - 1));
        return "Есть один город. На " + city.substring(0, prefixLength)
                + " начинается, на " + Character.toLowerCase(city.charAt(city.length() - 1))
                + " заканчивается…";
    }

    private GuessResult validateGuess(String cityName) {
        if (cityName.isEmpty()) {
            return GuessResult.EMPTY;
        }
        if (Character.toLowerCase(cityName.charAt(0)) != state.getExpectedLetter()) {
            return GuessResult.WRONG_LETTER;
        }
        if (!database.cityExists(cityName)) {
            return GuessResult.NOT_FOUND;
        }
        if (state.isUsed(cityName)) {
            return GuessResult.ALREADY_USED;
        }
        return GuessResult.VALID;
    }

    private String handleCityGuess(String cityName) {
        GuessResult result = validateGuess(cityName);

        if (result == GuessResult.VALID) {
            return processBotTurn(cityName);
        }

        // Если город не прошел проверку, просим Enum сгенерировать текст ошибки
        return result.getMessage(state.getExpectedLetter());
    }

    private String processBotTurn(String validCityName) {
        state.addCity(validCityName);
        char nextLetter = letterUtils.lastSymbol(validCityName);

        Optional<String> botCity = bot.chooseCity(nextLetter, state);
        if (botCity.isEmpty()) {
            state.reset();
            return "Я не знаю больше городов на букву \"" + Character.toUpperCase(nextLetter)
                    + "\"! Ты победил! Сыграем ещё?";
        }

        state.addCity(botCity.get());
        state.setExpectedLetter(letterUtils.lastSymbol(botCity.get()));
        return botCity.get();
    }

    public GameState getState() {
        return state;
    }
}