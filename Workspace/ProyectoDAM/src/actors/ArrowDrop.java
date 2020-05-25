package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como drop y que cuando el jugador pase sobre el se añadiran flechas 
 * @author Roberto Ramiro Hernández
 *
 */

public class ArrowDrop extends BaseActor{

	public ArrowDrop(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/arrow.png");
		setSize(15, 5);
		setBoundaryPolygon(4);
	}
}
