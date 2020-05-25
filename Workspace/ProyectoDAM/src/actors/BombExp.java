package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como efecto de una explosion, si hay un enemigo cerca morira.
 * @author Roberto Ramiro Hernandez
 *
 */

public class BombExp extends BaseActor{
	
	Sound bombExplosionSound;
	
	public BombExp(float x, float y, Stage stage) {
		super(x, y, stage);
		String[] fileNames = {"assets/sprites/objects/bombExp001.png", "assets/sprites/objects/bombExp002.png", "assets/sprites/objects/bombExp003.png", "assets/sprites/objects/bombExp004.png", 
				"assets/sprites/objects/bombExp005.png", "assets/sprites/objects/bombExp006.png", "assets/sprites/objects/bombExp007.png", 
				"assets/sprites/objects/bombExp008.png", "assets/sprites/objects/bombExp009.png", "assets/sprites/objects/bombExp010.png", "assets/sprites/objects/bombExp011.png"};
		bombExplosionSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Bomb_Blow.wav"));
		bombExplosionSound.play();
		loadAnimationFromFiles(fileNames, 0.1f, false);
		setSize(24, 24);
		setBoundaryPolygon(8);
	}

	public void act(float delta) {
		super.act(delta);
		if(isAnimationFinished()) {
			remove();
		}
	}
}
