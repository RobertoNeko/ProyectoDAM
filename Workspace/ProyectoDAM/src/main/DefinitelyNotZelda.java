package main;

import base.BaseGame;
import screens.ExtraLevel1;
import screens.ExtraLevel2;
import screens.Level1;
import screens.Level2;
import screens.Level3;
import screens.Menuscreen;
import screens.SelectLevelScreen;
import screens.TheEnd;

/**
 * 
 * @author Roberto Ramiro Hernández
 * Se encarga se asignar la pantalla con la que se abrira el juego.
 */

public class DefinitelyNotZelda extends BaseGame{
	
	public void create() {
		super.create();
		setActiveScreen(new Menuscreen());
	}
}
