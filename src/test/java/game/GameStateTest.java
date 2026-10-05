package game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    private GameState state;

    @BeforeEach
    public void setUp() {
        // Перед каждым тестом создаем чистый объект состояния
        state = new GameState();
    }

    @Test
    public void testInitialState() {
        // Сразу после создания игра не должна быть начата
        assertFalse(state.isStarted(), "При создании игра не должна иметь статус started");
        assertTrue(state.getSessionLog().isEmpty(), "История городов должна быть пустой");
    }

    @Test
    public void testStartAndReset() {
        state.start();
        assertTrue(state.isStarted(), "Метод start() должен переводить игру в активное состояние");

        state.reset();
        assertFalse(state.isStarted(), "Метод reset() должен сбрасывать состояние игры");
    }

    @Test
    public void testAddCityAndIsUsed() {
        state.addCity("Москва");

        // Проверяем, что город запомнился
        assertTrue(state.isUsed("Москва"), "Город должен числиться как использованный");

        // Проверяем  регистр(игрок может написать с маленькой буквы)
        assertTrue(state.isUsed("москва"), "Проверка не должна зависеть от регистра букв");

        // Проверяем другой город
        assertFalse(state.isUsed("Абакан"), "Недобавленный город не должен числиться использованным");
    }

    @Test
    public void testExpectedLetter() {
        state.setExpectedLetter('к');
        assertEquals('к', state.getExpectedLetter(), "Ожидаемая буква должна корректно сохраняться");
    }
}