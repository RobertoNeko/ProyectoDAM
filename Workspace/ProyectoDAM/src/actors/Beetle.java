package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, es un enemigo pasivo, no hace caso al jugador
 * @author Roberto Ramiro Hernández
 *
 */
public class Beetle extends BaseActor{
	
	float posX;
	float posY;
	boolean stopped;
	
	float timeSecondsClock = 0f;
	float periodClock = 20f;
	
	public Beetle(float x, float y, Stage stage) {
		super(x, y, stage);
		
		loadAnimationFromSheet("assets/sprites/enemies/beetle/beetleSheet.png", 1, 4, 0.3f, true);

		posX = this.getX();
		posY = this.getY();
		
		setSize(16, 10);
		setBoundaryPolygon(6);
		setSpeed(MathUtils.random(20, 40));
		setMotionAngle(MathUtils.random(0, 360));
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
		    	setSpeed(MathUtils.random(20, 40));
		    }
			
		}else{
			if(MathUtils.random(1, 120) == 1) {
				setMotionAngle(MathUtils.random(0, 360));
			}
		}
		
		applyPhysics(delta);
		boundToWorld();
		boundToLimits(posX + 50, posY + 50);
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}
	
	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
