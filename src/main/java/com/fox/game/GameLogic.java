package com.fox.game;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GameLogic {

    private boolean gameOver = false;
    private String gameResult = "";

    private final List<String> cities;
    private final Set<String> usedCities = new HashSet<>();

    public GameLogic() {
        cities = CityStorage.loadCities();
    }

    private int userScore = 0;
    private int computerScore = 0;

    private String lastUserCity = null;
    private String lastComputerCity = null;
    private boolean userGaveUp = false;

    public MoveResult processMove(String userCity) {

        if (cities.isEmpty()) {
            return error("Список міст не знайдено або він порожній");
        }

        if (userCity == null || userCity.isBlank()) {
            return error("Введіть місто");
        }

        String userCityName = userCity.trim();

        if (userCityName.isEmpty()) {
            return error("Введіть місто");
        }

        if (isGiveUp(userCityName)) {
            gameOver = true;
            userGaveUp = true;
            gameResult = "Комп'ютер переміг!";
            return new MoveResult(
                    MoveStatus.COMPUTER_WON,
                    gameResult,
                    lastUserCity,
                    lastComputerCity,
                    gameOver
            );
        }

        if (usedCities.contains(normalizeKey(userCityName))) {
            return error("Місто \"" + userCityName + "\" вже використано");
        }

        if (lastComputerCity != null) {
            char lastLetter = getLastChar(lastComputerCity);

            if (Character.toLowerCase(userCityName.charAt(0)) != lastLetter) {
                return error("Має бути місто на \"" + lastLetter + "\" (було: " + lastComputerCity + ")");
            }
        }

        String cityFromList = findCity(userCityName);

        if (cityFromList == null) {
            return error("Місто \"" + userCityName + "\" відсутнє у списку");
        }

        usedCities.add(normalizeKey(cityFromList));
        userScore++;
        lastUserCity = cityFromList;

        char lastLetter = getLastChar(cityFromList);

        for (String city : cities) {
            if (!usedCities.contains(normalizeKey(city)) &&
                Character.toLowerCase(city.charAt(0)) == lastLetter) {

                usedCities.add(normalizeKey(city));
                computerScore++;
                lastComputerCity = city;

                return new MoveResult(
                        MoveStatus.SUCCESS,
                        "Комп'ютер: " + city,
                        lastUserCity,
                        lastComputerCity,
                        gameOver
                );
            }
        }

        gameOver = true;

        gameResult = "Ви перемогли! Комп'ютер не знайшов місто на \""
                     + lastLetter + "\"";

        return new MoveResult(
                MoveStatus.USER_WON,
                gameResult,
                lastUserCity,
                lastComputerCity,
                gameOver
        );
    }

    private MoveResult error(String message) {
        return new MoveResult(
                MoveStatus.ERROR,
                message,
                lastUserCity,
                lastComputerCity,
                gameOver
        );
    }

    private String findCity(String cityName) {
        String cityKey = normalizeKey(cityName);

        for (String city : cities) {
            if (normalizeKey(city).equals(cityKey)) {
                return city;
            }
        }

        return null;
    }

    private String normalizeKey(String cityName) {
        return cityName.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isGiveUp(String userCityName) {
        return normalizeKey(userCityName).replace("!", "").equals("здаюсь");
    }

    private char getLastChar(String word) {
        if (word == null || word.isBlank()) {
            return ' ';
        }

        String lower = word.toLowerCase(Locale.ROOT);

        for (int i = lower.length() - 1; i >= 0; i--) {
            char c = lower.charAt(i);

            if (c != 'ь' && c != 'й' && c != '\'' && c != '’') {
                return c;
            }
        }

        return lower.charAt(lower.length() - 1);
    }

    public int getUserScore() {
        return userScore;
    }

    public int getComputerScore() {
        return computerScore;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameResult() {
        return gameResult;
    }

    public String getLastUserCity() {
        return lastUserCity;
    }

    public String getLastComputerCity() {
        return lastComputerCity;
    }

    public boolean hasCities() {
        return !cities.isEmpty();
    }

    public boolean isUserGaveUp() {
        return userGaveUp;
    }
}
