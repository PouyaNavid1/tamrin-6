package com.company.client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        new Thread(client).start();
        SendFromClient sendFromClient = new SendFromClient(client.socket);
        new Thread(sendFromClient).start();
    }
}
