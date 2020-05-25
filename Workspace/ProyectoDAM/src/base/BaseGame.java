package base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Se crea cuando el programa se inicia
 * Controla las pantallas que aparecen durante el juego
 * @author Roberto Ramiro Hernández
 *
 */

public abstract class BaseGame extends Game{

	private static BaseGame game;
	
	public static LabelStyle labelStyle;
	public static TextButtonStyle textButtonStyle;
	
	/**
	 * Se llama cuando el juego es inicializado, guarda la referencia global del juego
	 */
	public BaseGame() {
		game = this;
	}
	
	/**
	 * Se llama cuando el juego es inicializado, cuando Gdx.input y otros objetos se han inicializado
	 */
	public void create() {
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        
        //Para usar una fuente externa
        labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("assets/base/cooper.fnt"));
        
        //Para usar una fuente generada
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("assets/base/OpenSans.ttf"));
        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.size = 40;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 1;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = TextureFilter.Linear;
        fontParameters.magFilter = TextureFilter.Linear;
        
        BitmapFont customFont = fontGenerator.generateFont(fontParameters);
        labelStyle.font = customFont;
        
        textButtonStyle = new TextButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("assets/base/buttonEx.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 35, 30, 24, 24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = Color.GRAY;
	}
	
	/**
	 * Se usa para cambiar pantallas mientras el juego esta funcionando
	 */
	public static void setActiveScreen(BaseScreen screen) {
		game.setScreen(screen);
	}
}
