package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Actor que requiere el uso de una llave para ser abierto, cuando se habre aparecera una pocion (powerup)
 * @author Roberto Ramiro Hernández
 *
 */

public class Chest extends Solid{

	private GreenPotion potion;
	
	private boolean opened = false;
	@SuppressWarnings("rawtypes")
	private Animation openAnimation;
	
	public Chest(float x, float y, Stage stage) {
		super(x, y, 16, 16, stage);
		loadTexture("assets/sprites/objects/chest001.png");
		String[] fileNames = {"assets/sprites/objects/chest001.png", "assets/sprites/objects/chest002.png"};
		openAnimation = loadAnimationFromFiles(fileNames, 1f, false);
		setBoundaryRectangle();
		potion = new GreenPotion(this.getX(), this.getY(), this.getStage());
		potion.setVisible(false);
		
	}

	@SuppressWarnings("unchecked")
	public void act(float delta) {
		super.act(delta);
		if(opened == true) {
			setAnimation(openAnimation);
			potion.setVisible(true);
			potion.setScale(0.9f);
			addActor(potion);
			potion.centerAtPosition(getWidth()/2, getHeight()/2 + 10);
			potion.addAction(Actions.fadeOut(2.5f));
		}
	}
	
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
}
