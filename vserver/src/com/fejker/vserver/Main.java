package com.fejker.vserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String username = null;
    static DatagramSocket socket;
    private static ArrayList<Thread> threads = new ArrayList<>();

    static {
        try {
            socket = new DatagramSocket(27000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    static byte[] receive = new byte[1024];

    static public List<Thread> getThreadList() {
        return threads;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        Thread listen = new Thread() {
            @Override
            public void run() {
                String input = "";
                while (true) {
                    try {
                        input = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (input.equals("list")) {
                    System.out.println(getThreadList());
                }
                }
            }
        };
        listen.start();
        while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
                socket.receive(receivePacket);
                username = new String(data(receive));
                Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        clientHandler(receivePacket);
                    } catch (IOException | InterruptedException e) {
                        System.out.println(username + " " + "lost.");
                    }
                }
            };
            t.start();
            threads.add(t);
            t.setName(username);
            //System.out.println(t.getName());
        }
    }


    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }


    private static void clientHandler(DatagramPacket receivePacket) throws IOException, InterruptedException {
        byte[] buf;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        System.out.println(dtf.format(now) + "\n" + username + " " + clientAddress.toString().substring(1) + " " + clientPort);

        while (true) {
            buf = "stay alive".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, clientAddress, clientPort);
            socket.send(sendPacket);
            Thread.sleep(500);
        }
    }
}
