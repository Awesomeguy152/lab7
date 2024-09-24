package org.example.server.system;


import org.example.server.network.ServerTCP;
// принести лабораторную в виде 2 jar файлов (сервер и клиент). Сервер нужно запустить на гелиосе, а клиент локально (с помощью ssh туннеля)
public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

    }));

        if (args.length < 1) {
            System.out.println("Необходимо указать путь к XML-файлу.");
            return;
        }
        try {
            ServerTCP server = new ServerTCP(args[0], Integer.parseInt(args[1]));
            // ServerUDP server = new ServerUDP(args[0], Integer.parseInt(args[1]), args[2]);
            server.start();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
