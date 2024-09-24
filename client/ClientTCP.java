package org.example.client;

import org.example.data.MusicBand;
import org.example.data.network.Request;
import org.example.data.network.Response;
import org.example.server.command.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Client class
 */
public class ClientTCP {
    static HashMap<String, BaseCommand> commands = new HashMap();
    private InetSocketAddress serverAddress;
    private SocketChannel socket;
    public static boolean isRegistered = false;
    public static String login;
    public static String password;

    public ClientTCP(String host, int port) {
        serverAddress = new InetSocketAddress(host, port);
    }

    public void connect() throws IOException {
        // Подключение к серверу
        socket = SocketChannel.open();
        socket.connect(serverAddress);
        socket.configureBlocking(false);
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

    public void sendRequest(Request request) throws IOException {
        // Отправка сообщения на сервер
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);

        objectOutputStream.close();
        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        socket.write(buffer);

        System.out.println("Запрос отправлен на сервер");
    }

    public String getResponse() throws IOException, ClassNotFoundException, InterruptedException {
        // Получение ответа от сервера
        Thread.sleep(100);
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        socket.read(buffer);

        ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Response response = (Response) oi.readObject();

        System.out.println("Получен ответ от сервера: \n" + response.getMessage());
        return response.getMessage();
    }

    public void start() throws InterruptedException {
        try {
            connect();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                String inputcommand = input.split(" ")[0];
                if (input.contains("execute_script") && isRegistered) {
                    ScriptExecutor.executeScript(this, input.split(" ")[1]);
                    continue;
                }
                if (inputcommand.equals("exit")) {
                    System.exit(0);
                }
                if (commands.keySet().contains(inputcommand)) {
                    Request request = new Request(input);
                    if (isRegistered) {
                        request.setLogin(login);
                        request.setPassword(password);
                        if (input.contains("add") || input.contains("update") || input.contains("add_if_min") ||
                                input.contains("remove_greater") || input.contains("remove_lower")) {
                            MusicBand musicBand = Generator.createMusicBand();
                            request.setMusicBand(musicBand);
                        }

                        try {
                            request.setCommand(commands.get(inputcommand));
                            sendRequest(request);
                            System.out.println("Отправлено");
                            getResponse();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            System.out.println("Сервер временно не доступен, повторите попытку позже.");
                            connect();
                        }
                    } else {
                        if (inputcommand.equals("login") || inputcommand.equals("reg")){
                            System.out.println("Введите имя:");
                            login = scanner.nextLine();
                            System.out.println("Введите пароль:");
                            password = scanner.nextLine();

                            request.setCommand(commands.get(inputcommand));
                            request.setLogin(login);
                            request.setPassword(password);

                            sendRequest(request);
                            System.out.println("Отправлено");
                            String answer = getResponse();
                            if (answer.contains("вошли") || answer.contains("зарегистрировались")){
                                isRegistered = true;
                            }
                        } else {
                            System.out.println("Войдите или зарегистрируйтесь (login/reg)");
                        }
                    }

                } else {
                    System.out.println("Команда не найдена.");
                }

            }
        } catch (IOException e) {
            System.err.println("Ошибка соединения с сервером: " + e.getMessage());
            Thread.sleep(3000);
            start();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
