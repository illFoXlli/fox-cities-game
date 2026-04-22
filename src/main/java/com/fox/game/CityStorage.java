package com.fox.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CityStorage {

    public List<String> loadCities() {
        Set<String> cities = new LinkedHashSet<>();

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("cities.txt");

            if (is == null) {
                throw new CityStorageException("Файл cities.txt не знайдено");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String city = line.trim();

                    if (!city.isEmpty()) {
                        cities.add(city);
                    }
                }
            }
        } catch (IOException e) {
            throw new CityStorageException("Не вдалося прочитати файл cities.txt", e);
        }

        return new ArrayList<>(cities);
    }

}
