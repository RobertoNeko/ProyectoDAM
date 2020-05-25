package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, se quedara quieto hasta que el jugador este en su rango, entonces ira a por el.
 * @author Roberto Ramiro Hernández
 *
 */

public class Bat extends BaseActor{

	boolean aggro;
	boolean stopped;
	Animation atk;
	float facingAngle;
	float timeSecondsClock = 0f;
	float periodClock = 20f;
	
	public Bat(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/enemies/bat/iddle.png");		
		setSize(20, 14);
		setScale(0.8f);
		setBoundaryPolygon(6);
		setSpeed(0);
		facingAngle = 270;
		String[] fileNames = {"assets/sprites/enemies/bat/aggro001.png", "assets/sprites/enemies/bat/aggro002.png", "assets/sprites/enemies/bat/aggro003.png", "assets/sprites/enemies/bat/aggro004.png", "assets/sprites/enemies/bat/aggro005.png"};
		atk = loadAnimationFromFiles(fileNames, 0.2f, true);
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
				float angle = getMotionAngle();
				
				if(angle >= 45 && angle <= 135) {	
					facingAngle = 90;
				}else if(angle >= 135 && angle < 225) {	
					facingAngle = 180;
				}else if(angle >= 225 && angle <= 315) {
					facingAngle = 270;
				}else {
					facingAngle = 0;
				}
				setAnimation(atk);
				setSpeed(50);
			}
		}
		
		applyPhysics(delta);
		boundToWorld();
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
