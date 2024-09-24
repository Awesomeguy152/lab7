package org.example.server.command;

import org.example.data.MusicGenre;
import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;


public class CountLessThanGenre implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Выводит количество элементов, значение поля genre которых меньше заданного.";
    }

    @Override
    public String getName() {
        return "count_less_than_genre";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) throws WrongArgumentException {
        if (args.length != 2) {
            throw new WrongArgumentException();
        }

        try {
            MusicGenre genre = MusicGenre.valueOf(args[1].toUpperCase());
            return Receiver.countLessThanGenre(collectionManager, genre);
        } catch (IllegalArgumentException e) {
            return "Недопустимый жанр: " + args[1];
        }
    }
}
