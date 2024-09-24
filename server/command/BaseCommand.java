package org.example.server.command;

import org.example.data.network.Request;
import org.example.server.exception.WrongArgumentException;
import org.example.server.manager.CollectionManager;

import java.io.Serializable;

public interface BaseCommand extends Serializable {
        String getDescription();
        String getName();
        String execute(CollectionManager collectionManager, Request request, String[] args) throws WrongArgumentException;
    }
