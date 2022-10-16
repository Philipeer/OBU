package com.example.doprava_bp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    //private ObuParameters obuParameters;
    /*public static void main(String[] args) throws Exception {
        new Client(); // obtain key and idr from IdP

    }*/

    public Client(ObuParameters obuParameters) throws Exception{
        Socket socket = new Socket("192.168.56.1", 10001);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

        ObuParameters helloMessage = new ObuParameters("Hello from OBU!");
        objectOutputStream.writeObject(helloMessage);

        obuParameters = (ObuParameters) objectInputStream.readObject();

        System.out.println("Driver key: " + obuParameters.getDriverKey());
        System.out.println("IDr: " + obuParameters.getIdr());

        objectOutputStream.close();
        socket.close();
    }
}
