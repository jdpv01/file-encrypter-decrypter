package model;

public interface App {
    public byte[] encryptFile(String fileIn, String password) throws Exception;
	public byte[] decryptFile(String encryptedFile, String password) throws Exception ;
}