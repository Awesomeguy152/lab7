package org.example.server.command;


import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;

import java.io.Serializable;

public class Help implements BaseCommand, Serializable {
    @Override
    public String execute(CollectionManager manager, Request request, String[] args) throws WrongArgumentException {
        if (args.length > 1) {
            throw new WrongArgumentException();
        }
        System.out.println("Доступные команды:");
        System.out.println("help : вывести справку по доступным командам");
        System.out.println("info : вывести информацию о коллекции");
        System.out.println("show : вывести все элементы коллекции");
        System.out.println("add {element} : добавить новый элемент в коллекцию");
        System.out.println("update id {element} : обновить элемент коллекции по id");
        System.out.println("remove_by_id id : удалить элемент коллекции по id");
        System.out.println("clear : очистить коллекцию");
        System.out.println("execute_script file_name : выполнить скрипт из файла");
        System.out.println("exit : завершить программу");
        System.out.println("add_if_min {element} : добавить элемент, если он меньше минимального");
        System.out.println("remove_greater {element} : удалить элементы, превышающие заданный");
        System.out.println("remove_lower {element} : удалить элементы, меньшие заданного");
        System.out.println("sum_of_number_of_participants : вывести сумму участников всех групп");
        System.out.println("count_less_than_genre genre : вывести количество групп с жанром меньше заданного");
        System.out.println("filter_less_than_number_of_participants numberOfParticipants : вывести группы с количеством участников меньше заданного");
        String commands = "help : вывести справку по доступным командам\n"
                + "info : вывести информацию о коллекции\n"
                + "show : вывести все элементы коллекции\n"
                + "add {element} : добавить новый элемент в коллекцию\n"
                + "update id {element} : обновить элемент коллекции по id\n"
                + "remove_by_id id : удалить элемент коллекции по id\n"
                + "clear : очистить коллекцию\n"
                + "execute_script file_name : выполнить скрипт из файла\n"
                + "exit : завершить программу\n"
                + "add_if_min {element} : добавить элемент, если он меньше минимального\n"
                + "remove_greater {element} : удалить элементы, превышающие заданный\n"
                + "remove_lower {element} : удалить элементы, меньшие заданного\n"
                + "sum_of_number_of_participants : вывести сумму участников всех групп\n"
                + "count_less_than_genre genre : вывести количество групп с жанром меньше заданного\n"
                + "filter_less_than_number_of_participants numberOfParticipants : вывести группы с количеством участников меньше заданного\n";
        return commands;

    }

    @Override
    public String getDescription() {
        return "Выводит справку по доступным командам.";
    }

    @Override
    public String getName() {
        return "help";
    }
}
