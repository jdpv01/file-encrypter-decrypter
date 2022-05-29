package model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AppImp implements App {

	private final String ENCRYPT_ALGORITHM = "AES/GCM/NoPadding";
    private final int TAG_LENGTH_BIT = 128; 
    private final int IV_LENGTH_BYTE = 12;
    private final int SALT_LENGTH_BYTE = 16;

	public byte[] encryptFile(String fileIn, String password) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(fileIn));
        byte[] encryptedContent = encrypt(fileContent, password);
        return encryptedContent;
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

	public byte[] decryptFile(String encryptedFile, String password) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(encryptedFile));
        return decrypt(fileContent, password);
    }

	private byte[] decrypt(byte[] fileContent, String password) throws Exception {
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
        return  sha256;
    }

    public String getSHA1(String decryptedFile) throws NoSuchAlgorithmException, IOException {
        String sha1 = CryptoUtils.computeSHA1(Files.readAllBytes(Paths.get(decryptedFile)));
        return  sha1;
    }

    public static void main(String[] args) throws Exception {
        String fileIn = "AESAVS.pdf";
        String fileOut = "AESAVS.pdf.enc";
        String decryptedFile = "decrypted"+fileOut.substring(0, fileOut.length()-4);
        String password = "123456";
        AppImp app = new AppImp();
        try {
            Path path = Paths.get(fileOut);
            Files.write(path, app.encryptFile(fileIn, password));   
            String sha256 = app.getSHA256(fileIn);
            //Files.write(Paths.get(fileOut), sha256.getBytes(), StandardOpenOption.APPEND);

            path = Paths.get(decryptedFile);
            Files.write(path, app.decryptFile(fileOut, password)); 
            String sha1 = app.getSHA1(decryptedFile);

            System.out.println(sha256);
            System.out.println(sha1);
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
}
