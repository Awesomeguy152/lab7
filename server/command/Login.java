package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;
import org.example.server.manager.DataBaseManager;

import java.io.Serializable;

public class Login implements BaseCommand, Serializable {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String execute(CollectionManager collectionManager, Request request, String[] args) throws WrongArgumentException {
        if (DataBaseManager.checkUser(request.getLogin(), request.getPassword())){
            return "Вы успешно вошли в систему!";
        } else return "Некорректный логин или пароль";
    }
}
