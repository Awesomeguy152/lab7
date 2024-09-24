package org.example.server.system;

import org.example.data.Color;
import org.example.data.MusicGenre;
import org.example.server.exception.DataException;

public class Validator {
    public static void validateCoordinates(long x, long y) throws DataException {
        if (x > 707) {
            throw new DataException("Значение X не может быть больше 707.");
        }
        if (y <= -776) {
            throw new DataException("Значение Y должно быть больше -776.");
        }
    }

    public static void validateNumberOfParticipants(Integer numberOfParticipants) throws DataException {
        if (numberOfParticipants != null && numberOfParticipants <= 0) {
            throw new DataException("Количество участников должно быть больше 0.");
        }
    }
    public static void validateName(String name) throws DataException {
        if (name == null || name.trim().isEmpty()) {
            throw new DataException("Название группы не может быть пустым или null.");
        }
    }


    public static void validateMusicGenre(String genre) throws DataException {
        try {
            if (genre != null) {
                MusicGenre.valueOf(genre.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            throw new DataException("Недопустимый жанр музыки: " + genre);
        }
    }

    public static void validatePersonName(String name) throws DataException {
        if (name == null || name.trim().isEmpty()) {
            throw new DataException("Имя человека не может быть пустым или null.");
        }
    }


    public static void validateWeight(int weight) throws DataException {
        if (weight <= 0) {
            throw new DataException("Вес человека должен быть больше 0.");
        }
    }


    public static void validateColor(String color) throws DataException {
        try {
            if (color != null && !color.isEmpty()) {
                Color.valueOf(color.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            throw new DataException("Недопустимый цвет: " + color);
        }
    }
}
