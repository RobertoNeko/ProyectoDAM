package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, actua por su cuenta, cuando es golpeado se vuelve loco y tras pasado un tiempo explotara
 * @author rober
 *
 */

public class BombEnemy extends BaseActor{

	boolean hit;
	boolean explode;
	float posX;
	float posY;
	
	Animation leftAn;
	Animation rightAn;
	Animation hitLeftAn;
	Animation hitRightAn;
	
	float facingAngle;
	BombExp exp;
	private float timeSeconds = 0f;
	private float period = 3f;
	
	public BombEnemy(float x, float y, Stage stage) {
		super(x, y, stage);
		
		hit = false;
		posX = this.getX();
		posY = this.getY();
		
		String[] left = {"assets/sprites/enemies/bombEn/bombEnLeft001.png", "assets/sprites/enemies/bombEn/bombEnLeft002.png", "assets/sprites/enemies/bombEn/bombEnLeft003.png"};
		leftAn = loadAnimationFromFiles(left, 0.5f, true);
		String[] right = {"assets/sprites/enemies/bombEn/bombEnRight001.png", "assets/sprites/enemies/bombEn/bombEnRight002.png", "assets/sprites/enemies/bombEn/bombEnRight003.png"};
		rightAn = loadAnimationFromFiles(right, 0.5f, true);
		String[] hitLeft = {"assets/sprites/enemies/bombEn/bombEnHurtLeft001.png", "assets/sprites/enemies/bombEn/bombEnHurtLeft002.png", "assets/sprites/enemies/bombEn/bombEnHurtLeft003.png"};
		hitLeftAn = loadAnimationFromFiles(hitLeft, 0.5f, true);
		String[] hitRight = {"assets/sprites/enemies/bombEn/bombEnHurtRight001.png", "assets/sprites/enemies/bombEn/bombEnHurtRight002.png", "assets/sprites/enemies/bombEn/bombEnHurtRight003.png"};
		hitRightAn = loadAnimationFromFiles(hitRight, 0.5f, true);
		
		setBoundaryPolygon(8);
		setAnimation(leftAn);
		setSpeed(25);
	}
	
	public void act(float delta) {
		super.act(delta);
		
		if(getSpeed() == 0) {
			setAnimationPaused(true);
		}else {
			setAnimationPaused(false);
			
			float angle = getMotionAngle();

			if(angle >= 45 && angle < 225) {
				//setSize(32, 25);
				if(hit) {
					setAnimation(hitLeftAn);
					setSpeed(75);
					setSize(16, 24);
					timeSeconds += Gdx.graphics.getRawDeltaTime();
				    if(timeSeconds > period){
				        timeSeconds-=period;
				        exp = new BombExp(0, 0, this.getStage());
				        exp.centerAtActor(this);
				        remove();
				    }
				}else {
					setAnimation(leftAn);
					setSize(13, 17);
					setSpeed(25);
				}
			}else {
				if(hit) {
					setAnimation(hitRightAn);
					setSpeed(75);
					setSize(16, 24);
					timeSeconds += Gdx.graphics.getRawDeltaTime();
				    if(timeSeconds > period){
				        timeSeconds-=period;
				        exp = new BombExp(0, 0, this.getStage());
				        exp.centerAtActor(this);
				        remove();
				    }
				}else {
					setAnimation(rightAn);
					setSize(13, 17);
					setSpeed(25);
				}
			}	
		}
		if(MathUtils.random(1, 100) == 1) {
			setMotionAngle(MathUtils.random(0, 360));
		}
		boundToLimits(posX + 100, posY + 100);
		boundToWorld();
		applyPhysics(delta);
	}
	
	public float getFacingAngle() {
		return facingAngle;
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
	
	
}
