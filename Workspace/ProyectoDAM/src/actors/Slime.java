package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Enemigo que es invulnerable a ciertos ataques, no hace daño al jugador, solo molesta
 * @author Roberto Ramiro Hernández
 *
 */

public class Slime extends BaseActor{

	float posX;
	float posY;
	
	public Animation spawnAnimation;
	public Animation defaultAnimation;
	public Animation hurtAnimation;
	public Animation attackingAnimation;
	
	public boolean attacking;
	public boolean hurt;
	
	public Slime(float x, float y, Stage stage) {
		super(x, y, stage);
	
		posX = this.getX();
		posY = this.getY();
		
		String[] fileNames = {"assets/sprites/enemies/slime/spawn001.png", "assets/sprites/enemies/slime/spawn002.png",
				"assets/sprites/enemies/slime/spawn003.png", "assets/sprites/enemies/slime/spawn004.png", "assets/sprites/enemies/slime/spawn005.png",
				"assets/sprites/enemies/slime/spawn006.png", "assets/sprites/enemies/slime/spawn007.png", "assets/sprites/enemies/slime/spawn008.png", 
				"assets/sprites/enemies/slime/spawn009.png", "assets/sprites/enemies/slime/spawn010.png", "assets/sprites/enemies/slime/spawn011.png"};
		
		
		spawnAnimation = loadAnimationFromFiles(fileNames, 0.15f, false);
		
		String[] fileNames2 = {"assets/sprites/enemies/slime/default001.png", "assets/sprites/enemies/slime/default002.png",
				"assets/sprites/enemies/slime/default003.png", "assets/sprites/enemies/slime/default004.png", "assets/sprites/enemies/slime/default005.png",
				"assets/sprites/enemies/slime/default006.png", "assets/sprites/enemies/slime/default007.png", "assets/sprites/enemies/slime/default008.png", 
				"assets/sprites/enemies/slime/default009.png", "assets/sprites/enemies/slime/default010.png", "assets/sprites/enemies/slime/default011.png"};
		
		defaultAnimation = loadAnimationFromFiles(fileNames2, 0.3f, true);
	
		String[] fileNames3 = {"assets/sprites/enemies/slime/hurt001.png", "assets/sprites/enemies/slime/hurt002.png", "assets/sprites/enemies/slime/hurt003.png"};
		
		hurtAnimation = loadAnimationFromFiles(fileNames3, 2f, false);
		
		String[] fileNames4 = {"assets/sprites/enemies/slime/jump001.png", "assets/sprites/enemies/slime/jump002.png", "assets/sprites/enemies/slime/jump003.png",
				"assets/sprites/enemies/slime/jump004.png", "assets/sprites/enemies/slime/jump005.png", "assets/sprites/enemies/slime/jump006.png", "assets/sprites/enemies/slime/jump007.png"};
		
		attackingAnimation = loadAnimationFromFiles(fileNames4, 0.2f, false);
		
		setBoundaryPolygon(8);
		setScale(0.9f);
		setAnimation(spawnAnimation);
		setVisible(false);
	}

	public void act(float delta) {
		super.act(delta);
		
		if(getSpeed() == 0) {
			setAnimationPaused(true);
		}else {
			setSpeed(10);
			setAnimationPaused(false);
			if(isAnimationFinished()) {
				if(attacking) {
					setAnimation(attackingAnimation);
					attacking = false;
				}else if(hurt){
					setAnimation(hurtAnimation);
					if(isAnimationFinished()) {
						hurt = false;
					}
				}
				else {
					setAnimation(defaultAnimation);
				}
			}	
		}
		
		if(MathUtils.random(1, 120) == 1) {
			setMotionAngle(MathUtils.random(0, 360));
		}
		boundToWorld();
		boundToLimits(posX + 50, posY + 50);
		applyPhysics(delta);
	}
	
	public void spawn() {
		setVisible(true);
		setSpeed(10);
	}
	
	public void setAttacking(boolean atk) {
		attacking = atk;
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}
	
	public boolean isHurt() {
		return hurt;
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
