package org.example.client;

import org.example.data.Coordinates;
import org.example.data.MusicBand;
import org.example.data.MusicGenre;
import org.example.data.Person;
import org.example.server.exception.DataException;
import org.example.server.system.Validator;

import java.util.Scanner;

import static org.example.data.Person.createFrontMan;

public class Generator {
    public static MusicBand createMusicBand() throws DataException {
        Scanner scanner = new Scanner(System.in);
        MusicBand musicBand = new MusicBand();

        while (true) {
            try {
                System.out.print("Введите имя группы: ");
                String name = scanner.nextLine().trim();
                Validator.validateName(name);
                musicBand.setName(name);
                break;
            } catch (DataException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("Введите координаты X: ");
                long x = Long.parseLong(scanner.nextLine().trim());
                System.out.print("Введите координаты Y: ");
                long y = Long.parseLong(scanner.nextLine().trim());
                Validator.validateCoordinates(x, y);
                musicBand.setCoordinates(new Coordinates((int) x, y));
                break;
            } catch (NumberFormatException | DataException e) {
                System.out.println("Ошибка ввода координат: " + e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("Введите количество участников: ");
                Integer participants = Integer.parseInt(scanner.nextLine().trim());
                Validator.validateNumberOfParticipants(participants);
                musicBand.setNumberOfParticipants(participants);
                break;
            } catch (NumberFormatException | DataException e) {
                System.out.println("Ошибка ввода количества участников: " + e.getMessage());
            }
        }
        Person frontMan = createFrontMan();
        musicBand.setFrontMan(frontMan);

        while (true) {
            try {
                System.out.print("Выберите жанр (PSYCHEDELIC_ROCK, PSYCHEDELIC_CLOUD_RAP, SOUL, POP): ");
                String genreInput = scanner.nextLine().trim();
                MusicGenre genre = MusicGenre.valueOf(genreInput.toUpperCase());
                musicBand.setGenre(genre);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Некорректный жанр: " + e.getMessage());
            }
        }

        return musicBand;
    }
}
