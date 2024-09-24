package org.example.data.network;

import org.example.data.MusicBand;
import org.example.server.command.BaseCommand;
import org.example.server.manager.CommandManager;

import java.io.Serializable;

public class Request implements Serializable {
    private String message;
    private MusicBand musicBand;
    private BaseCommand command;
    private String login;
    private String password;
    public Request(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MusicBand getMusicBand() {
        return musicBand;
    }

    public void setMusicBand(MusicBand musicBand) {
        this.musicBand = musicBand;
    }

    public BaseCommand getCommand() {
        return command;
    }

    public void setCommand(BaseCommand command) {
        this.command = command;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Request{" +
                "message='" + message + '\'' +
                ", musicBand=" + musicBand +
                '}';
    }
}
