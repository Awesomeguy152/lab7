package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.system.Receiver;

import java.io.Serializable;

public class SumOfNumberOfParticipants implements BaseCommand, Serializable {

    @Override
    public String getDescription() {
        return "Выводит сумму значений поля numberOfParticipants для всех элементов коллекции.";
    }

    @Override
    public String getName() {
        return "sum_of_number_of_participants";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) {
        return Receiver.sumOfNumberOfParticipants(collectionManager);
    }
}
