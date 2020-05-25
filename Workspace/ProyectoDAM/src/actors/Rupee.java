package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como moneda del juego, tiene varios valores
 * @author Roberto Ramiro Hernández
 *
 */

public class Rupee extends BaseActor{

	int valor;
	
	public Rupee(float x, float y, Stage stage, int valor) {
		super(x, y, stage);
		
		this.valor = valor;
		
		if(valor == 1) {
			loadAnimationFromSheet("assets/sprites/objects/greenRupee.png", 1, 3, 0.2f, true);
		}
		else if(valor == 5) {
			loadAnimationFromSheet("assets/sprites/objects/blueRupee.png", 1, 3, 0.2f, true);
		}
		else {
			loadAnimationFromSheet("assets/sprites/objects/redRupee.png", 1, 3, 0.2f, true);
		}
		setSize(8, 14);
		setScale(0.8f);
		setBoundaryPolygon(4);
	}
	
	public int getValor() {
		return valor;
	}
	
}
