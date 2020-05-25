package screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;

/**
 * @author Roberto Ramiro Hernández
 * Clase que se encarga de las opciones, en esta pantalla se puede asginar un nombre y una contraseña para que puedan ser usados en el online
 */
public class SettingsScreen extends BaseScreen{

	private Music music;
	public String username;
	public String password;
	
	@Override
	public void initialize() {
		
		music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/02 File Select.mp3"));
		music.play();

		Skin skin = new Skin(Gdx.files.internal("assets/base/skin/cloud-form-ui.json"));
		
		BaseActor wallpaper = new BaseActor(0, 0, mainStage);
		wallpaper.loadTexture("assets/images/wp.jpg");
		wallpaper.setSize(900, 650);
		
		Label titulo = new Label("Settings", BaseGame.labelStyle);
		titulo.setFontScale(1.5f);
		uiTable.add(titulo).colspan(2);
		uiTable.row().padTop(100);
		
		Label lblUsername = new Label("Username", BaseGame.labelStyle);
		lblUsername.setFontScale(0.8f);
		uiTable.add(lblUsername);
		
		TextField txtUsername = new TextField("", skin);
		uiTable.add(txtUsername);
		uiTable.row().pad(50);
		
		Label lblPass = new Label("Password", BaseGame.labelStyle);
		lblPass.setFontScale(0.8f);
		uiTable.add(lblPass);
		
		TextField txtPass = new TextField("", skin);
		uiTable.add(txtPass);
		uiTable.row();
		
		File file = new File("assets/data/info.txt");
		
		try {
			if(file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String datos = br.readLine();
				
				String[] data = datos.split("-");
			
				txtUsername.setText(data[0]);
				txtPass.setText(data[1]);
				br.close();
			}else {
				txtUsername.setText("");
				txtPass.setText("");
			}
		} catch (IOException e2) {}
		
		
		TextButton btnAccept = new TextButton("      Accept      ", BaseGame.textButtonStyle);
		uiTable.add(btnAccept);
		btnAccept.addListener((Event e) -> {
			
				if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				
				username = txtUsername.getText();
				password = txtPass.getText();
				
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write(username + "-" + password + "-" + 0);
					bw.close();
				} catch (IOException e1) {}
				
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Menuscreen());
				return false;
			}	
		);
		
		TextButton btnBack = new TextButton("        Back        ", BaseGame.textButtonStyle);
		uiTable.add(btnBack);
		btnBack.addListener((Event e) -> {
			
				if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				music.stop();
				DefinitelyNotZelda.setActiveScreen(new Menuscreen());
				return false;
			}	
		);
		
		
	}

	@Override
	public void update(float delta) {}
}
