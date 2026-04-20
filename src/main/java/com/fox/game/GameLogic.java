package com.fox.game;

import java.util.*;

public class GameLogic {

    private boolean gameOver = false;
    private String gameResult = "";

    private final List<String> cities;
    private final Set<String> usedCities = new HashSet<>();

    public GameLogic() {
        CityStorage storage = new CityStorage();
        cities = storage.loadCities();
    }

    private int userScore = 0;
    private int computerScore = 0;

    private String lastUserCity = null;
    private String lastComputerCity = null;

    public String processMove(String userCity) {

        // 1. пустое
        if (userCity == null || userCity.isBlank()) {
            return "Введіть місто";
        }

        String userCityName = userCity.trim();

        if (userCityName.isEmpty()) {
            return "Введіть місто";
        }

        // 2. повтор
        if (usedCities.contains(normalizeKey(userCityName))) {
            return "Місто \"" + userCityName + "\" вже використано";
        }

        // 3. проверка буквы (теперь ПРАВИЛЬНО)
        if (lastComputerCity != null) {
            char lastLetter = getLastChar(lastComputerCity);

            if (Character.toLowerCase(userCityName.charAt(0)) != lastLetter) {
                return "Має бути місто на \"" + lastLetter + "\" (було: " + lastComputerCity + ")";
            }
        }

        // 4. проверка списка
        String cityFromList = findCity(userCityName);

        if (cityFromList == null) {
            return "Місто \"" + userCityName + "\" відсутнє у списку";
        }

        // ✔ сохраняем ход пользователя
        usedCities.add(normalizeKey(cityFromList));
        userScore++;
        lastUserCity = cityFromList;

        char lastLetter = getLastChar(cityFromList);

        // 🤖 ход компьютера
        for (String city : cities) {
            if (!usedCities.contains(normalizeKey(city)) &&
                Character.toLowerCase(city.charAt(0)) == lastLetter) {

                usedCities.add(normalizeKey(city));
                computerScore++;
                lastComputerCity = city;

                return "Комп'ютер: " + city;
            }
        }

        // 🏆 пользователь выиграл
        gameOver = true;

        gameResult = "Ви перемогли! Комп'ютер не знайшов місто на \""
                     + lastLetter + "\"";

        return "";
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

    private char getLastChar(String word) {
        if (word == null || word.isBlank()) {
            return ' ';
        }

        String lower = word.toLowerCase(Locale.ROOT);

        for (int i = lower.length() - 1; i >= 0; i--) {
            char c = lower.charAt(i);

            if (c != 'ь' && c != 'й' && c != '\'') {
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
}