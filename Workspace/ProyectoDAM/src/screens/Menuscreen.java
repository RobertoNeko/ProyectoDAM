package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;
import online.Cliente;

/**
 * 
 * @author Roberto Ramiro Hernández
 * Es la clase que se encarga del menu principal
 * Cuenta con varias opciones: Iniciar un juego nuevo, Seleccionar un nivel en especifico, Cambiar los ajustes, Conectar con el servidor y Salir del juego.
 */

public class Menuscreen extends BaseScreen {

	private Music music;
	public static Cliente cliente;

	/**
	 * Se inicializan todos los elementos del menu y se les asgina una funcion a los botones
	 */
	@Override
	public void initialize() {
			
		music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/02 File Select.mp3"));
		music.play();
		
		cliente = new Cliente();
		
		BaseActor wallpaper = new BaseActor(0, 0, mainStage);
		wallpaper.loadTexture("assets/images/wp.jpg");
		wallpaper.setSize(900, 650);
		
		BaseActor title = new BaseActor(0, 0, mainStage);
		title.loadTexture("assets/images/title3.png");
		uiTable.add(title);
		uiTable.row();
		
		TextButton startButton = new TextButton(" New Game ", BaseGame.textButtonStyle);
		uiTable.add(startButton);
		uiTable.row();
		startButton.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Level1());
				return false;
			}
		);

		TextButton selectButton = new TextButton("Select Level", BaseGame.textButtonStyle);
		uiTable.add(selectButton);
		uiTable.row();
		selectButton.addListener((Event e) -> {
			
				if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new SelectLevelScreen());
				return false;
			}	
		);
		

		TextButton settingsButton = new TextButton("    Settings    ", BaseGame.textButtonStyle);
		uiTable.add(settingsButton);
		uiTable.row();
		settingsButton.addListener((Event e) -> {
			
				if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new SettingsScreen());
				return false;
			}	
		);
		
		TextButton connectButton = new TextButton("    Connect    ", BaseGame.textButtonStyle);
		uiTable.add(connectButton);
		uiTable.row();
		connectButton.addListener((Event e) -> {
			
				if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				uiTableMenu.clear();
				cliente.conectar("localhost", 1234);
				
				if(cliente.socket == null) {
					Label label = new Label("No se ha podido establecer la conexion", BaseGame.labelStyle);
					label.setColor(Color.RED);
					uiTableMenu.add(label).padTop(575);
					label.addAction(Actions.fadeOut(5));
					label.addAction(Actions.after(Actions.removeActor()));
				}else {
					Label label = new Label("Conexion establecida", BaseGame.labelStyle);
					label.setColor(Color.GREEN);
					uiTableMenu.add(label).padTop(575);
					label.addAction(Actions.fadeOut(2));
					label.addAction(Actions.after(Actions.removeActor()));
				}
				return false;
			}	
		);
		
		TextButton quitButton = new TextButton("        Quit        ", BaseGame.textButtonStyle);
		uiTable.add(quitButton);
		quitButton.addListener((Event e) -> {
			
				if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				Gdx.app.exit();
				return false;
			}	
		);

	}

	/**
	 * Metodo que se actualiza en tiempo real
	 */
	@Override
	public void update(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			music.stop();
			DefinitelyNotZelda.setActiveScreen(new Level1());
		}	
	}

	/**
	 * Metodo que reacciona cuando se presiona un boton
	 */
	public boolean keyDown(int keyCode) {
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			music.stop();
			DefinitelyNotZelda.setActiveScreen(new Level1());
		}if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		return false;
	}
}
