package model;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface MasterCrypt {

    public byte[] encryptFile(String fileIn, String password) throws Exception;

    public byte[] decryptFile(String encryptedFile, String password) throws Exception;

    public String getSHA256(String fileIn) throws NoSuchAlgorithmException, IOException;

    public String getSHA1(String decryptedFile) throws NoSuchAlgorithmException, IOException;

    public String init(File fileToCrypt, String password);
}