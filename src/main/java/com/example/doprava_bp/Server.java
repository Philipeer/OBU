package com.example.doprava_bp;

import javax.security.auth.kerberos.EncryptionKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

    public Cryptogram userCryptogram;
    public Cryptogram receiverCryptogram;

    public Server(ObuParameters obuParameters) throws Exception{
        ServerSocket serverSocket = new ServerSocket(10002);
        System.out.println("Server is up and running on ip " + serverSocket.getInetAddress().getLocalHost().getHostAddress() + " port: " + serverSocket.getLocalPort());
        Socket socket = serverSocket.accept();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


        //Cryptogram userCryptogram = new Cryptogram();
        Random rnd = new Random();
        Cryptogram pokus = new Cryptogram();
        pokus.setNonce(rnd.nextInt());
        pokus.setIdr(obuParameters.getIdr());
        receiverCryptogram = pokus;
        objectOutputStream.writeObject(receiverCryptogram);

        //Cryptogram recieverCryptogram = new Cryptogram();
        userCryptogram = (Cryptogram) objectInputStream.readObject();


        System.out.println("Nonce user: " + userCryptogram.getNonce());
        System.out.println("Nonce reciever: " + receiverCryptogram.getNonce());

        //objectOutputStream.close();
        socket.close();
    }

    public void sendAndReceiveObject(int port) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is up and running on ip " + serverSocket.getInetAddress().getLocalHost().getHostAddress() + " port: " + serverSocket.getLocalPort());
        Socket socket = serverSocket.accept();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        userCryptogram = (Cryptogram) objectInputStream.readObject();
        socket.close();
    }

    public void sendAuthenticationMessage(boolean authenticated) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10004);
        System.out.println("Server is up and running on ip " + serverSocket.getInetAddress().getLocalHost().getHostAddress() + " port: " + serverSocket.getLocalPort());
        Socket socket = serverSocket.accept();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        userCryptogram.setAuthenticated(authenticated);

        objectOutputStream.writeObject(userCryptogram);

        socket.close();
    }
}
