package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

public class FilterLessThanNumberOfParticipants implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Выводит элементы, значение поля numberOfParticipants которых меньше заданного.";
    }

    @Override
    public String getName() {
        return "filter_less_than_number_of_participants";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) {
        if (args.length < 2) {
            return "Использование: filter_less_than_number_of_participants <number>";
        }

        try {
            int numberOfParticipants = Integer.parseInt(args[1]);
            return Receiver.filterLessThanNumberOfParticipants(collectionManager, numberOfParticipants);
        } catch (NumberFormatException e) {
            return "Неверный формат числа: " + args[1];
        }
    }
}

