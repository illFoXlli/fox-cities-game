package com.fox.game;

import org.w3c.dom.ls.LSOutput;

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
        CityStorage storage = new CityStorage();
        cities = storage.loadCities();
    }

    private int userScore = 0;
    private int computerScore = 0;

    private String lastUserCity = null;
    private String lastComputerCity = null;
    private boolean userGaveUp = false;

    public String processMove(String userCity) {

        if (cities.isEmpty()) {
            return "Список міст не знайдено або він порожній";
        }
        if (userCity == null || userCity.isBlank()) {
            return "Введіть місто";
        }

        String userCityName = userCity.trim();

        if (userCityName.isEmpty()) {
            return "Введіть місто";
        }

        if (isGiveUp(userCityName)) {
            gameOver = true;
            userGaveUp = true;
            gameResult = "Комп'ютер переміг!";
            return "";
        }

        if (usedCities.contains(normalizeKey(userCityName))) {
            return "Місто \"" + userCityName + "\" вже використано";
        }

        if (lastComputerCity != null) {
            char lastLetter = getLastChar(lastComputerCity);

            if (Character.toLowerCase(userCityName.charAt(0)) != lastLetter) {
                return "Має бути місто на \"" + lastLetter + "\" (було: " + lastComputerCity + ")";
            }
        }

        String cityFromList = findCity(userCityName);

        if (cityFromList == null) {
            return "Місто \"" + userCityName + "\" відсутнє у списку";
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

                return "Комп'ютер: " + city;
            }
        }

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
