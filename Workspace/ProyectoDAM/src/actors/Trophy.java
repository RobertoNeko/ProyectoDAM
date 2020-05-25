package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Trofeo que se obtiene tras pasarse el juego
 * @author Roberto Ramiro Hernández
 *
 */

public class Trophy extends BaseActor{

	public Trophy(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/trophy.png");
		setBoundaryRectangle();
	}

}
