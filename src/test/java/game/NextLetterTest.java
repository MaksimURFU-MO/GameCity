package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NextLetterTest {

    private NextLetter nextLetter;

    // Выполняется перед КАЖДЫМ тестом - у каждого теста свой новый объект
    @BeforeEach
    void setUp() {
        nextLetter = new NextLetter();
    }

    @Test
    void ordinaryCity_returnsLastLetter() {
        assertEquals('и', nextLetter.lastSymbol("Сочи"));
        assertEquals('ч', nextLetter.lastSymbol("Углич"));
        assertEquals('а', nextLetter.lastSymbol("Москва"));
    }

    @Test
    void softSign_isSkipped() {
        assertEquals('м', nextLetter.lastSymbol("Пермь"));
        assertEquals('л', nextLetter.lastSymbol("Ярославль"));
    }

    @Test
    void hardSign_isSkipped() {
        // Реальных городов на "ъ" нет, поэтому проверяем на условном слове
        assertEquals('т', nextLetter.lastSymbol("Тестъ"));
    }

    @Test
    void letterY_isSkipped() {
        assertEquals('а', nextLetter.lastSymbol("Гай"));
    }

    @Test
    void severalSkippedLettersInARow_areAllSkipped() {
        // "Грозный": сначала пропускается "й", потом "ы" -> остаётся "н"
        assertEquals('н', nextLetter.lastSymbol("Грозный"));
    }

    @Test
    void letterYo_isReplacedWithYe() {
        // Условное слово, чтобы проверить замену ё -> е
        assertEquals('е', nextLetter.lastSymbol("Тестё"));
    }

    @Test
    void upperCase_isHandled() {
        assertEquals('а', nextLetter.lastSymbol("МОСКВА"));
    }

    @Test
    void spacesAroundCity_areIgnored() {
        assertEquals('а', nextLetter.lastSymbol("  Москва  "));
    }

    @Test
    void compoundNames_useLastLetterOfWholeName() {
        assertEquals('д', nextLetter.lastSymbol("Нижний Новгород"));
        assertEquals('у', nextLetter.lastSymbol("Ростов-на-Дону"));
    }

    @Test
    void nullCity_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> nextLetter.lastSymbol(null));
    }

    @Test
    void emptyCity_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> nextLetter.lastSymbol(""));
    }

    @Test
    void onlySpaces_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> nextLetter.lastSymbol("   "));
    }
}