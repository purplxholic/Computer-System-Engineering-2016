import javax.xml.bind.DatatypeConverter;
import javax.crypto.Cipher;
import java.io.*;
import java.security.*;


public class DigitalSignatureSolution {

    public static void main(String[] args) throws Exception {
   //Read the text file and save to String data
    String data = "";
    String line;
        File f = new File("C:\\Users\\The Gt Zan\\Documents\\Term 5\\50.005 CSE\\NS lab 2\\TextFiles\\smallFile.txt");
    BufferedReader bufferedReader = new BufferedReader( new FileReader(args[0]));
    while((line= bufferedReader.readLine())!=null){
        data = data +"\n" + line;
    }


//TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(1024);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    Key publicKey = keyPair.getPublic();
    Key privateKey = keyPair.getPrivate();

//TODO: Calculate message digest, using MD5 hash function
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] textinBytes = data.getBytes();
    messageDigest.update(textinBytes);
    byte[] digest = messageDigest.digest(textinBytes);
//TODO: print the length of output digest byte[], compare the length of file smallSize.txt and largeSize.txt
        System.out.println("Size: " + digest.length);

//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as encrypt mode, use PRIVATE key.
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipher.init(Cipher.ENCRYPT_MODE,privateKey);

//TODO: encrypt digest message
    byte[] cipherRSAed= rsaCipher.doFinal(digest);

//TODO: print the encrypted message (in base64format String using DatatypeConverter)
        String convertedCipher = DatatypeConverter.printBase64Binary(cipherRSAed);
        System.out.println("encrypted cipher: " + convertedCipher);
        System.out.println("signed message digest size: " + cipherRSAed.length);
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.
    Cipher rsaCipherDE = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipherDE.init(Cipher.DECRYPT_MODE,publicKey);
//TODO: decrypt message
    byte[] cipherRSAde= rsaCipherDE.doFinal(cipherRSAed);

//TODO: print the decrypted message (in base64format String using DatatypeConverter), compare with origin digest
    String convertedCipherDE = DatatypeConverter.printBase64Binary(cipherRSAde);
        System.out.println("decrypted cipher: " + convertedCipherDE);


    }

}
