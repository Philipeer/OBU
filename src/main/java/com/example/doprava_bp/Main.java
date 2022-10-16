package com.example.doprava_bp;

public class Main {
    public static void main(String[] args) throws Exception {
        ObuParameters obuParameters = new ObuParameters();
        new Client(obuParameters);
        Cryptogram userCryptogram = new Cryptogram();
        Cryptogram receiverCryptogram = new Cryptogram();
        Server server = new Server(userCryptogram,receiverCryptogram, obuParameters);

    }
}
