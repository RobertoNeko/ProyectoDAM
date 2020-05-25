package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, cuando el jugador se encuentre cerca se teletransportara a otro punto cercano.
 * @author Roberto Ramiro Hernández
 *
 */

public class Mole extends BaseActor{

	Animation popup;
	Animation burrow;
	
	boolean aggro = false;
	boolean sur = false;
	boolean stopped;
	
	float timeSecondsWarp = 0f;
	float periodWarp = 1f;
	float timeSecondsClock = 0f;
	float periodClock = 20f;
	
	public Mole(float x, float y, Stage stage) {
		super(x, y, stage);
		
		
		String[] fileNames1 = {"assets/sprites/enemies/mole/pop001.png", "assets/sprites/enemies/mole/pop002.png", "assets/sprites/enemies/mole/pop003.png", "assets/sprites/enemies/mole/pop004.png", "assets/sprites/enemies/mole/pop005.png", "assets/sprites/enemies/mole/pop006.png", "assets/sprites/enemies/mole/pop007.png"};
		String[] fileNames2 = {"assets/sprites/enemies/mole/bur001.png", "assets/sprites/enemies/mole/bur002.png", "assets/sprites/enemies/mole/bur003.png", "assets/sprites/enemies/mole/bur004.png", "assets/sprites/enemies/mole/bur005.png", "assets/sprites/enemies/mole/bur006.png", "assets/sprites/enemies/mole/bur007.png"};
		
		loadTexture("assets/sprites/enemies/mole/pop004.png");
		
		popup = loadAnimationFromFiles(fileNames1, 0.2f, false);
		burrow = loadAnimationFromFiles(fileNames2, 0.2f, false);
		
		setSpeed(0);
		setScale(0.8f);
		setBoundaryPolygon(8);
	}

	public void act(float delta) {
		super.act(delta);
		
		if(stopped) {
			setAnimationPaused(true);
			setSpeed(0);
			timeSecondsClock += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsClock > periodClock){
		    	timeSecondsClock -= periodClock;
		    	stopped = false;
		    	setAnimationPaused(false);
		    }
		}else{
			if(aggro) {
				setAnimation(burrow);
				warp();
			}
			if(sur) {
				setAnimation(popup);
			}
		}
		
		applyPhysics(delta);
		boundToWorld();
	}
	
	public void warp() { //TODO
		timeSecondsWarp += Gdx.graphics.getRawDeltaTime();
	    if(timeSecondsWarp > periodWarp){
	    	timeSecondsWarp -= periodWarp; 
	    	setPosition(MathUtils.random(900), MathUtils.random(650));
	    	aggro = false;
	    	setAnimation(popup);
	    }
	}

	public boolean isAggro() {
		return aggro;
	}

	public void setAggro(boolean aggro) {
		this.aggro = aggro;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	
}
