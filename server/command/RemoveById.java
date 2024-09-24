package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

public class RemoveById implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Удаляет элемент коллекции по его ID.";
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) {
        if (args.length < 2) {
            return "Использование: remove_by_id <id>";
        }

        try {
            long id = Long.parseLong(args[1]);
            return Receiver.removeById(collectionManager, id, request.getLogin());
        } catch (NumberFormatException e) {
            return "Неверный формат ID. Пожалуйста, введите числовое значение.";
        }
    }
}
