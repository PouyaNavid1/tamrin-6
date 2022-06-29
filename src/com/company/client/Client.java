package com.company.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Client implements Runnable{
    Socket socket;

    public Client() throws IOException {
        this.socket =  new Socket("localhost", 8000);
    }

    @Override
    public void run() {
        try {


            Scanner scanner = new Scanner(socket.getInputStream());

            while (true) {


                    try {
                        String input = scanner.nextLine();
                        System.out.println("Message from server: " + input);
                    } catch (Exception e) {
                        System.out.println("ee");
                    }




            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
