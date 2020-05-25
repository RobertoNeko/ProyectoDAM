package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como una planta, si el jugador usa la espada podra cortarla 
 * @author Roberto Ramiro Hernández
 *
 */

public class Plant extends BaseActor{

	boolean cut;
	Animation getcut;
	
	public Plant(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/plant001.png");
		setScale(0.8f);
		setBoundaryRectangle();
		cut = false;
		
		String[] fileNames = {"assets/sprites/objects/plant001.png", "assets/sprites/objects/plant002.png"};
		getcut = loadAnimationFromFiles(fileNames, 1f, false); 
		
	}
	
	public void act(float delta) {
		super.act(delta);
		if(cut) {
			setAnimation(getcut);
		}
	}

	public boolean isCut() {
		return cut;
	}

	public void setCut(boolean cut) {
		this.cut = cut;
	}
	
	
	
}
