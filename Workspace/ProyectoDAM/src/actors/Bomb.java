package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que es usado como arma por el jugador, pasados tres segundos explotara
 * @author Roberto Ramiro Hernández
 *
 */

public class Bomb extends BaseActor{

	private float timeSeconds = 0f;
	private float period = 3f;
	
	public Bomb(float x, float y, Stage stage) {
		super(x, y, stage);
		
		String[] fileNames = {"assets/sprites/objects/bomb001.png", "assets/sprites/objects/bomb002.png"};
		
		loadAnimationFromFiles(fileNames, 0.35f, true);
		setSize(16, 16);
		setScale(0.8f);
		
		setBoundaryPolygon(8);
	}

	public void act(float delta) {
		super.act(delta);
		applyPhysics(delta);
	
		timeSeconds += Gdx.graphics.getRawDeltaTime();
	    if(timeSeconds > period){
	        timeSeconds-=period;
	        BombExp exp = new BombExp(0, 0, this.getStage());
	        exp.centerAtActor(this);
	        remove();
	    }
	    
	}
	
}
