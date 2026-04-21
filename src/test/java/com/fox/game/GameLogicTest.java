package com.fox.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    @Test
    void shouldReturnErrorIfCityIsEmpty() {
        GameLogic logic = new GameLogic();

        String result = logic.processMove("");

        assertEquals("Введіть місто", result);
    }

    @Test
    void shouldReturnErrorIfCityNotInList() {
        GameLogic logic = new GameLogic();

        String result = logic.processMove("Лондон");

        assertTrue(result.contains("відсутнє"));
    }

    @Test
    void shouldIncreaseScoreOnValidMove() {
        GameLogic logic = new GameLogic();

        logic.processMove("Київ");

        assertEquals(1, logic.getUserScore());
    }

    @Test
    void shouldNotAllowSameCityTwice() {
        GameLogic logic = new GameLogic();

        logic.processMove("Київ");
        String result = logic.processMove("Київ");

        assertTrue(result.contains("вже використано"));
    }

    @Test
    void shouldAcceptCityInDifferentCase() {
        GameLogic logic = new GameLogic();

        logic.processMove("київ");

        assertEquals(1, logic.getUserScore());
    }

    @Test
    void shouldNotAllowSameCityInDifferentCase() {
        GameLogic logic = new GameLogic();

        logic.processMove("Київ");
        String result = logic.processMove("київ");

        assertTrue(result.contains("вже використано"));
    }

    @Test
    void computerShouldNotRepeatUserCity() {
        GameLogic logic = new GameLogic();

        logic.processMove("Запоріжжя");

        assertNotEquals(logic.getLastUserCity(), logic.getLastComputerCity());
    }

    @Test
    void shouldFinishGameWhenUserTypesGiveUp() {
        GameLogic logic = new GameLogic();

        logic.processMove("здаюсь");

        assertTrue(logic.isGameOver());
        assertTrue(logic.isUserGaveUp());
        assertEquals("Комп'ютер переміг!", logic.getGameResult());
    }
}
