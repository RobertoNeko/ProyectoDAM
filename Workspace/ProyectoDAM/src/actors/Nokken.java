package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor enemigo, enemigo invencible que ira a por el jugador, se vuelve vulnerable al cabo de unos segundos
 * @author Roberto Ramiro Hernández
 *
 */
public class Nokken extends BaseActor{

	boolean aggro;
	boolean vul;
	Animation iddle;
	Animation moving;
	Animation vulnerable;
	
	float facingAngle;
	float timeSecondsVul;
	float periodVul;
	float timeSecondsAggro;
	float periodAggro;
	
	
	public Nokken(float x, float y, Stage stage) {
		super(x, y, stage);
		aggro = false;
		vul = false;
		facingAngle = 270;
		timeSecondsVul = 0f;
		periodVul = 1.5f;
		timeSecondsAggro = 0f;
		periodAggro = 5f;
		
		String[] fileNames1 = {"assets/sprites/enemies/nokken/iddle001.png", "assets/sprites/enemies/nokken/iddle002.png", "assets/sprites/enemies/nokken/iddle003.png"};
		iddle = loadAnimationFromFiles(fileNames1, 0.2f, true);
		
		String[] fileNames2 = {"assets/sprites/enemies/nokken/aggro001.png", "assets/sprites/enemies/nokken/aggro002.png", "assets/sprites/enemies/nokken/aggro003.png", "assets/sprites/enemies/nokken/aggro004.png", "assets/sprites/enemies/nokken/aggro005.png", "assets/sprites/enemies/nokken/aggro006.png"};
		moving = loadAnimationFromFiles(fileNames2, 0.4f, true);
		
		String[] fileNames3 = {"assets/sprites/enemies/nokken/vul001.png", "assets/sprites/enemies/nokken/vul002.png", "assets/sprites/enemies/nokken/vul003.png"}; 
		vulnerable = loadAnimationFromFiles(fileNames3, 0.5f, true);	
		
		setAnimation(iddle);
		setBoundaryPolygon(8);
		setScale(0.8f);
		setSpeed(0);
		
	}

	public void act(float delta) {
		super.act(delta);
		
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
			setAnimation(moving);
			setSpeed(30);
			
			timeSecondsAggro += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsAggro > periodAggro){
		    	timeSecondsAggro -= periodAggro;
		    	aggro = false;
		    	vul = true;
		    }
			
		}
		
		if(vul) {
			setAnimation(vulnerable);
			setSpeed(10);
			timeSecondsVul += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsVul > periodVul){
		    	timeSecondsVul -= periodVul;
		    	vul = false;
		    	aggro = true;
		    }
		}
		
		if(!aggro && !vul) {
			setAnimation(iddle);
			setSpeed(0);
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

	public boolean isVul() {
		return vul;
	}

	public void setVul(boolean vul) {
		this.vul = vul;
	}

	
	
	
}
