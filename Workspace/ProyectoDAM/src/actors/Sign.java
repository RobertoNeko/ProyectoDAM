package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como cartel, si el jugador se acerca podra leerlo
 * @author Roberto Ramiro Hernández
 *
 */
public class Sign extends BaseActor{

	private String text;
	private boolean viewing;
	
	public Sign(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/sign.png");
		setScale(0.8f);
		text = "";
		viewing = false;
	}

	public void setText(String t) {
		text = t;
	}
	
	public String getText() {
		return text;
	}
	
	public void setViewing(boolean v) {
		viewing = v;
	}
	
	public boolean isViewing() {
		return viewing;
	}
}
