package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;
import org.example.server.manager.DataBaseManager;

import java.io.Serializable;

public class Register implements BaseCommand, Serializable {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "reg";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) throws WrongArgumentException {
        if (DataBaseManager.insertUser(request.getLogin(), request.getPassword())){
            return "Вы успешно зарегистрировались!";
        } else return "Ошибка регистрации, попробуйте позже";
    }
}
