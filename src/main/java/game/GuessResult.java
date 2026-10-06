package game;

public enum GuessResult {
    EMPTY,
    WRONG_LETTER,
    NOT_FOUND,
    ALREADY_USED,
    VALID;

    public String getMessage(char expectedLetter) {
        switch (this) {
            case EMPTY:
                return "Назови город на букву \"" + Character.toUpperCase(expectedLetter) + "\".";
            case WRONG_LETTER:
                return "Нужно называть город на последнюю букву предыдущего. "
                        + "Конкретно сейчас нужен город на букву " + Character.toUpperCase(expectedLetter) + ".";
            case NOT_FOUND:
                return "Нет такого города!";
            case ALREADY_USED:
                return "Этот город уже называли! Назови другой на букву "
                        + Character.toUpperCase(expectedLetter) + ".";
            default:
                return "";
        }
    }
}