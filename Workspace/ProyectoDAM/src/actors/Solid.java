package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que sirve para establecer que algo sea solido
 * @author Roberto Ramiro Hernández
 *
 */

public class Solid extends BaseActor{

	public Solid(float x, float y, float width, float height, Stage stage) {
		super(x, y, stage);
		setSize(width, height);
		setBoundaryRectangle();
	}
}
