package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que el jugador controla.
 * @author Roberto Ramiro Hernández
 *
 */
public class Player extends BaseActor{

	Animation north;
	Animation south;
	Animation east;
	Animation west;
	Animation deathAnimation;
	
	float facingAngle;
	boolean dead;
	boolean sparkle;
	private Sparkle sparkleEffect;
	
	public Player(float x, float y, Stage stage) {
		super(x, y, stage);
		dead = false;
		sparkle = false;
		
		sparkleEffect = new Sparkle(this.getX(), this.getY(), this.getStage());
		sparkleEffect.setVisible(false);
		
		String[] filesNorth = {"assets/sprites/link/north001.png", "assets/sprites/link/north002.png", "assets/sprites/link/north003.png", "assets/sprites/link/north004.png", 
				"assets/sprites/link/north005.png", "assets/sprites/link/north006.png", "assets/sprites/link/north007.png", "assets/sprites/link/north008.png",
				"assets/sprites/link/north009.png", "assets/sprites/link/north010.png"};
		
		String[] filesSouth = {"assets/sprites/link/south001.png", "assets/sprites/link/south002.png", "assets/sprites/link/south003.png", "assets/sprites/link/south004.png", 
				"assets/sprites/link/south005.png", "assets/sprites/link/south006.png", "assets/sprites/link/south007.png", "assets/sprites/link/south008.png", 
				"assets/sprites/link/south009.png", "assets/sprites/link/south010.png", "assets/sprites/link/south011.png"};
		
		String [] filesWest = {"assets/sprites/link/west001.png", "assets/sprites/link/west002.png", "assets/sprites/link/west003.png", "assets/sprites/link/west004.png", 
				"assets/sprites/link/west005.png", "assets/sprites/link/west006.png", "assets/sprites/link/west007.png", "assets/sprites/link/west008.png", 
				"assets/sprites/link/west009.png", "assets/sprites/link/west010.png", };
		
		String[] filesEast = {"assets/sprites/link/east001.png", "assets/sprites/link/east002.png", "assets/sprites/link/east003.png", "assets/sprites/link/east004.png", 
				"assets/sprites/link/east005.png", "assets/sprites/link/east006.png", "assets/sprites/link/east007.png", "assets/sprites/link/east008.png", 
				"assets/sprites/link/east009.png", "assets/sprites/link/east010.png", };
		
		String[] filesDeath = {"assets/sprites/link/death001.png", "assets/sprites/link/death002.png", "assets/sprites/link/death003.png", "assets/sprites/link/death004.png",
				"assets/sprites/link/death005.png", "assets/sprites/link/death006.png", "assets/sprites/link/death007.png", "assets/sprites/link/death008.png",
				"assets/sprites/link/death009.png", "assets/sprites/link/death010.png", "assets/sprites/link/death011.png", "assets/sprites/link/death012.png",};
		
		deathAnimation = loadAnimationFromFiles(filesDeath, 0.18f, false);
		
		north = loadAnimationFromFiles(filesNorth, 0.1f, true);
		south = loadAnimationFromFiles(filesSouth, 0.1f, true);
		west = loadAnimationFromFiles(filesWest, 0.1f, true);
		east = loadAnimationFromFiles(filesEast, 0.1f, true);
		
		setAnimation(south);
		facingAngle = 270;
		
		setScale(0.8f);
		setBoundaryPolygon(8);
		setAcceleration(200);
		setMaxSpeed(70);
		setDeceleration(800);
	}
	
	public void act(float delta) {
		super.act(delta);
		
		float angle = getMotionAngle();
		
		if(sparkle) {
			sparkleEffect.setVisible(true);
			toFront();
			addActor(sparkleEffect);
			sparkleEffect.centerAtPosition(getWidth()/2, getHeight()/2);
			setMaxSpeed(90);
		}else {
			sparkleEffect.setVisible(false);
			setMaxSpeed(70);
		}
		
		if(getSpeed() == 0) {
			setAnimationPaused(true);
		}else {
			setAnimationPaused(false);
			if(dead) {
				setAnimation(deathAnimation);
			}else {
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
		}
		
		alignCamera();
		boundToWorld();
		applyPhysics(delta);
	}

	public float getFacingAngle() {
		return facingAngle;
	}

	public void setFacingAngle(float facingAngle) {
		this.facingAngle = facingAngle;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isSparkle() {
		return sparkle;
	}

	public void setSparkle(boolean sparkle) {
		this.sparkle = sparkle;
	}
	
	
}
