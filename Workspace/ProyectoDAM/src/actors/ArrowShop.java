package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que actua como item de la tienda
 * @author Roberto Ramiro Hernandez
 *
 */

public class ArrowShop extends BaseActor{
	
	public ArrowShop(float x, float y, Stage stage) {
		super(x, y, stage);
		loadTexture("assets/sprites/objects/arrow.png");
		setSize(15, 5);
		setBoundaryPolygon(4);
	}
}
