package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como proyectil que el jugador puede usar en el ultimo nivel
 * @author Roberto Ramiro Hernández
 *
 */

public class Fire extends BaseActor{

	Animation ballAn;
	
	public Fire(float x, float y, Stage stage) {
		super(x, y, stage);
		
		String[] fileNames = {"assets/sprites/enemies/vaati/attacks/fireball/fireball001.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball002.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball003.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball004.png"};
			
		ballAn = loadAnimationFromFiles(fileNames, 0.3f, true);
		setAnimation(ballAn);
		setSize(14, 15);
		setScale(0.7f);
		setSpeed(70);
	}
	
	public void act(float delta) {
		super.act(delta);
		applyPhysics(delta);
	}
}