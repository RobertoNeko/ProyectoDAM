package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import base.BaseActor;

/**
 * Enemigo que lanzara proyectiles cuando el jugador este cerca, ademas huira de el
 * @author Roberto Ramiro Hernández
 *
 */
public class Skeleton extends BaseActor{

	public int health = 20;
	
	private Bone bone;
	
	float posX;
	float posY;
	
	Animation north;
	Animation south;
	Animation west;
	Animation east;
	
	float facingAngle;
	
	
	public Skeleton(float x, float y, Stage stage) {
		super(x, y, stage);
		
		posX = this.getX();
		posY = this.getY();
		
		north = loadAnimationFromSheet("assets/sprites/enemies/skeleton/northSheet.png", 1, 6, 0.3f, true);
		south = loadAnimationFromSheet("assets/sprites/enemies/skeleton/southSheet.png", 1, 6, 0.3f, true);
		west = loadAnimationFromSheet("assets/sprites/enemies/skeleton/westSheet.png", 1, 6, 0.3f, true);
		east = loadAnimationFromSheet("assets/sprites/enemies/skeleton/eastSheet.png", 1, 6, 0.3f, true);

		setBoundaryPolygon(8);
		setAnimation(south);
		facingAngle = 270;
		
		setSpeed(30);
		
	}
	
	public void act(float delta) {
		super.act(delta);
		
		if(getSpeed() == 0) {
			setAnimationPaused(true);
		}else {
			setAnimationPaused(false);
			setSpeed(30);
			float angle = getMotionAngle();
			
			if(angle >= 45 && angle <= 135) {
				setAnimation(north);
				setSize(22, 24);
				facingAngle = 90;
			}else if(angle >= 135 && angle < 225) {
				setAnimation(west);
				setSize(23, 27);
				facingAngle = 180;				
			}else if(angle >= 225 && angle <= 315) {
				setAnimation(south);
				setSize(28, 29);
				facingAngle = 270;			
			}else {
				setAnimation(east);	
				setSize(23, 27);
				facingAngle = 0;			
			}
		}
		
		if(MathUtils.random(1, 120) == 1) {
			setMotionAngle(MathUtils.random(0, 360));
		}
		boundToLimits(posX + 50, posY + 50);
		boundToWorld();
		applyPhysics(delta);
	}
	
	public void throwBone() {
		Bone bone = new Bone(0, 0, this.getStage());
		bone.centerAtActor(this);
		bone.setRotation(this.getFacingAngle());
		bone.setMotionAngle(this.getFacingAngle() + 180);
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
}
