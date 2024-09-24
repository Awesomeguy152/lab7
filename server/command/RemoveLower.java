package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

public class RemoveLower implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Удаляет из коллекции все элементы, меньшие, чем заданный.";
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) {
        return Receiver.removeLower(collectionManager, request.getMusicBand(),request.getLogin());
    }
}
