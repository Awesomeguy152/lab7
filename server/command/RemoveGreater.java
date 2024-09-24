package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

import static org.example.server.system.Receiver.createMusicBand;
import static org.example.server.system.Receiver.removeGreater;

public class RemoveGreater implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Удаляет из коллекции все элементы, превышающие заданный.";
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) {
        return Receiver.removeGreater(collectionManager, request.getMusicBand(),request.getLogin());
    }
}
