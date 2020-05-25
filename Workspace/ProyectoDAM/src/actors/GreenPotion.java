package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como power up, cuando el jugador lo recoga le aumentara la velocidad de movimiento durante un tiempo
 * @author Roberto Ramiro Hernández
 *
 */
public class GreenPotion extends BaseActor{

	public GreenPotion(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/greenPot.png");
	}

}
