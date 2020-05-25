package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;

/**
 * 
 * @author Roberto Ramiro Hernandez
 * Clase que representa la pantalla final del juego una vez pasado un tiempo volvera a la pantalla de menu
 */

public class TheEnd extends BaseScreen{
	
	float timeSeconds = 0f;
	float period = 10f;
	
	@Override
	public void initialize() {
		BaseActor wallpaper = new BaseActor(0, 0, mainStage);
		wallpaper.loadTexture("assets/images/ending.png");
		wallpaper.setSize(900, 650);
		
		Label label = new Label("Thanks for playing", BaseGame.labelStyle);
		label.setVisible(false);
		label.setFontScale(2);
		label.moveBy(50, 300);
		label.addAction(Actions.after(Actions.fadeIn(1)));
		label.addAction(Actions.after(Actions.visible(true)));
		uiStage.addActor(label);
		
	}

	@Override
	public void update(float delta) {
		
		timeSeconds += Gdx.graphics.getRawDeltaTime();
	    if(timeSeconds > period){
	    	timeSeconds -= period;
	    	DefinitelyNotZelda.setActiveScreen(new Menuscreen());
	    }
	}
}
