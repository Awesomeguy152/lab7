package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

public class Show implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Отображает все элементы коллекции.";
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) throws WrongArgumentException {
        if (args.length > 1) {
            throw new WrongArgumentException();
        }
        return Receiver.showData(collectionManager);
    }
}
