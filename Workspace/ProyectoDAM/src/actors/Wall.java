package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como objeto solido 
 * @author Roberto Ramiro Hernández
 *
 */

public class Wall extends BaseActor{
	public Wall(float x, float y, float width, float height, Stage stage) {
		super(x, y, stage);
		setSize(width, height);
		setBoundaryRectangle();
	}
}
