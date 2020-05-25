package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actuara como arma a distancia del jugador
 * @author Roberto Ramiro Hernandez
 *
 */

public class Arrow extends BaseActor{

	public Arrow(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/arrow.png");
		setSpeed(300);
	}

	public void act(float delta) {
		super.act(delta);
		applyPhysics(delta);
	}
	
}
