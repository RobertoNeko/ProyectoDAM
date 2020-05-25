package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Sirve como lugar donde haran spawn ciertos enemigos
 * @author Roberto Ramiro Hernández
 *
 */
public class Spawn extends BaseActor{

	public Spawn(float x, float y, Stage stage) {
		super(x, y, stage);
		setSize(16, 16);
		setVisible(false);
	}
}
