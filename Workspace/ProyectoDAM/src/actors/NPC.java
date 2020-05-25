package actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import base.BaseActor;

/**
 * Actor que ayuda al jugador, dependiendo de su ID tendra una funcion u otra
 * @author Roberto Ramiro Hernández
 *
 */

public class NPC extends BaseActor{

	private String text;
	private boolean viewing;
	private String ID;
	
	public NPC(float x, float y, Stage stage) {
		super(x, y, stage);
		text = "";
		viewing = false;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setViewing(boolean viewing) {
		this.viewing = viewing;
	}
	
	public boolean isViewing() {
		return viewing;
	}
	
	public void setID(String id) {
		ID = id;
		if(ID.equals("shopKeeper1")) {
			String[] fileNames = {"assets/sprites/npcs/red001.png", "assets/sprites/npcs/red002.png",
					"assets/sprites/npcs/red003.png", "assets/sprites/npcs/red004.png"};
			loadAnimationFromFiles(fileNames, 0.80f, true);
			setScale(0.8f);
		}else if(ID.equals("shoopKeeper2")) {
			String[] fileNames = {"assets/sprites/npcs/blue001.png", "assets/sprites/npcs/blue002.png",
					"assets/sprites/npcs/blue003.png", "assets/sprites/npcs/blue004.png"};
			loadAnimationFromFiles(fileNames, 0.80f, true);
			setScale(0.8f);
		}else if(ID.equals("tutoKid")) {
			loadAnimationFromSheet("assets/sprites/npcs/kidSheet.png", 1, 3, 0.4f, true);
			setSize(18, 20);
			setScale(0.8f);
		}else if(ID.equals("guard")) {
			loadAnimationFromSheet("assets/sprites/npcs/guardSheet.png", 1, 3, 0.7f, true);
			setSize(16, 28);
			setScale(0.9f);
		}else if(ID.equals("zelda")){
			loadAnimationFromSheet("assets/sprites/npcs/zeldaSheet.png", 1, 5, 0.8f, true);
			setSize(17, 28);
			setScale(0.8f);
		}else {
			String[] fileNames = {"assets/sprites/npcs/green001.png", "assets/sprites/npcs/green002.png",
					"assets/sprites/npcs/green003.png", "assets/sprites/npcs/green004.png"};
			loadAnimationFromFiles(fileNames, 0.80f, true);
		}
		
	}

	public String getID() {
		return ID;
	}
}
