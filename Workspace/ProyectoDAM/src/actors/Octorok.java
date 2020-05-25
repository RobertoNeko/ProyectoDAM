package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, cuando el jugador este en su rango, este le lanzara proyectiles
 * @author Roberto Ramiro Hernández
 *
 */

public class Octorok extends BaseActor{

	boolean aggro;
	boolean stopped;
	
	float facingAngle;
	float timeSecondsClock = 0f;
	float periodClock = 20f;
	
	Animation iddleSouth;
	Animation iddleNorth;
	Animation iddleWest;
	Animation iddleEast;
	Animation atkNorth;
	Animation atkEast;
	Animation atkSouth;
	Animation atkWest;
	
	public Octorok(float x, float y, Stage stage) {
		super(x, y, stage);
		aggro = false;
		
		String[] fileNames1 = {"assets/sprites/enemies/octorok/iddleNorth001.png", "assets/sprites/enemies/octorok/iddleNorth002.png"};
		iddleNorth = loadAnimationFromFiles(fileNames1, 0.5f, true);
		
		String[] fileNames2 = {"assets/sprites/enemies/octorok/iddleEast001.png", "assets/sprites/enemies/octorok/iddleEast002.png"};
		iddleEast = loadAnimationFromFiles(fileNames2, 0.5f, true);
		
		String[] fileNames3 = {"assets/sprites/enemies/octorok/iddleSouth001.png", "assets/sprites/enemies/octorok/iddleSouth001.png"};
		iddleSouth = loadAnimationFromFiles(fileNames3, 0.5f, true);
		
		String[] fileNames4 = {"assets/sprites/enemies/octorok/iddleWest001.png", "assets/sprites/enemies/octorok/iddleWest002.png"};
		iddleWest = loadAnimationFromFiles(fileNames4, 0.5f, true);
		
		String[] fileNames5 = {"assets/sprites/enemies/octorok/atkNorth001.png", "assets/sprites/enemies/octorok/atkNorth002.png"};
		atkNorth = loadAnimationFromFiles(fileNames5, 0.5f, true);
		
		String[] fileNames6 = {"assets/sprites/enemies/octorok/atkEast001.png", "assets/sprites/enemies/octorok/atkEast002.png"};
		atkEast = loadAnimationFromFiles(fileNames6, 0.5f, true);
		
		String[] fileNames7 = {"assets/sprites/enemies/octorok/atkSouth001.png", "assets/sprites/enemies/octorok/atkSouth002.png"};
		atkSouth = loadAnimationFromFiles(fileNames7, 0.5f, true);
		
		String[] fileNames8 = {"assets/sprites/enemies/octorok/atkWest001.png", "assets/sprites/enemies/octorok/atkWest002.png"};
		atkWest = loadAnimationFromFiles(fileNames8, 0.5f, true);
		
		facingAngle = 270;
		setSpeed(20);
		setBoundaryPolygon(8);
		setScale(0.8f);
	}

	public void act(float delta) {
		super.act(delta);
		float angle = getMotionAngle();
		
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
				setSpeed(5);
				if(angle >= 45 && angle <= 135) {	
					facingAngle = 90;
					setAnimation(atkNorth);
				}else if(angle >= 135 && angle < 225) {	
					facingAngle = 180;
					setAnimation(atkWest);
				}else if(angle >= 225 && angle <= 315) {
					facingAngle = 270;
					setAnimation(atkSouth);
				}else {
					facingAngle = 0;
					setAnimation(atkEast);
				}		
			}else {
				setSpeed(20);
				if(angle >= 45 && angle <= 135) {	
					facingAngle = 90;
					setAnimation(iddleNorth);
				}else if(angle >= 135 && angle < 225) {	
					facingAngle = 180;
					setAnimation(iddleWest);
				}else if(angle >= 225 && angle <= 315) {
					facingAngle = 270;
					setAnimation(iddleSouth);
				}else {
					facingAngle = 0;
					setAnimation(iddleEast);
				}		
			}
		}
		
		
		if(MathUtils.random(1, 120) == 1) {
			setMotionAngle(MathUtils.random(0, 360));
			
		}
		applyPhysics(delta);
		boundToWorld();
	}
	
	public void throwRock(float angle) {
		Octorock rock = new Octorock(0, 0, this.getStage());
		rock.centerAtActor(this);
		rock.setMotionAngle(angle);
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
