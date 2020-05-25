package online;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author Roberto Ramiro Hernández
 * Se encarga de conectar con el servidor y de enviar los datos.
 */

public class Cliente {
	
	private static Key KEY = new SecretKeySpec("ESTA ES UNA CLAVE MUY SEGURA".getBytes(), 0, 16, "AES");
	private Encrypt encrypt; 
	public Socket socket;
    private DataOutputStream salida;

	/**
	 * Contructor de la clase
	 */
	public Cliente() {
		encrypt = new Encrypt();
		socket = null;
		salida = null;
	}
	
	/**
	 * Conecta con el servidor 
	 * @param host Direccion del servidor
	 * @param puerto Puerto del servidor
	 */
    public void conectar(String host, int puerto) {
		try {
			socket = new Socket(host, puerto);
			salida = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			socket = null;
			salida = null;
			
		}
    }
    
    /**
     * Recibe y encripta los datos y lo envia al servidor
     * @param name nombre de usuario
     * @param pass contrasena del usuario
     * @param score puntuacion del usuario
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public void enviarData(String name, String pass, int score) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
    	
    	 String player = name + "-" + pass + "-" + score;
         byte[] playerEnc = encrypt.encriptar(player, KEY);

         salida.writeInt(playerEnc.length);

         for(int i = 0; i < playerEnc.length; i++){
             salida.writeByte(playerEnc[i]);
         }
    }

    public void desconectar() throws IOException {
    	socket.close();
    }
    
    /**
     * Getter del Socket
     * @return
     */
	public Socket getSocket() {
		return socket;
	}

}
