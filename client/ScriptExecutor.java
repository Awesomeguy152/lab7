package org.example.client;

import org.example.data.*;
import org.example.data.network.Request;
import org.example.server.manager.CollectionManager;
import org.example.server.manager.CommandManager;
import org.example.server.system.Receiver;
import org.example.server.system.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Класс ScriptExecutor отвечает за выполнение команд из файла скрипта.
 */
public class ScriptExecutor {

    /**
     * Выполняет команды из указанного файла.
     * @param fileName имя файла скрипта
     */
    public static void executeScript(ClientTCP clientTCP, String fileName ) {
        File scriptFile = new File(fileName);

        if (!scriptFile.canRead()){
            System.out.println("Невозможно прочитать файл (ошибка доступа)");
            return;
        }

        try (Scanner fileScanner = new Scanner(scriptFile)) {
            CommandManager commandManager = new CommandManager();
            while (fileScanner.hasNextLine()) {
                String command = fileScanner.nextLine().trim();
                if (!command.isEmpty()) {
                    try {
                        if (command.contains("add") || command.contains("update") || command.contains("add_if_min") ||
                                command.contains("remove_greater") || command.contains("remove_lower")){
                            ArrayList<String> args = new ArrayList<>();
                            for (int i = 0; i<9; i++) {
                                args.add(fileScanner.nextLine().trim());
                            }
                            Validator.validateName(args.get(0));
                            Validator.validateCoordinates(Long.parseLong(args.get(1)), Long.parseLong(args.get(2)));

                            Validator.validateNumberOfParticipants(Integer.valueOf(args.get(3)));

                            MusicBand musicBand = new MusicBand();
                            musicBand.setName(args.get(0));
                            musicBand.setCoordinates(new Coordinates(Long.parseLong(args.get(1)), Long.parseLong(args.get(2))));
                            musicBand.setNumberOfParticipants(Integer.valueOf(args.get(3)));

                            Person person = new Person();
                            person.setName(args.get(4));
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate birthday = LocalDate.parse(args.get(5), formatter);
                            person.setBirthday(Date.from(birthday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                            person.setWeight(Integer.parseInt(args.get(6)));
                            person.setEyeColor(Color.valueOf(args.get(7)));

                            musicBand.setFrontMan(person);
                            musicBand.setGenre(MusicGenre.valueOf(args.get(8)));

                            System.out.println(musicBand);

                            Request request2 = new Request(command);
                            request2.setMusicBand(musicBand);
                            request2.setCommand(ClientTCP.commands.get(command.split(" ")[0]));
                            clientTCP.sendRequest(request2);
                            clientTCP.getResponse();
                        }
                        else if(command.contains("execute_script")){
                            ScriptExecutor.executeScript(clientTCP, command.split(" ")[1]);
                        }
                        else{
                            Request request = new Request(command);
                            request.setCommand(ClientTCP.commands.get(command.split(" ")[0]));
                            clientTCP.sendRequest(request);
                            clientTCP.getResponse();
                        }
                    } catch (Exception e) {
                        System.out.println("Ошибка выполнения команды: " + command + ". " + e.getMessage());
                    }
                }
            }

            System.out.println("Скрипт выполнен успешно.");

        } catch (FileNotFoundException e) {
            System.out.println("Файл скрипта не найден: " + fileName);
        } catch (Exception e) {
            System.out.println("Произошла ошибка при выполнении скрипта: " + e.getMessage());
        }
    }
}
