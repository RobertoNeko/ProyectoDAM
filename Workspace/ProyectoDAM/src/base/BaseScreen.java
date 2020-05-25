package base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * 
 * @author Roberto Ramiro Hernández
 * Clase que sirve como base de la pantalla
 */
public abstract class BaseScreen implements Screen, InputProcessor{

	protected Stage mainStage;
	protected Stage uiStage;
	protected Table uiTable;
	
	protected Stage uiMenu;
	protected Table uiTableMenu;
	
	/**
	 * Contructor, inicia los escenearios y las tablas.
	 */
	public BaseScreen() {
		mainStage = new Stage();
		mainStage.getViewport().update(900/4, 650/4);
		
		uiStage = new Stage();
		uiTable = new Table();
		uiTable.setFillParent(true);
		uiStage.addActor(uiTable);
		
		uiMenu = new Stage();
		uiTableMenu = new Table();
		uiTableMenu.setFillParent(true);
		uiMenu.addActor(uiTableMenu);
		
		initialize();
	}
	
	/**
	 * Se usa para iniciar los objetos del juego y los elementos de la interfaz
	 */
	public abstract void initialize();
	
	/**
	 * Metodo que se actualiza a tiempo real
	 * @param delta
	 */
	public abstract void update(float delta);
	
	/**
	 * Bucle del juego
	 * 1 - Process Input
	 * 2 - Update game logic
	 * 3 - Render the graphics
	 */
	public void render(float delta) {
		uiStage.act(delta);
		uiMenu.act(delta);
		mainStage.act(delta);
		
		update(delta);
		
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mainStage.draw();
		uiStage.draw();
		uiMenu.draw();
	}
	
	/**
	 * Se llama cuando se convierte en la pantalla activa del juego
	 */
	public void show() {
		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
		im.addProcessor(this);
		im.addProcessor(uiStage);
		im.addProcessor(uiMenu);
		im.addProcessor(mainStage);
	}
	
	/**
	 * Se llama cuando esta pantalla ya no es la activa.
	 */
	public void hide() {
		InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
		im.removeProcessor(this);
		im.removeProcessor(uiStage);
		im.removeProcessor(uiMenu);
		im.removeProcessor(mainStage);
	}
	
	/**
	 * Esta atento a si hay un evento que hace uso del raton
	 * @param e
	 * @return
	 */
	public boolean isTouchDownEvent(Event e) {
		return (e instanceof InputEvent) && ((InputEvent)e).getType().equals(Type.touchDown);
	}
	
	public void resize(int width, int height) { }
	
	public void pause() { }
	
	public void resume() { }
	
	public void dispose() { }

	public void restart() { }
	
	public boolean keyDown(int keycode) { return false; }
	
	public boolean keyUp(int keycode) { return false; }
	
	public boolean keyTyped(char c) { return false; }
	
	public boolean mouseMoved(int screenX, int screenY) { return false; }
	
	public boolean scrolled(int amount) { return false; }
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
	
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
}
