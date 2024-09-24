package org.example.server.system;

import org.example.client.ScriptExecutor;
import org.example.data.*;
import org.example.server.exception.DataException;
import org.example.server.manager.CollectionManager;
import org.example.server.manager.DataBaseManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.example.data.Person.createFrontMan;

/**
 * Класс Receiver отвечает за создание и управление объектами коллекции MusicBand.
 */
public class Receiver {

    /**
     * Создает новый экземпляр MusicBand на основе пользовательского ввода.
     *
     * @return новый объект MusicBand
     * @throws DataException если введенные данные некорректны
     */
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

    /**
     * Добавляет новый элемент в коллекцию.
     *
     * @param collectionManager менеджер коллекции для управления элементами
     */
    public static String addMusicBand(CollectionManager collectionManager, MusicBand musicBand, String login) {
        try {
            int id = DataBaseManager.insertMusicBand(musicBand, login);
            if (id != 0){
                collectionManager.addMusicBand(musicBand.setId(id));
                return "Элемент успешно добавлен в коллекцию.";
            } return "Ошибка при создании элемента";
        } catch (Exception e) {
            return "Ошибка при создании элемента: " + e.getMessage();
        }
    }


    /**
     * Обновляет существующий элемент в коллекции по его ID.
     *
     * @param collectionManager менеджер коллекции для управления элементами
     * @param id                идентификатор обновляемого элемента
     */
    public static String updateMusicBand(CollectionManager collectionManager, long id, MusicBand newBand, String login) {
        try {
            MusicBand existingBand = collectionManager.getMusicBandById(id);

            if (existingBand == null) {
                return "Элемент с ID " + id + " не найден.";
            }
           if(DataBaseManager.updateMusicBandById((int) id, newBand, login)){
               newBand.setId(existingBand.getId());
               collectionManager.getMusicBands().remove(existingBand);
               collectionManager.addMusicBand(newBand);
               return "Элемент успешно обновлен.";
           } else return "Это не ваш элемент";


        } catch (Exception e) {
            return "Ошибка при обновлении элемента: " + e.getMessage();
        }

    }

    /**
     * Удаляет элемент из коллекции по его ID.
     *
     * @param collectionManager менеджер коллекции для управления элементами
     * @param id                идентификатор удаляемого элемента
     */
    public static String removeById(CollectionManager collectionManager, long id, String login) {
        if (DataBaseManager.removeMusicBandByName(login, String.valueOf(id), collectionManager)) {
            return "Элемент с ID " + id + " успешно удален.";
        } else {
            return "Элемент с ID " + id + " не Ваш или не найден.";
        }
    }

    /**
     * Очищает коллекцию.
     *
     * @param collectionManager менеджер коллекции для управления элементами
     */
    public static String clearCollection(CollectionManager collectionManager, String login) {
        DataBaseManager.clear(collectionManager, login);
        return "Коллекция успешно очищена.";
    }

    /**
     * Добавляет новый элемент, если он меньше минимального в коллекции.
     *
     * @param collectionManager менеджер коллекции для управления элементами
     */
    public static String addIfMin(CollectionManager collectionManager, MusicBand newBand, String login) {
        try {
            if (!collectionManager.getMusicBands().isEmpty()) {

                MusicBandComparator musicBandComparator = new MusicBandComparator();

                ArrayList<MusicBand> copy = new ArrayList<>();
                for (MusicBand musicBand : collectionManager.getMusicBands()) {
                    copy.add(musicBand);
                }
                copy.sort(musicBandComparator);

                if (copy.get(0).compareTo(newBand) > 0) {
                    int id = DataBaseManager.insertMusicBand(newBand, login);
                    if (id > 0){
                        collectionManager.addMusicBand(newBand);
                        return "Элемент успешно добавлен в коллекцию.";
                    } return "Возникла ошибка";
                } else {
                    return "Элемент не был добавлен, так как он не является минимальным.";
                }
            } else {
                return "Коллекция пуста";
            }
        } catch (DataException e) {
            return "Ошибка при создании элемента: " + e.getMessage();
        }
    }


    /**
     * Удаляет все элементы коллекции, которые больше указанного.
     *
     * @param collectionManager менеджер коллекции для управления элементами
     */

    public static String removeGreater(CollectionManager collectionManager, MusicBand comparisonBand, String login) {
        try {
            for (MusicBand musicBand: collectionManager.getMusicBands()){
                if (musicBand.compareTo(comparisonBand) > 0){
                    DataBaseManager.removeMusicBandByName(login, String.valueOf(musicBand.getId()), collectionManager);
                }
            }
            return "Удалены элементы, превышающие указанный.";
        } catch (DataException e) {
            return "Ошибка создания элемента: " + e.getMessage();
        }
    }

    public static String removeLower(CollectionManager collectionManager, MusicBand comparisonBand, String login) {
        try {
            for (MusicBand musicBand: collectionManager.getMusicBands()){
                if (musicBand.compareTo(comparisonBand) < 0){
                    DataBaseManager.removeMusicBandByName(login, String.valueOf(musicBand.getId()), collectionManager);
                }
            }
            return "Удалены элементы, меньшие указанного.";
        } catch (DataException e) {
            return "Ошибка создания элемента: " + e.getMessage();
        }
    }

    public static String sumOfNumberOfParticipants(CollectionManager collectionManager) {
        int sum = 0; // Инициализация суммы
        for (MusicBand musicBand : collectionManager.getMusicBands()) {
            if (musicBand.getNumberOfParticipants() != null) {
                sum += musicBand.getNumberOfParticipants(); // Суммируем количество участников
            }
        }
        return "Сумма количества участников: " + sum;
    }

    public static String countLessThanGenre(CollectionManager collectionManager, MusicGenre genre) {
        int count = 0; // Инициализация счетчика
        for (MusicBand musicBand : collectionManager.getMusicBands()) {
            if (musicBand.getGenre() != null && musicBand.getGenre().compareTo(genre) < 0) {
                count++; // Увеличиваем счетчик, если жанр меньше заданного
            }
        }
        return "Количество групп с жанром меньше " + genre + ": " + count;
    }

    public static String filterLessThanNumberOfParticipants(CollectionManager collectionManager, int numberOfParticipants) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (MusicBand musicBand : collectionManager.getMusicBands()) {
            if (musicBand.getNumberOfParticipants() != null && musicBand.getNumberOfParticipants() < numberOfParticipants) {
                stringBuilder.append(musicBand).append("\n"); // Выводим группы, у которых количество участников меньше заданного
            }
        }
        return stringBuilder.toString();
    }


    public static String showData(CollectionManager collectionManager) {
        if (collectionManager.getMusicBands().isEmpty()) {
            return "Коллекция пуста.";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Содержимое коллекции:\n");
            MusicBandComparatorByName musicBandComparatorByName = new MusicBandComparatorByName();
            collectionManager.getMusicBands().stream().sorted(musicBandComparatorByName);
            for (MusicBand musicBand : collectionManager.getMusicBands()) {
                stringBuilder.append(musicBand).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}
