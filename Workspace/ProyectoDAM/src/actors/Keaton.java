package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, cuando el jugador esta en su rango cargara hacia el.
 * @author Roberto Ramiro Hernández
 *
 */

public class Keaton extends BaseActor{

	public int health = 20;
	float posX;
	float posY;
	float facingAngle;
	float timerHit = 0f;
	float periodHit = 1f;
	float timeSecondsClock = 0f;
	float periodClock = 20f;
	
	boolean hit;
	boolean stopped;
	
	Animation north;
	Animation south;
	Animation west;
	Animation east;
	Animation iddleAnimationDown;
	Animation iddleAnimationUp;
	Animation iddleAnimationRight;
	Animation iddleAnimationLeft;
	
	public Keaton(float x, float y, Stage stage) {
		super(x, y, stage);
		
		posX = this.getX();
		posY = this.getY();
		hit = false;
		
		north = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonUp.png", 1, 4, 0.3f, true);
		south = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonDown.png", 1, 4, 0.3f, true);
		west = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonLeft.png", 1, 4, 0.3f, true);
		east = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonRight.png", 1, 4, 0.3f, true);

		iddleAnimationDown = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonIddleDown.png", 1, 9, 0.3f, true);
		iddleAnimationUp = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonIddleUp.png", 1, 9, 0.3f, true);
		iddleAnimationRight = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonIddleRight.png", 1, 9, 0.3f, true);
		iddleAnimationLeft = loadAnimationFromSheet("assets/sprites/enemies/keaton/keatonIddleLeft.png", 1, 9, 0.3f, true);
		setBoundaryPolygon(8);
		setAnimation(south);
		facingAngle = 270;
		setSpeed(10);
		
	}
	
	public void act(float delta) {
		super.act(delta);
		
		if(stopped) {
			setSpeed(0);
			setAnimationPaused(true);
			timeSecondsClock += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsClock > periodClock){
		    	timeSecondsClock -= periodClock;
		    	stopped = false;
		    	setAnimationPaused(false);
		    }
		}else {
			if(hit) {
				setSpeed(0.001f);
				
				float angle = getMotionAngle();
				
				if(angle >= 45 && angle <= 135) {	
					facingAngle = 90;
					setAnimation(iddleAnimationUp);
				}else if(angle >= 135 && angle < 225) {	
					facingAngle = 180;
					setAnimation(iddleAnimationLeft);
				}else if(angle >= 225 && angle <= 315) {
					facingAngle = 270;
					setAnimation(iddleAnimationDown);
				}else {
					facingAngle = 0;
					setAnimation(iddleAnimationRight);	
				}
				timerHit += Gdx.graphics.getRawDeltaTime();
			    if(timerHit > periodHit){
			    	timerHit -= periodHit;
			    	hit = false;
			    }
			}else {

				float angle = getMotionAngle();
				
				if(angle >= 45 && angle <= 135) {
					facingAngle = 90;
					setAnimation(north);
				}else if(angle >= 135 && angle < 225) {
					facingAngle = 180;		
					setAnimation(west);
				}else if(angle >= 225 && angle <= 315) {
					facingAngle = 270;
					setAnimation(south);
				}else {
					facingAngle = 0;
					setAnimation(east);	
				}
			}
			
			if(MathUtils.random(1, 120) == 1) {
				setMotionAngle(MathUtils.random(0, 360));
			}
		}
		
		
		
		boundToWorld();
		applyPhysics(delta);
	}
	
	public float getFacingAngle() {
		return facingAngle;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
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

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	
}
