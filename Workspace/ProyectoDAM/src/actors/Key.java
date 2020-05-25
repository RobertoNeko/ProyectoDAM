package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que se encuentra en la tienda, cuando el jugador la compre podra abrir el cofre
 * @author Roberto Ramiro Hernández
 *
 */
public class Key extends BaseActor{

	public Key(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/key.png");
		setScale(0.8f);
		setBoundaryPolygon(8);
	}

}
