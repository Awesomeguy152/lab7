package org.example.server.network;

import org.example.data.network.Request;
import org.example.data.network.Response;
import org.example.server.manager.CollectionManager;
import org.example.server.manager.CommandManager;
import org.example.server.manager.DataBaseManager;
import org.example.server.system.ReadXML;
import org.example.server.system.Receiver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    private ServerSocketChannel serverSocketChannel;
    private CommandManager commandManager;
    private CollectionManager collectionManager;
    private ExecutorService responseExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ServerTCP(String host, int port) throws IOException {

        // Создаем серверный канал
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        // Привязываем канал к порту
        serverSocketChannel.bind(new InetSocketAddress(host, port));
        commandManager = new CommandManager();
        collectionManager = new CollectionManager();

        DataBaseManager.connectToDataBase();
        DataBaseManager.getDataFromDatabase(collectionManager);
    }

    public void start() throws IOException, ClassNotFoundException {
        // Создаем селектор
        Selector selector = Selector.open();
        // Регистрируем серверный канал для приема подключений
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP-сервер запущен:" + serverSocketChannel.getLocalAddress().toString());
        // Основной цикл обработки событий
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys(); // получаем список ключей от каналов, готовых к работе
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator(); // получаем итератор ключей
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // Обработка событий
                if (key.isAcceptable()) {
                    handleAccept(key, selector);
                } else if (key.isReadable()) {
                    handleRead(key, selector);
                }
                keyIterator.remove();
            }
        }
    }

    // Обработка события ACCEPT (новое подключение)
    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        // Регистрируем клиентский канал для чтения
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Новое подключение от " + clientChannel.getRemoteAddress());
    }

    // Обработка события READ (получение данных)
    private void handleRead(SelectionKey key, Selector selector) throws IOException, ClassNotFoundException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        // Читаем данные от клиента
        ByteBuffer buffer = ByteBuffer.allocate(5000);
        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();

                Thread requestHandler = new Thread(() -> {
                    try {
                        ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                        ObjectInputStream oi = new ObjectInputStream(bi);
                        Request request = (Request) oi.readObject();

                        System.out.println("Получено сообщение от клиента: " + request.getMessage());

                        bi.close();
                        oi.close();

                        // Создаем новый поток для обработки запроса
                        Thread responseHandler = new Thread(() -> {
                            Response response = commandManager.executeCommand(request, collectionManager);
                            responseExecutor.execute(() -> {
                                try {
                                    sendResponse(key, response);
                                } catch (IOException e) {
                                    System.err.println("Ошибка при обработке запроса: " + e.getMessage());
                                }
                            });
                        });
                        responseHandler.start();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                }});
                requestHandler.start();

            } else if (bytesRead == -1) {
                // Соединение закрыто клиентом
                clientChannel.close();
                System.out.println("Соединение закрыто клиентом.");
            }
        } catch (SocketException e) {
            System.err.println("Соединение сброшено: " + e.getMessage());
            clientChannel.close(); // Закрываем канал, если соединение сброшено
        } catch (StreamCorruptedException e) {
            System.out.println("Возникла ошибка: " + e.getMessage());
        }
    }

    public void sendResponse(SelectionKey key, Response response) throws IOException {
        // Используем FixedThreadPool для отправки ответов
        responseExecutor.execute(() -> {
            try {
                SocketChannel client = (SocketChannel) key.channel(); // получаем канал для работы
                client.configureBlocking(false);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(response);
                objectOutputStream.close();
                ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
                client.write(buffer);
            } catch (IOException e) {
                System.err.println("Ошибка при отправке ответа: " + e.getMessage());
            }
        });
    }
}
