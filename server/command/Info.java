package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;

import java.io.Serializable;

public class Info implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Выводит информацию о коллекции.";
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) throws WrongArgumentException {
        if (args.length > 1) {
            throw new WrongArgumentException();
        }
        return "Тип коллекции: " + collectionManager.getMusicBands().getClass().getName() + "\n"
                + "Количество элементов: " + collectionManager.size();

    }
}
