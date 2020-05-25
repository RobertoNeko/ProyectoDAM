package actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Jefe final del juego, cuenta con una variedad de ataques, cuando le queda menos del 25% de vida sus ataques son diferentes
 * @author Roberto Ramiro Hernández
 *
 */

public class Vaati extends BaseActor{

	public int health;
	
	Animation transforming;
	Animation moving;
	
	float posX;
	float posY;
	
	boolean aggro;
	boolean trans;
	boolean inv;
	
	public Vaati(float x, float y, Stage stage) {
		super(x, y, stage);
		health = 1000;
		aggro = false;
		trans = false;
		inv = true;
		
		posX = this.getX();
		posY = this.getY();

		loadTexture("assets/sprites/enemies/vaati/idle.png");
		setSize(20, 28);
		setScale(0.8f);
		setSpeed(0);
		
		String[] fileNames1 = {"assets/sprites/enemies/vaati/transforming/trans001.png", "assets/sprites/enemies/vaati/transforming/trans002.png", "assets/sprites/enemies/vaati/transforming/trans003.png", "assets/sprites/enemies/vaati/transforming/trans004.png", "assets/sprites/enemies/vaati/transforming/trans005.png", "assets/sprites/enemies/vaati/transforming/trans006.png", "assets/sprites/enemies/vaati/transforming/trans007.png", "assets/sprites/enemies/vaati/transforming/trans008.png", "assets/sprites/enemies/vaati/transforming/trans009.png", "assets/sprites/enemies/vaati/transforming/trans010.png", "assets/sprites/enemies/vaati/transforming/trans011.png", "assets/sprites/enemies/vaati/transforming/trans012.png", "assets/sprites/enemies/vaati/transforming/trans013.png", "assets/sprites/enemies/vaati/transforming/trans014.png"};		
		String[] fileNames2 = {"assets/sprites/enemies/vaati/normal/moving001.png", "assets/sprites/enemies/vaati/normal/moving002.png", "assets/sprites/enemies/vaati/normal/moving003.png", "assets/sprites/enemies/vaati/normal/moving004.png", "assets/sprites/enemies/vaati/normal/moving005.png", "assets/sprites/enemies/vaati/normal/moving006.png", "assets/sprites/enemies/vaati/normal/moving007.png", "assets/sprites/enemies/vaati/normal/moving008.png", "assets/sprites/enemies/vaati/normal/moving009.png", "assets/sprites/enemies/vaati/normal/moving010.png", "assets/sprites/enemies/vaati/normal/moving011.png", "assets/sprites/enemies/vaati/normal/moving012.png"};
		
		transforming = loadAnimationFromFiles(fileNames1, 0.2f, false);
		moving = loadAnimationFromFiles(fileNames2, 0.5f, true);
		
	}
	//48 65
	public void act(float delta) {
		super.act(delta);
		
		if(trans) {
			setAnimation(transforming);
			setSize(48, 65);
			setScale(1f);
			if(isAnimationFinished()) {
				setAnimation(transforming);
				trans = false;
				aggro = true;
			}
		}
	
		if(aggro) {
			trans = false;
			setSize(44, 60);
			setSpeed(40);
			setAnimation(moving);
		}
		
		applyPhysics(delta);
		
		if(MathUtils.random(1, 120) == 1) {
			setMotionAngle(MathUtils.random(0, 360));
		}
		
		boundToLimits(posX + 200, posY + 150);
		boundToWorld();
		
	}

	public void throwFireball(float angle) {
		Fireball fireball = new Fireball(0, 0, this.getStage());
		fireball.centerAtActor(this);
		fireball.setMotionAngle(angle);
	}

	public void createTornado() {
		if(health < 250) {
			new Tornado(this.getX() + 25, this.getY(), this.getStage());
			new Tornado(this.getX() - 25, this.getY(), this.getStage()); 
		}else {
			new Tornado(this.getX(), this.getY(), this.getStage());  
		}
		 
	}

	public boolean isAggro() {
		return aggro;
	}

	public void setAggro(boolean aggro) {
		this.aggro = aggro;
	}

	public boolean isTrans() {
		return trans;
	}

	public void setTrans(boolean trans) {
		this.trans = trans;
	}
	public boolean isInv() {
		return inv;
	}
	public void setInv(boolean inv) {
		this.inv = inv;
	}
}
