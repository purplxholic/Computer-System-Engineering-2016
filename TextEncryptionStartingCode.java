import sun.security.krb5.internal.crypto.Des;

import java.io.*;
import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;


public class TextEncryptionStartingCode {
    public static void main(String[] args) throws Exception {
        String data = "";
        String line;
        File f = new File("C:\\Users\\The Gt Zan\\Documents\\Term 5\\50.005 CSE\\NS lab 2\\TextFiles\\largeFile.txt");
        BufferedReader bufferedReader = new BufferedReader( new FileReader(args[0])); //args[0] is the file you are going to encrypt.
        while((line= bufferedReader.readLine())!=null){
            data = data +"\n" + line;
        }

//TODO: Print to screen contents of the file
        System.out.println(data);
//TODO: generate secret key using DES algorithm
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        SecretKey secretKey = keyGenerator.generateKey();
//TODO: create cipher object, initialize the ciphers with the given key, choose encryption mode as DES
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        int ENmode = Cipher.ENCRYPT_MODE;
        int DEmode = Cipher.DECRYPT_MODE;

//TODO: do encryption, by calling method Cipher.doFinal().
        desCipher.init(ENmode,secretKey);
        //convert string to bytearray
        byte[] textinbyte = data.getBytes();
        byte[] cipherBytes = desCipher.doFinal(textinbyte);
//TODO: print the length of output encrypted byte[], compare the length of file smallSize.txt and largeSize.txt
        System.out.println("cipher length: " + cipherBytes.length);
        System.out.println(new String(cipherBytes));
//TODO: do format conversion. Turn the encrypted byte[] format into base64format String using DatatypeConverter
        String convertedCipher = DatatypeConverter.printBase64Binary(cipherBytes);
//TODO: print the encrypted message (in base64format String format)
        System.out.println(convertedCipher);
//TODO: create cipher object, initialize the ciphers with the given key, choose decryption mode as DES
        Cipher desCipher2 = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher2.init(DEmode,secretKey);
//TODO: do decryption, by calling method Cipher.doFinal().
        byte[] decipherByte = desCipher2.doFinal(cipherBytes);
//TODO: do format conversion. Convert the decrypted byte[] to String, using "String a = new String(byte_array);"
        String a = new String(decipherByte);
//TODO: print the decrypted String text and compare it with original text
        System.out.println(a);
    }
}
