package actors;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import base.BaseActor;

/**
 * Actor que actua como proyectir del enemigo Skeleton, si hace contacto con el jugador, este recibira daño
 * @author Roberto Ramiro Hernandez
 *
 */

public class Bone extends BaseActor{

	public Bone(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/enemies/skeleton/bone.png");
		setSpeed(75);
		Action rotate = Actions.rotateBy(130, 2);
		Action rotateForever = Actions.forever(rotate);
		addAction(rotateForever);
	}
	
	public void act(float delta) {
		super.act(delta);
		applyPhysics(delta);
	}
	
}
