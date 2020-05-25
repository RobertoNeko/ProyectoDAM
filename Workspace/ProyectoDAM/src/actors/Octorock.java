package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;


/**
 * Proyectir usado por el enemigo Octorok
 * @author Roberto Ramiro Hernández
 *
 */
public class Octorock extends BaseActor{

	Animation rock;
	
	public Octorock(float x, float y, Stage stage) {
		super(x, y, stage);
		String[] fileNames = {"assets/sprites/enemies/octorok/rock001.png", "assets/sprites/enemies/octorok/rock002.png", "assets/sprites/enemies/octorok/rock003.png"};
		rock = loadAnimationFromFiles(fileNames, 0.2f, true);
		setAnimation(rock);
		setSpeed(100);
	}

	public void act(float delta) {
		super.act(delta);
		applyPhysics(delta);
	}
}
