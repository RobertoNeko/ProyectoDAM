package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como drop para se recogido por el jugador
 * @author Roberto Ramiro Hernández
 *
 */

public class BombDrop extends BaseActor{

	public BombDrop(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/bomb001.png");
		setScale(0.8f);
		setBoundaryPolygon(8);
	}
}
