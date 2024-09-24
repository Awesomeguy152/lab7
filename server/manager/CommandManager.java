package org.example.server.manager;


import org.example.data.network.Request;
import org.example.data.network.Response;
import org.example.server.command.*;
import org.example.server.exception.WrongArgumentException;

import java.util.HashMap;


public class CommandManager {
    private final HashMap<String, BaseCommand> commands = new HashMap<>();

    public CommandManager() {
        commands.put("help", new Help());
        commands.put("info", new Info());
        commands.put("show", new Show());
        commands.put("add", new Add());
        commands.put("add_if_min", new AddIfMin());
        commands.put("clear", new Clear());
        commands.put("filter_less_than_number_of_participants", new FilterLessThanNumberOfParticipants());
        commands.put("remove_by_id", new RemoveById());
        commands.put("remove_greater", new RemoveGreater());
        commands.put("remove_lower", new RemoveLower());
        commands.put("update", new Update());
        commands.put("sum_of_number_of_participants", new SumOfNumberOfParticipants());
        commands.put("count_less_than_genre", new CountLessThanGenre());
        commands.put("login", new Login());
        commands.put("reg", new Register());
    }

    public Response executeCommand(Request request, CollectionManager collectionManager) {
        String[] data = request.getMessage().split(" ");
            try {
                System.out.println(collectionManager.size());
                return new Response(
                        commands.get(request.getCommand().getName())
                                .execute(collectionManager, request, data));
            } catch (WrongArgumentException e){
                return new Response(
                        e.getMessage());
            }
        }

    }


