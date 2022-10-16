package com.example.doprava_bp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

    public Server(Cryptogram userCryptogram, Cryptogram receiverCryptogram, ObuParameters obuParameters) throws Exception{
        ServerSocket serverSocket = new ServerSocket(10002);
        System.out.println("Server is up and running on ip " + serverSocket.getInetAddress().getLocalHost().getHostAddress() + " port: " + 10002);
        Socket socket = serverSocket.accept();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());


        //Cryptogram userCryptogram = new Cryptogram();
        Random rnd = new Random();
        receiverCryptogram.setNonce(rnd.nextInt());
        receiverCryptogram.setIdr(obuParameters.getIdr());
        objectOutputStream.writeObject(receiverCryptogram);

        //Cryptogram recieverCryptogram = new Cryptogram();
        userCryptogram = (Cryptogram) objectInputStream.readObject();


        System.out.println("Nonce user: " + userCryptogram.getNonce());
        System.out.println("Nonce reciever: " + receiverCryptogram.getNonce());

        objectOutputStream.close();
        socket.close();
    }

}
