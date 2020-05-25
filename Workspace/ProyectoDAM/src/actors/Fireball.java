package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import base.BaseActor;

/**
 * Actor que actua como proyectil que usara el Final Boss, una vez golpea un solido se expande y tras pasados unos segundos se extinguira
 * @author Roberto Ramiro Hernández
 *
 */

public class Fireball extends BaseActor{

	Animation ballAn;
	Animation growAn;
	Animation extAn;
	
	boolean grow;
	boolean ex;
	
	float timeSecondsFire = 0f;
	float periodFire = 3f;
	
	public Fireball(float x, float y, Stage stage) {
		super(x, y, stage);
		grow = false;
		ex = false;
		
		String[] fileNames1 = {"assets/sprites/enemies/vaati/attacks/fireball/fireball001.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball002.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball003.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball004.png"};
		String[] fileNames2 = {"assets/sprites/enemies/vaati/attacks/fireball/fireball005.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball006.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball007.png"};
		String[] fileNames3 = {"assets/sprites/enemies/vaati/attacks/fireball/fireball008.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball009.png", "assets/sprites/enemies/vaati/attacks/fireball/fireball010.png"};
		
		ballAn = loadAnimationFromFiles(fileNames1, 0.5f, true);
		growAn = loadAnimationFromFiles(fileNames2, 0.5f, true);
		extAn = loadAnimationFromFiles(fileNames3, 0.2f, true);
		
		setAnimation(ballAn);
		setSize(14, 15);
		
		setSpeed(100);
	}
	
	public void act(float delta) {
		super.act(delta);
		
		if(grow) {
			setAnimation(growAn);
			setSize(24, 34);
			setSpeed(0);
			timeSecondsFire += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsFire > periodFire){
		    	timeSecondsFire -= periodFire;
		    	setGrow(false);
		    	setEx(true);
		    }
			
		}
		if(ex) {
			setAnimation(extAn);
			setSize(14, 15);
			setScale(0.7f);
			setSpeed(0);
			addAction(Actions.fadeOut(1f));
			addAction(Actions.after(Actions.removeActor()));
		}
		
		applyPhysics(delta);
	}
	
	public boolean isGrow() {
		return grow;
	}

	public void setGrow(boolean grow) {
		this.grow = grow;
	}

	public boolean isEx() {
		return ex;
	}

	public void setEx(boolean ex) {
		this.ex = ex;
	}
}
