package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;

/**
 * 
 * @author Roberto Ramiro Hernandez
 * Clase que sirve para seleccionar un nivel en especifico
 */
public class SelectLevelScreen extends BaseScreen {

	private Music music;
	
	
	@Override
	public void initialize() {
		music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/02 File Select.mp3"));
		music.play();
		
		BaseActor wallpaper = new BaseActor(0, 0, mainStage);
		wallpaper.loadTexture("assets/images/wp.jpg");
		wallpaper.setSize(900, 650);
		
		TextButton lvl1 = new TextButton("Level 1", BaseGame.textButtonStyle);
		uiTable.add(lvl1).pad(50);
		
		lvl1.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Level1() );
				return false;
			}
		);
		
		TextButton lvl2 = new TextButton("Level 2", BaseGame.textButtonStyle);
		
		uiTable.add(lvl2);
		uiTable.row();
		lvl2.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Level2() );
				return false;
			}
		);
		
		TextButton lvl3 = new TextButton("Level 3", BaseGame.textButtonStyle);
		uiTable.add(lvl3).pad(50);
		
		lvl3.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Level3() );
				return false;
			}
		);
		
		TextButton extra1 = new TextButton("Extra 1", BaseGame.textButtonStyle);
		uiTable.add(extra1);
		uiTable.row();
		extra1.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new ExtraLevel1() );
				return false;
			}
		);
		
		TextButton extra2 = new TextButton("Extra 2", BaseGame.textButtonStyle);
		uiTable.add(extra2).pad(50);;
		
		extra2.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new ExtraLevel2() );
				return false;
			}
		);
		
		TextButton back = new TextButton("  Back  ", BaseGame.textButtonStyle);
		uiTable.add(back);
		uiTable.row();
		back.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Menuscreen());
				return false;
			}
		);
		
		
	}

	@Override
	public void update(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			music.stop();
			DefinitelyNotZelda.setActiveScreen(new Level1());
		}	
	}

	public boolean keyDown(int keyCode) {
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			music.stop();
			DefinitelyNotZelda.setActiveScreen(new Level1());
		}if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			music.stop();
			DefinitelyNotZelda.setActiveScreen(new Menuscreen());
		}
		return false;
	}

}
