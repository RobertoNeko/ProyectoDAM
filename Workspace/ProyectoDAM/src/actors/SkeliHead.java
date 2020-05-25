package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como obstaculo, el jugador puede romperlo
 * @author Roberto Ramiro Hernández
 *
 */

public class SkeliHead extends Solid{

	public SkeliHead(float x, float y, Stage stage) {
		super(x, y, 12, 16, stage);
		loadTexture("assets/sprites/enemies/skeleton/skelihead.png");
		setBoundaryRectangle();
	}

}
