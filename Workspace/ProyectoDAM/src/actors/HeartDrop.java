package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como drop de vida
 * @author Roberto Ramiro Hernández
 *
 */

public class HeartDrop extends BaseActor{

	public HeartDrop(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/heart.png");
		setSize(16, 16);
		setScale(0.7f);
		setBoundaryPolygon(8);
	}

}
