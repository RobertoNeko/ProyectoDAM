package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como meta final, si el jugador hace contacto con el habra terminado el nivel
 * @author Roberto Ramiro Hernández
 *
 */

public class End extends BaseActor{

	public End(float x, float y, Stage stage) {
		super(x, y, stage);
		setSize(16, 16);
		setBoundaryRectangle();
		setVisible(false);
	}
}
