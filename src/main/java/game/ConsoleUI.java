package game;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleUI {

    private static final String EXIT_COMMAND = "\\exit";
    private final String citiesFilePath;

    public ConsoleUI(String citiesFilePath) {
        this.citiesFilePath = citiesFilePath;
    }

    public void start() throws IOException {

        CityDataBase database = new CityDataBase();
        database.loadFromFile(Path.of(citiesFilePath));

        NextLetter letterUtils = new NextLetter();
        Bot bot = new Bot(database);
        GameSession session = new GameSession(database, letterUtils, bot);
        ResultWriter resultWriter = new ResultWriter();


        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
             PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8)) {

            out.println(session.handleMessage(""));

            while (true) {
                out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String line = scanner.nextLine().trim();

                if (line.equalsIgnoreCase(EXIT_COMMAND)) {
                    out.println("Пока! Спасибо за игру!");
                    break;
                }

                out.println(session.handleMessage(line));
            }
        }

        resultWriter.writeResult(session.getState().getSessionLog());
    }
}