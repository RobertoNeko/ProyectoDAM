package online;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author Roberto Ramiro Hernández
 * Clase que se encarga de la encriptacion
 */

public class Encrypt {

	/**
	 * Metodo que genera una clave aleatoria con el algoritmo AES
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
    public Key generarClave() throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();

        return key;
    }

    /**
     * Metodo que recibe un texto que se quiere encriptar y una clave, se devolvera un array de bytes
     * @param texto Texto a encriptar
     * @param key Clave para encriptar
     * @return bytes[]
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public byte[] encriptar(String texto, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, key);
        byte[] encriptado = aes.doFinal(texto.getBytes());

        return encriptado;
    }

    
    /**
     * Metodo que se encarga de desencriptar un texto.
     * @param textoEncriptado Texto que se desea desencriptar
     * @param key Clave necesaria para desencriptar el texto
     * @return String
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String desencriptar(byte[] textoEncriptado, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

        aes.init(Cipher.DECRYPT_MODE, key);

        byte[] desencriptado = aes.doFinal(textoEncriptado);

        return new String(desencriptado);
    }
}
