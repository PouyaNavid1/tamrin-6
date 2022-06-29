package com.company.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SendFromClient implements Runnable{
    Socket socket;

    public SendFromClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                String a = scanner.nextLine();
                printWriter.println(a);
                printWriter.flush();
            }
        } catch (Exception w) {
            System.out.println("44");
        }
    }
}
