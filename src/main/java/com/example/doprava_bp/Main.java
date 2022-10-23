package com.example.doprava_bp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Client client = new Client(); //přijmutí IDr a Kr od Identity Providera
        Server server = new Server(client.obuParameters); //přijmutí NU, odeslání IDr a NR
        server.sendAndReceiveObject(10003); //přijmutí IV, HATu a C1
        String userKey = hash(client.obuParameters.getDriverKey() + server.userCryptogram.getHatu(), "SHA-1");
        userKey= userKey.substring(0, 16);
        String ukeyString = hash(userKey + "user" + server.userCryptogram.getNonce() + server.receiverCryptogram.getNonce(),"SHA-1");
        ukeyString = ukeyString.substring(0,16);
        //Encryption
        String plainTextString = server.userCryptogram.getHatu() + client.obuParameters.getIdr() + server.userCryptogram.getNonce() + server.receiverCryptogram.getNonce();
        byte[] plainText = plainTextString.getBytes();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); // pořešit padding
        SecretKey ukey = new SecretKeySpec(ukeyString.getBytes(),"AES");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, server.userCryptogram.getIv());
        cipher.init(Cipher.ENCRYPT_MODE, ukey, parameterSpec);
        byte[] cipherText = cipher.doFinal(plainText);
        //AUTHENTICATE
        if(Arrays.equals(cipherText,server.userCryptogram.cryptograms.get(0))){
            server.userCryptogram.setAuthenticated(true);
        }
        else server.userCryptogram.setAuthenticated(false);
        server.sendAuthenticationMessage(server.userCryptogram.isAuthenticated());
        //C3
        server.sendAndReceiveObject(10005);
        cipher.init(Cipher.DECRYPT_MODE,ukey, parameterSpec);
        byte[] decryptedMessage = cipher.doFinal(server.userCryptogram.cryptograms.get(1)); //TODO: rozdělit na ATU a command
        byte[] ATU = Arrays.copyOfRange(decryptedMessage,0,32);
        String message = new String(Arrays.copyOfRange(decryptedMessage,32,decryptedMessage.length));
        System.out.println("Message: " + message);
        //Verify HATU and C4
        String newHatu = hash(new String(ATU),"SHA-256");
        newHatu = newHatu.substring(0,16);
        if(newHatu.equals(server.userCryptogram.getHatu())){
            System.out.println("ATU accepted");
        }

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
