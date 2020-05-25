package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como una antorcha, si esta apagada y es golpeado con fuego se encendera
 * @author Roberto Ramiro Hernández
 *
 */

public class Fireplace extends BaseActor{

	Animation fire;
	boolean lit;
	
	public Fireplace(float x, float y, Stage stage) {
		super(x, y, stage);
		lit = false;
		loadTexture("assets/sprites/objects/fire000.png");
		setBoundaryRectangle();
		setScale(0.9f);
		String[] fileNames = {"assets/sprites/objects/fire001.png", "assets/sprites/objects/fire002.png", "assets/sprites/objects/fire003.png", "assets/sprites/objects/fire004.png"};
		fire = loadAnimationFromFiles(fileNames, 0.3f, true);
	}

	public void act(float delta) {
		super.act(delta);
		if(lit) {
			setAnimation(fire);
		}else {
			loadTexture("assets/sprites/objects/fire000.png");
		}
	}

	public boolean isLit() {
		return lit;
	}

	public void setLit(boolean lit) {
		this.lit = lit;
	}
	
	
	
}
