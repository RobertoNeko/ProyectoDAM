package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Arma principal del jugador
 * @author Roberto Ramiro Hernández
 *
 */

public class Sword extends BaseActor{

	public Sword(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/sword.png");
	}
}
