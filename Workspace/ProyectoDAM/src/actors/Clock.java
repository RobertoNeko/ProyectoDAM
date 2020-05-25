package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que cuando es llamado se encargara de parar el tiempo de los enemigos
 * @author Roberto Ramiro Hernández
 *
 */
public class Clock extends BaseActor{

	public Clock(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/clock.png");
		setScale(0.8f);
		setBoundaryRectangle();
	}
}
