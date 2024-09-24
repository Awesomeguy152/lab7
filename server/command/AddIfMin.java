package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

import static org.example.server.system.Receiver.createMusicBand;

public class AddIfMin implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента.";
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) {
        return Receiver.addIfMin(collectionManager, request.getMusicBand(), request.getLogin());
    }
}