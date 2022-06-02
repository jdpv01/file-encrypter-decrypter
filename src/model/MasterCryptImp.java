package model;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
//import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MasterCryptImp {

    private final String ENCRYPT_ALGORITHM = "AES/GCM/NoPadding";
    private final int TAG_LENGTH_BIT = 128;
    private final int IV_LENGTH_BYTE = 12;
    private final int SALT_LENGTH_BYTE = 16;

    private String lastOriginalFileEncrypted;

    public void encryptFile(File fileIn, String password) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(fileIn.getAbsolutePath()));
//        System.out.println(fileContent.length);
        byte[] encryptedContent = encrypt(fileContent, password);
        Files.write(Paths.get(fileIn.getAbsolutePath() + ".enc"), encryptedContent);
//        Files.write(Paths.get(fileIn.getAbsolutePath() + ".enc"), ("\n"+getSHA256(fileIn.getAbsolutePath())).getBytes(), StandardOpenOption.APPEND);
    }

    private byte[] encrypt(byte[] fileContent, String password) throws Exception {
        byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);
        byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt, TAG_LENGTH_BIT);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] cipherText = cipher.doFinal(fileContent);
        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv).put(salt).put(cipherText).array();
        return cipherTextWithIvSalt;
    }

    public void decryptFile(File encryptedFile, String password)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	
        byte[] fileContent = Files.readAllBytes(Paths.get(encryptedFile.getAbsolutePath()));
//        System.out.println(fileContent.length);
//        fileContent = Arrays.copyOf(fileContent, fileContent.length-109);
//        System.out.println(fileContent.length);
        byte[] decrypt = decrypt(fileContent, password);
        Files.write(Paths.get(encryptedFile.getAbsolutePath().substring(0, encryptedFile.getAbsolutePath().length() - 
        		encryptedFile.getName().length()) + "Decrypted"+ encryptedFile.getName().substring(0, encryptedFile.getName().length()-4))
        		, decrypt);
    }

    private byte[] decrypt(byte[] fileContent, String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);
        byte[] salt = new byte[SALT_LENGTH_BYTE];
        byteBuffer.get(salt);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt, TAG_LENGTH_BIT);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] plainText = cipher.doFinal(cipherText);
        return plainText;
    }

    public String getSHA256(String fileIn) throws NoSuchAlgorithmException, IOException {
        String sha256 = CryptoUtils.computeSHA256(Files.readAllBytes(Paths.get(fileIn)));
        return sha256;
    }

    public void startEncrypting(File fileToCrypt, String password) throws Exception {
        lastOriginalFileEncrypted = fileToCrypt.getAbsolutePath();
        encryptFile(fileToCrypt, password);
    }

    public String startDecrypt(File fileToDecrypt, String password) throws Exception {
        decryptFile(fileToDecrypt, password);

        String resultMsg = "Archivo Original:\n" + getSHA256(lastOriginalFileEncrypted) + "\n"
                + "Archivo Desencriptado:\n" + getSHA256(
                        fileToDecrypt.getAbsolutePath().substring(0, fileToDecrypt.getAbsolutePath().length() - 4));

        return resultMsg;
    }

}