package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Ataque que usara el final boss, se hace mas grande con el tiempo.
 * @author Roberto Ramiro Hernández
 *
 */

public class Tornado extends BaseActor{

	Animation spawn;
	Animation normal;
	
	boolean hurt;
	
	public Tornado(float x, float y, Stage stage) {
		super(x, y, stage);
		hurt = false;
		
		String[] fileNames1 = {"assets/sprites/enemies/vaati/attacks/tornado/tornado001.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado002.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado003.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado004.png"};
		String[] fileNames2 = {"assets/sprites/enemies/vaati/attacks/tornado/tornado005.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado006.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado007.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado008.png",
				"assets/sprites/enemies/vaati/attacks/tornado/tornado009.png" ,"assets/sprites/enemies/vaati/attacks/tornado/tornado010.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado011.png", "assets/sprites/enemies/vaati/attacks/tornado/tornado012.png"};
		
		spawn = loadAnimationFromFiles(fileNames1, 0.5f, false);
		normal = loadAnimationFromFiles(fileNames2, 0.2f, true);
		setSpeed(0);
		setAnimation(spawn);
	}

	public void act(float delta) {
		super.act(delta);
		applyPhysics(delta);
		if(isAnimationFinished()) {
			setAnimation(normal);
			setSpeed(30);
			hurt = true;
		}
	}

	public boolean isHurt() {
		return hurt;
	}

	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}
	
	
}
