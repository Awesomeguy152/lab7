package org.example.client;

import java.io.IOException;

/**
 * Main class in witch client starts
 */
public class Main {
    /**
     * @param args path to xml, host, port
     * @throws IOException, ClassNotFoundException
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println(args[0] + " " + args[1]);
        ClientTCP client = new ClientTCP(args[0], Integer.parseInt(args[1]));
        // ClientUDP client = new ClientUDP(args[0], Integer.parseInt(args[1]));
        client.start();
    }
}
