package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como efecto de muerte de un enemigo
 * @author Roberto Ramiro Hernández
 *
 */

public class EnemyExp extends BaseActor{

	public EnemyExp(float x, float y, Stage stage) {
		super(x, y, stage);
		String[] fileNames = {"assets/sprites/objects/explosion001.png", "assets/sprites/objects/explosion002.png", "assets/sprites/objects/explosion003.png", "assets/sprites/objects/explosion004.png", 
				"assets/sprites/objects/explosion005.png", "assets/sprites/objects/explosion006.png", "assets/sprites/objects/explosion007.png"};
		
		loadAnimationFromFiles(fileNames, 0.1f, true);
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
