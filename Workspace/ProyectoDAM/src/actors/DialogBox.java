package actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import base.BaseActor;
import base.BaseGame;

/**
 * Clase que se encarga del aspecto visual de los cuadros de dialogo
 * @author Roberto Ramiro Hernández
 *
 */

public class DialogBox extends BaseActor {

	public Label dialogLabel;
	private float padding = 16;
	
	/**
	 * Se carga la textura y se centra la Label en el centro del cuadro
	 * @param x
	 * @param y
	 * @param stage
	 */
	public DialogBox(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/base/dialog-translucent.png");
		dialogLabel = new Label("", BaseGame.labelStyle);
		dialogLabel.setWrap(true);
		dialogLabel.setAlignment(Align.topLeft);
		dialogLabel.setPosition(padding, padding);
		this.setDialogSize(getWidth(), getHeight());
		this.addActor(dialogLabel);
	}

	/**
	 * Cambia el tamaño del cuadro de dialogo
	 * @param width
	 * @param height
	 */
	public void setDialogSize(float width, float height) {
		this.setSize(width, height);
		dialogLabel.setWidth(width - 2 * padding);
		dialogLabel.setHeight(height - 2 * padding);
	}
	
	/**
	 * Cambia el texto de dialogo
	 * @param text
	 */
	public void setText(String text) {
		dialogLabel.setText(text);
	}
	
	/**
	 * Cambia el tamaño de la letra
	 * @param scale
	 */
	public void setFontScale(float scale) {
		dialogLabel.setFontScale(scale);
	}
	
	/**
	 * Cambia el color de la letra
	 * @param color
	 */
	public void setFontColor(Color color) {
		dialogLabel.setColor(color);
	}
	
	/**
	 * Cambia el color del background
	 * @param color
	 */
	public void setBackgroundColor(Color color) {
		this.setColor(color);
	}
	
	/**
	 * Alinea el texto de manera centrada
	 */
	public void alignCenter() {
		dialogLabel.setAlignment(Align.center);
	}
	
	/**
	 * Alinea el texto arriba a la derecha
	 */
	public void alignTopLeft() {
		dialogLabel.setAlignment(Align.topLeft);
	}	
}
