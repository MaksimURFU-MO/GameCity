package game;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityDataBase {

    private final Map<Character, List<String>> citiesByLetter = new HashMap<>();

    public Map<Character, List<String>> loadFromFile(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        citiesByLetter.clear();

        for (String line : lines) {
            String city = line.trim();
            if (city.isEmpty()) continue;

            char firstLetter = Character.toLowerCase(city.charAt(0));
            citiesByLetter.putIfAbsent(firstLetter, new ArrayList<>());
            citiesByLetter.get(firstLetter).add(city);
        }

        return citiesByLetter;
    }

    public List<String> getCitiesByLetter(char letter) {
        char normalizedLetter = Character.toLowerCase(letter);
        List<String> cities = citiesByLetter.get(normalizedLetter);

        if (cities == null) {
            return new ArrayList<>();
        }

        return cities;
    }

    public boolean cityExists(String city) {
        if (city == null || city.isEmpty()) {
            return false;
        }

        String trimmedCity = city.trim();
        char firstLetter = Character.toLowerCase(trimmedCity.charAt(0));

        List<String> candidates = citiesByLetter.get(firstLetter);
        if (candidates == null) {
            return false;
        }

        for (String candidate : candidates) {
            if (candidate.equalsIgnoreCase(trimmedCity)) {
                return true;
            }
        }
        return false;
    }
}