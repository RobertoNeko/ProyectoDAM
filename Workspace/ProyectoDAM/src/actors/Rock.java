package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Actor que actua como una roca, si el jugador usa una bomba podra romperlas
 * @author Roberto Ramiro Hernández
 *
 */
public class Rock extends Solid{

	public Rock(float x, float y, Stage stage) {
		super(x, y, 16, 16, stage);
		loadTexture("assets/sprites/objects/rock.png");
		setBoundaryRectangle();
	}

}
