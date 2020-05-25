package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Efecto que se pondra alrededor del jugador y que indica que tiene un power up
 * @author Roberto Ramiro Hernández
 *
 */

public class Sparkle extends BaseActor{
	
	public Sparkle(float x, float y, Stage stage) {
		super(x, y, stage);
		String[] fileNames = {"assets/sprites/objects/sparkle001.png", "assets/sprites/objects/sparkle002.png", "assets/sprites/objects/sparkle003.png", 
				"assets/sprites/objects/sparkle004.png", "assets/sprites/objects/sparkle005.png" };
		loadAnimationFromFiles(fileNames, 0.2f, true);
	}
}
