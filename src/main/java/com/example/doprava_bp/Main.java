package com.example.doprava_bp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws Exception {
        ObuParameters obuParameters = new ObuParameters();
        new Client(obuParameters); //přijmutí IDr a Kr od Identity Providera
        Cryptogram userCryptogram = new Cryptogram();
        Cryptogram receiverCryptogram = new Cryptogram();
        Server server = new Server(userCryptogram,receiverCryptogram, obuParameters); //přijmutí NU, odeslání IDr a NR
        server.sendAndReceiveObject(userCryptogram); //přijmutí IV, HATu a C1
        String userKey = hash(obuParameters.getDriverKey() + receiverCryptogram.getHatu(), "SHA-256");
        userKey= userKey.substring(0, 16);
        //TODO: ukey
        //TODO: Encryption a autentizace

    }

    public static String hash(String input, String hashType)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance(hashType);

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            /*while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }*///

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
