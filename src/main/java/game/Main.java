package game;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String citiesFilePath = (args.length > 0) ? args[0] : "src/main/resources/cities.txt";

        ConsoleUI gameUI = new ConsoleUI(citiesFilePath);
        gameUI.start();
    }
}