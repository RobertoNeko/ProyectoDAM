package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


/**
 * Clase principal del programa, se encarga de iniciar el juego y de asignar los valores de la ventana.
 * @author Roberto Ramiro Hernández
 */

public class Launcher extends Thread{

	public static void main(String[] args) {
		Game game = new DefinitelyNotZelda();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "DefinitelyNotZelda";
        config.width = 900;
        config.height = 650;
        config.x = 300;
        config.y = 50;
        config.vSyncEnabled = true;
		new LwjglApplication(game, config);	
	}
}
