package screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import actors.Arrow;
import actors.ArrowDrop;
import actors.ArrowShop;
import actors.Beetle;
import actors.Bomb;
import actors.BombExp;
import actors.Chest;
import actors.DialogBox;
import actors.EnemyExp;
import actors.Keaton;
import actors.Key;
import actors.NPC;
import actors.Player;
import actors.Rock;
import actors.Rupee;
import actors.Slime;
import actors.Solid;
import actors.Sword;
import actors.TilemapActor;
import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;
import online.Cliente;

/**
 * 
 * @author Roberto Ramiro Hernández
 * Primer nivel del Videojuego
 */

public class Level1 extends BaseScreen{

	Player player;
	Sword sword;
	Chest chest;
	Key key;
	ArrowShop arrowShop;
	
	int arrows;
	int rupees;
	int health;
	int bombs;
	int keys;
	int score;
	
	DialogBox dialogBox;
	boolean gameOver;
	boolean youWin;
	boolean paused;
	
	//Tiempo del powerUp
	private float timeSecondsSparkle = 0f;
	private float periodSparkle = 15f;
	
	//UI
	Label healthLabel;
	Label rupeesLabel;
	Label arrowsLabel;
	Label bombsLabel;
	Label messageLabel;
	Label keysLabel;
	
	//Musica - Sonidos 
	Music backgroundMusic;
	Music winMusic;
	Sound arrowShotSound;
	Sound arrowHitSound;
	Sound linkHitSound;
	Sound swordSwungSound1;
	Sound swordSwungSound2;
	Sound swordSwungSound3;
	Sound putBombSound;
	Sound enemyKilledSound;
	Sound rupeeCollectedSound;
	Sound girlLaugh;
	Sound boySound;
	Sound guardSound;
	Sound chestOpenSound;
	Sound boulderDestroyedSound;
	Sound keyBoughtSound;
	
	Cliente cliente;
	
	/**
	 * Se inician los valores 
	 */
	@Override
	public void initialize() {
		
		cliente = new Cliente();
		
		arrows = 7;
		health = 5;
		bombs = 3;
		rupees = 10;
		keys = 0;
		score = 0;
		
		uiInit();
		
		mapInit();
		
		soundtrackInit();		
	}

	/**
	 * Metodo que se actualiza en tiempo real
	 */
	@Override
	public void update(float delta) {
		
		if(gameOver) {
			player.setDead(true);
			player.setSpeed(0.00001f);
			
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				beetle.setSpeed(0);
				beetle.setAnimationPaused(true);
			}
			for(BaseActor slime : BaseActor.getList(mainStage, Slime.class)) {
				slime.setSpeed(0);
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				keaton.setSpeed(0);
			}
			for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
				NPC npc = (NPC)npcActor;
				npc.setSpeed(0);
				npc.setViewing(false);
			}	
			if(Gdx.input.isKeyPressed(Keys.R)) {
				backgroundMusic.stop();
				DefinitelyNotZelda.setActiveScreen(new Level1());
			}
			return;
		}

		if(health <= 0) {
			gameOver = true;
			backgroundMusic.stop();
			backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/33 Game Over.mp3"));
			backgroundMusic.play();
			BaseActor gameOverMessage = new BaseActor(0, 0, uiStage);
			gameOverMessage.loadTexture("assets/images/gameOver.png");
			gameOverMessage.centerAtPosition(450, 325);
			gameOverMessage.setOpacity(0);
			gameOverMessage.setScale(0.5f);
			gameOverMessage.addAction(Actions.delay(1));
			gameOverMessage.addAction(Actions.after(Actions.fadeIn(1)));
			Label label = new Label("Pulsa \"R\" para resetear el nivel", BaseGame.labelStyle);
			label.setFontScale(0.7f);
			uiTable.padLeft(100);
			uiTable.addActor(label);
		}
		
		healthLabel.setText(" x " + health);
		rupeesLabel.setText(" x " + rupees);
		arrowsLabel.setText(" x " + arrows);
		bombsLabel.setText(" x " + bombs);
		keysLabel.setText(" x " + keys);
		
		if(youWin) {
			backgroundMusic.stop();
			winMusic.play();
		}
		
		if(BaseActor.count(mainStage, Beetle.class) == 0 && BaseActor.count(mainStage, Keaton.class) == 0 && BaseActor.count(mainStage, Slime.class) == 0) {
			youWin = true;
		}
		
		if(!paused) {
			if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				player.accelerateAtAngle(180);
			}
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
				player.accelerateAtAngle(0);
			}
			if(Gdx.input.isKeyPressed(Keys.DOWN)) {
				player.accelerateAtAngle(270);
			}
			if(Gdx.input.isKeyPressed(Keys.UP)) {
				player.accelerateAtAngle(90);
			}
			if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				paused = true;
				
				openMenu();
				
				for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
					beetle.setSpeed(0);
					beetle.setAnimationPaused(true);
				}
				for(BaseActor slime : BaseActor.getList(mainStage, Slime.class)) {
					slime.setSpeed(0);
					slime.setAnimationPaused(true);
				}
				for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
					Keaton keaton = (Keaton)keatonActor;
					keaton.setStopped(true);
				}
				for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
					NPC npc = (NPC)npcActor;
					npc.setSpeed(0);
					npc.setViewing(false);
					npc.setAnimationPaused(true);
				}
			}
		}else {
			
			if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				uiTableMenu.clear();
				paused = false;
				for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
					beetle.setSpeed(30);
					beetle.setAnimationPaused(false);
				}
				for(BaseActor slime : BaseActor.getList(mainStage, Slime.class)) {
					slime.setSpeed(10);
					slime.setAnimationPaused(false);
				}
				for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
					Keaton keaton = (Keaton)keatonActor;
					keaton.setStopped(false);
				}
				for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
					NPC npc = (NPC)npcActor;
					npc.setSpeed(0);
					npc.setViewing(false);
					npc.setAnimationPaused(false);
				}
			}

		}
		
		if(player.isSparkle()) {
			timeSecondsSparkle += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsSparkle > periodSparkle){
		    	timeSecondsSparkle -= periodSparkle;
		        player.setSparkle(false);
		    }
		}
		
		/**
		 * Interaccion con los solidos
		 */
		for(BaseActor solid : BaseActor.getList(mainStage, Solid.class)) {
			player.preventOverlap(solid);
			
			float angulo = MathUtils.random(135, 225);
			
			//Enemigo Beetle
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				beetle.preventOverlap(solid);
				if(beetle.isWithinDistance(0.01f, solid)) {
					beetle.setMotionAngle(beetle.getMotionAngle() + angulo);
				}
			}
			
			//Enemigo Keaton
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				keaton.preventOverlap(solid);
				if(keaton.isWithinDistance(0.01f, solid)) {
					keaton.setMotionAngle(keaton.getMotionAngle() + angulo);
				}
			}

			for(BaseActor slime : BaseActor.getList(mainStage, Slime.class)) {
				slime.preventOverlap(solid);
				if(slime.isWithinDistance(0.001f, solid)) {
					slime.setMotionAngle(slime.getMotionAngle() + angulo);
				}
			}
			
			//Bombas del player
			for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
				bomb.preventOverlap(solid);
			}
		}
		
		/**
		 * Interaccion con el chest
		 */
		for(BaseActor chestActor : BaseActor.getList(mainStage, Chest.class)) {
			Chest chest = (Chest)chestActor;
			if(player.isWithinDistance(1f, chest)) {
				if(keys > 0) {
					chest.setOpened(true);
					keys--;
					player.setSparkle(true);
					chestOpenSound.play();
				}
			}
		}
		
		/**
		 * Interaccion con el enemigo Beetle
		 */
		for(BaseActor beetleActor : BaseActor.getList(mainStage, Beetle.class)) {
			
			Beetle beetle = (Beetle)beetleActor;

			if(player.overlaps(beetle)) {
				linkHitSound.play();
				health--;
				player.preventOverlap(beetle);
				beetle.setMotionAngle(beetle.getMotionAngle() + 180);
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 beetlePosition = new Vector2(beetle.getX(), beetle.getY());
				Vector2 hitVector = playerPosition.sub(beetlePosition);
				player.setMotionAngle(hitVector.angle());
				player.setSpeed(50);
			}
		}
		
		/**
		 * Interaccion con el enemigo Keaton
		 */
		for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
			
			Keaton keaton = (Keaton)keatonActor;
			
			if(keaton.isWithinDistance(50, player)) {
				if(!keaton.isHit()){
					keaton.setSpeed(50);
					Vector2 playerPosition = new Vector2(player.getX(), player.getY());
					Vector2 keatonPosition = new Vector2(keaton.getX(), keaton.getY());
					Vector2 hitVector = playerPosition.sub(keatonPosition);
					keaton.setMotionAngle(hitVector.angle());
				}
			}else {
				if(!keaton.isHit()) {
					keaton.setSpeed(10);
				}
			}
			
			if(player.overlaps(keaton)) {
				keaton.setHit(true);
				health--;
				linkHitSound.play();
				player.preventOverlap(keaton);
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 keatonPosition = new Vector2(keaton.getX(), keaton.getY());
				Vector2 hitVector = playerPosition.sub(keatonPosition);
				player.setMotionAngle(hitVector.angle());
				player.setSpeed(100);
			}
		}
		
		/**
		 * Interaccion con el enemigo Slime
		 */
		for(BaseActor slimeActor : BaseActor.getList(mainStage, Slime.class)) {
			
			Slime slime = (Slime)slimeActor;
			if(player.isWithinDistance(5f, slime) && slime.isVisible() == false) {
				slime.spawn();
				
			}
			
			if(player.isWithinDistance(30f, slime)) {
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 slimePosition = new Vector2(slime.getX(), slime.getY());
				Vector2 hitVector = playerPosition.sub(slimePosition);
				slime.setMotionAngle(hitVector.angle());
				
				if(player.isWithinDistance(3f, slime)) {
					slime.setAttacking(true);
				}
			}
			
			if(player.overlaps(slime)) {
				player.preventOverlap(slime);
			}
			
		}
		
		for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
			NPC npc = (NPC)npcActor;
			player.preventOverlap(npcActor);
			
			for(BaseActor beetle: BaseActor.getList(mainStage, Beetle.class)) {
				beetle.preventOverlap(npc);
			}
			
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				keaton.preventOverlap(npc);
			}
		
			boolean nearby = player.isWithinDistance(2, npc);
			
			if(nearby && !npc.isViewing()) {
				
				if(npc.getID().equals("shopKeeper1")) {
					girlLaugh.play();
					if(key == null) {
						npc.setText("¿Quieres comprar flechas?\nUn set de 5 cuesta 10 rupias");
					}else {
						npc.setText("¿Quieres comprar una llave?\nCuestan 25 rupias");
					}
					dialogBox.setText(npc.getText());
				}else if(npc.getID().equals("tutoKid")) {
					boySound.play();
					dialogBox.setText(npc.getText());
				}else if(npc.getID().equals("guard")) {
					guardSound.play();
					
					if(youWin) {
						if(Menuscreen.cliente.getSocket() != null) {
							cliente = Menuscreen.cliente;
							try {
								
								File file = new File("assets/data/info.txt");
								
								if(file.exists()) {
									BufferedReader br = new BufferedReader(new FileReader(file));
									
									String datos = br.readLine();
									String[] data = datos.split("-");
									
									int scor = Integer.parseInt(data[2]);
									
									System.out.println(scor);
									score += scor;
									System.out.println(score);
									BufferedWriter bw = new BufferedWriter(new FileWriter(file));
									bw.write(data[0] + "-" + data[1] + "-" + score);
									
									cliente.enviarData(data[0], data[1], score);
									br.close();
									bw.close();
								}
								
							} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException
									| IllegalBlockSizeException | IOException e) {}
						}
						DefinitelyNotZelda.setActiveScreen(new Level2());
					}else{
						dialogBox.setText(npc.getText());
					}
					
					
				}
				else {
					dialogBox.setText(npc.getText());
				}
				dialogBox.setVisible(true);
				npc.setViewing(true);
			}
			
			if(npc.isViewing() && !nearby) {
				dialogBox.setText(" ");
				dialogBox.setVisible(false);
				npc.setViewing(false);
			}

		}
		
		//Interaccion con las flechas
		for(BaseActor arrow : BaseActor.getList(mainStage, Arrow.class)) {
			
			//Flecha golpea a un beetle
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(arrow.overlaps(beetle)) {
					arrowHitSound.play();
					arrow.remove();
					deathEnemy(beetle, "beetle");
				}
			}
			
			//Flecha golpea a un keaton
			for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
				Keaton keaton = (Keaton)keatonActor;
				if(arrow.overlaps(keaton)) {
					arrowHitSound.play();
					arrow.remove();
					keaton.health -= 5;
					if(keaton.health <= 0) {
						deathEnemy(keaton, "keaton");
					}
					
				}
			}
			
			//Flecha golpea a un slime
			for(BaseActor slimeActor : BaseActor.getList(mainStage, Slime.class)) {
				
				Slime slime = (Slime)slimeActor;
				if(arrow.overlaps(slime)) {
					arrowHitSound.play();
					slime.setHurt(true);
					arrow.remove();
				}
			}
			
			//Flecha golpea a un solido
			for(BaseActor solid : BaseActor.getList(mainStage, Solid.class)) {
				if(arrow.overlaps(solid)) {
					arrow.preventOverlap(solid);
					arrow.setSpeed(0);
					arrow.addAction(Actions.fadeOut(0.5f));
					arrow.addAction(Actions.after(Actions.removeActor()));
				}
			}
			
		}
		
		//Interaccion con la bomba
		for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
			bomb.preventOverlap(player);
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				bomb.preventOverlap(beetle);
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				bomb.preventOverlap(keaton);
			}
		}
		
		//Interaccion con la explosion de la bomba
		for(BaseActor bombExp : BaseActor.getList(mainStage, BombExp.class)) {
			for(BaseActor rock : BaseActor.getList(mainStage, Rock.class)) {
				if(bombExp.isWithinDistance(0.05f, rock)) {
					boulderDestroyedSound.play();
					rock.remove();
					int random = MathUtils.random(0, 100);
					if(random < 50) {
						Rupee rupee = new Rupee(0, 0, mainStage, 1);
						rupee.centerAtActor(rock);
					}
					if(random >= 50 && random < 80) {
						Rupee rupee = new Rupee(0, 0, mainStage, 5);
						rupee.centerAtActor(rock);
					}
					if(random >= 80 && random < 90) {
						Rupee rupee = new Rupee(0, 0, mainStage, 20);
						rupee.centerAtActor(rock);
					}
					
				}
			}
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(bombExp.isWithinDistance(0.05f, beetle)) {
					beetle.remove();
					deathEnemy(beetle, "beetle");
				}
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				if(bombExp.isWithinDistance(0.05f, keaton)) {
					keaton.remove();
					deathEnemy(keaton, "keaton");
				}
			}
			
			for(BaseActor slime : BaseActor.getList(mainStage, Slime.class)) {
				if(bombExp.isWithinDistance(0.05f, slime)) {
					slime.remove();
					deathEnemy(slime, "slime");
				}
			}
		}
		
		/**
		 * Interaccion de la espada
		 */
		if(sword.isVisible()) {
			
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(sword.overlaps(beetle)) {
					deathEnemy(beetle, "beetle");
				}
			}
			
			for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {	
				Keaton keaton = (Keaton)keatonActor;
				if(sword.overlaps(keatonActor)) {
					keaton.health--;
					keaton.preventOverlap(sword);
					Vector2 swordPosition = new Vector2(sword.getX(), sword.getY());
					Vector2 keatonPosition = new Vector2(keaton.getX(), keaton.getY());
					Vector2 hitVector = swordPosition.sub(keatonPosition);
					keaton.setMotionAngle(hitVector.angle());
					if(keaton.health == 0) {
						deathEnemy(keaton, "keaton");
					}	
				}
			}
			
			for(BaseActor slimeActor : BaseActor.getList(mainStage, Slime.class)) {
				Slime slime = (Slime) slimeActor;
				if(sword.overlaps(slime)) {
					deathEnemy(slime, "slime");
				}
				
			}
		}	
		
		/**
		 * Valor de las rupias
		 */
		for(BaseActor rupeeActor : BaseActor.getList(mainStage, Rupee.class)) {
			Rupee rupia = (Rupee)rupeeActor;
			if(player.overlaps(rupeeActor)) {
				rupeeCollectedSound.play();
				if(rupia.getValor() == 1) {
					rupees++;
				}if(rupia.getValor() == 5) {
					rupees+=5;
				}if(rupia.getValor() == 20) {
					rupees+=20;
				}
				rupia.remove();
			}
		}
		
		/**
		 * Drop de flechas
		 */
		for(BaseActor arrowDropActor : BaseActor.getList(mainStage, ArrowDrop.class)) {
			if(player.overlaps(arrowDropActor)) {
				arrows += 3;
				arrowDropActor.remove();
			}
		}
	}

	/**
	 * Metodo que se encarga de varias cosas cuando se mata a un enemigo.
	 * 1 - Modifica la puntuacion dependiendo del enemigo que se a matado
	 * 2 - Spawnea un item aleatoriamente
	 * 3 - Aplica un efecto
	 * @param actor Actor
	 * @param type Nombre del Actor
	 */
	
	private void deathEnemy(BaseActor actor, String type) {
		actor.remove();
		
		if(type.equals("beetle")) {
			score += 25;	
		}else if(type.equals("keaton")) {
			score += 100;
		}else {
			score += 50;
		}
		
		int random = MathUtils.random(0, 100);
		if(random < 35) {
			Rupee rupee = new Rupee(0, 0, mainStage, 1);
			rupee.centerAtActor(actor);
		}
		if(random >= 35 && random < 50) {
			Rupee rupee = new Rupee(0, 0, mainStage, 5);
			rupee.centerAtActor(actor);
		}
		if(random >= 50 && random < 60) {
			Rupee rupee = new Rupee(0, 0, mainStage, 20);
			rupee.centerAtActor(actor);
		}
		if(random >= 60 && random < 90) {
			ArrowDrop ArrowDrop = new ArrowDrop(0, 0, mainStage);
			ArrowDrop.centerAtActor(actor);
		}
		enemyKilledSound.play();
		EnemyExp exp = new EnemyExp(0, 0, mainStage);
		exp.centerAtActor(actor);
	}
	
	/**
	 *  Metodo que le permite al jugador disparar flechas
	 */
	public void shootArrow() {
		if(arrows <= 0) {
			return;
		}
		arrows--;
		Arrow arrow = new Arrow(0, 0, mainStage);
		arrow.centerAtActor(player);
		arrow.setRotation(player.getFacingAngle());
		arrow.setMotionAngle(player.getFacingAngle());
		arrowShotSound.play();
	}

	/**
	 * Metodo que le permite al jugador poner bombas
	 */
	public void putBomb() {
		if(bombs <= 0) {
			return;
		}
		bombs--;
		player.setSpeed(0);
		
		Bomb bomb = new Bomb(0, 0, mainStage);

		float facingAngle = player.getFacingAngle();
		Vector2 offset = new Vector2();
		
		if(facingAngle == 0) {
			offset.set(0.80f, 0.1f);
		}else if(facingAngle == 90) {
			offset.set(0.13f, 0.8f);
		}else if(facingAngle == 270) {
			offset.set(0.07f, -0.4f);
		}else {
			offset.set(-0.50f, 0.1f);
		}
		
		bomb.setPosition(player.getX(), player.getY());
		bomb.moveBy(offset.x * player.getWidth(), offset.y * player.getHeight());
		
	}
	
	/**
	 * Metodo que le permite al jugador usar la espada
	 */
	public void swingSword() {
		if(sword.isVisible()) {
			return;
		}
		player.setSpeed(0);
		
		float facingAngle = player.getFacingAngle();
		Vector2 offset = new Vector2();
		
		if(facingAngle == 0) {
			offset.set(0.8f, 0.2f);
		}else if(facingAngle == 90) {
			offset.set(0.65f, 0.5f);
		}else if(facingAngle == 270) {
			offset.set(0.40f, 0.20f);
		}else {
			offset.set(0.24f, 0.2f);
		}
		
		sword.setPosition(player.getX(), player.getY());
		sword.moveBy(offset.x * player.getWidth(), offset.y * player.getHeight());
		
		float swordArc = 90;
		sword.setRotation(facingAngle - swordArc / 2);
		sword.setOriginX(0);
		
		sword.setVisible(true);
		sword.addAction(Actions.rotateBy(swordArc, 0.15f));
		sword.addAction(Actions.after(Actions.visible(false)));
		
		if(facingAngle == 90 || facingAngle == 180) {
			player.toFront();
		}else {
			sword.toFront();
		}
	}
	
	/**
	 * Metodo que dependiendo del boton presionado tendra un efecto u otro
	 */
	public boolean keyDown(int keyCode) {
		if(gameOver) {
			return false;
		}
		if(keyCode == Keys.A) {
			shootArrow();
		}
		if(keyCode == Keys.S) {
			swingSword();
			int aleatorio = MathUtils.random(1,3);
			if(aleatorio == 1) {
				swordSwungSound1.play();
			}if(aleatorio == 2) {
				swordSwungSound2.play();
			}else {
				swordSwungSound3.play();
			}
		}
		if(keyCode == Keys.D) {
			putBomb();
			putBombSound.play();
		}
		if(keyCode == Keys.B) {
			if(key!=null) {
				if(player.overlaps(key)) {
					if(rupees >= 25) {
						keys++;
						rupees -= 25;
						key.remove();
						arrowShop = new ArrowShop(key.getX(), key.getY() + 5, mainStage);
						player.toFront();
						key = null;
					}
				}
			}else {
				if(player.overlaps(arrowShop)) {
					if(rupees >= 10) {
						rupees -= 10;
						arrows += 5;
					}
				}
			}
			
		}

		return false;
	}
	
	/**
	 * Metodo que se encarga de cargar el mapa e iniciar todos los elementos del mismo
	 */
	private void mapInit() {
		TilemapActor tma = new TilemapActor("assets/maps/level1/map1.tmx", mainStage);
		
		for(MapObject obj : tma.getRectangleList("solid")) {
			MapProperties prop = obj.getProperties();
			new Solid((float)prop.get("x"), (float)prop.get("y"), (float)prop.get("width"), (float)prop.get("height"), mainStage);	
		}
		
		for(MapObject obj : tma.getRectangleList("beetle")) {
			MapProperties prop = obj.getProperties();
			Beetle beetle = new Beetle((float)prop.get("x"), (float)prop.get("y"), mainStage);
			beetle.setPosX((float)prop.get("x"));
			beetle.setPosY((float)prop.get("y"));
		}
		
		for(MapObject obj : tma.getRectangleList("rock")) {
			MapProperties prop = obj.getProperties();
			new Rock((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject keatonObj : tma.getRectangleList("keaton")) {
			MapProperties prop = keatonObj.getProperties();
			new Keaton((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject obj : tma.getRectangleList("npc")) {
			MapProperties prop = obj.getProperties();
			NPC npc = new NPC((float)prop.get("x"), (float)prop.get("y"), mainStage);
			npc.setID((String)prop.get("id"));
			npc.setText((String)prop.get("text"));
		}
		
		for(MapObject obj : tma.getRectangleList("slime")) {
			MapProperties prop = obj.getProperties();
			new Slime((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		MapObject keyObject = tma.getRectangleList("key").get(0);
		MapProperties props = keyObject.getProperties();
		key = new Key((float)props.get("x"), (float)props.get("y"), mainStage);
		
		MapObject chestObject = tma.getRectangleList("chest").get(0);
		props = chestObject.getProperties();
		chest = new Chest((float)props.get("x"), (float)props.get("y"), mainStage);	
		chest.setScale(0.8f);
		
		MapObject startPoint = tma.getRectangleList("start").get(0);
		props = startPoint.getProperties();
		player = new Player((float)props.get("x"), (float)props.get("y"), mainStage);

		sword = new Sword(0, 0, mainStage);
		sword.setVisible(false);
		sword.setScale(0.8f);
	}
	
	/**
	 * Metodo que se encarga de iniciar la musica y los sonidos.
	 */
	private void soundtrackInit() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/18 Deepwood Shrine.mp3"));
		backgroundMusic.setVolume(1f);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
		
		winMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/95 Get Small Item (Unused).mp3"));
		winMusic.setVolume(1f);
		winMusic.setLooping(false);
		
		arrowShotSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Arrow_Shoot.wav"));
		arrowHitSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Arrow_Hit.wav"));
		linkHitSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Hurt.wav"));
		swordSwungSound1 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Sword1.wav"));
		swordSwungSound2 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Sword2.wav"));
		swordSwungSound3 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Sword3.wav"));
		putBombSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Bomb_Drop.wav"));
		enemyKilledSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Enemy_Kill.wav"));
		rupeeCollectedSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Rupee.wav"));
		girlLaugh = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Unheard_Girl_Laugh4.wav"));
		boySound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Minish_Hey2.wav"));
		guardSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_CastleGuard_Hey.wav"));
		chestOpenSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Chest_Open.wav"));
		boulderDestroyedSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Boulder.wav"));
		keyBoughtSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Emote_Exclaim.wav"));
	}
	
	/**
	 * Metodo qu ese encarga de iniciar la interfaz del juego
	 */
	private void uiInit() {
		healthLabel = new Label(" x " + health, BaseGame.labelStyle);
		healthLabel.setColor(Color.PINK);
		healthLabel.setFontScale(0.75f);;
		rupeesLabel = new Label(" x " + rupees, BaseGame.labelStyle);
		rupeesLabel.setColor(Color.PINK);
		rupeesLabel.setFontScale(0.75f);;
		arrowsLabel = new Label(" x " + arrows, BaseGame.labelStyle);
		arrowsLabel.setColor(Color.PINK);
		arrowsLabel.setFontScale(0.75f);;
		bombsLabel = new Label(" x " + bombs, BaseGame.labelStyle);
		bombsLabel.setColor(Color.PINK);
		bombsLabel.setFontScale(0.75f);;
		keysLabel = new Label(" x " + keys, BaseGame.labelStyle);
		keysLabel.setColor(Color.PINK);
		keysLabel.setFontScale(0.75f);;
		messageLabel = new Label("...", BaseGame.labelStyle);
		messageLabel.setVisible(false);
		
		BaseActor healthIcon = new BaseActor(0, 0, uiStage);
		healthIcon.loadTexture("assets/sprites/objects/heart001.png");
		healthIcon.setScale(2.5f);
		BaseActor arrowIcon = new BaseActor(0, 0, uiStage);
		arrowIcon.loadTexture("assets/sprites/objects/arrow.png");
		arrowIcon.setScale(2.5f);
		BaseActor bombIcon = new BaseActor(0, 0, uiStage);
		bombIcon.loadTexture("assets/sprites/objects/bomb001.png");
		bombIcon.setScale(2.5f);
		BaseActor rupeesIcon = new BaseActor(0, 0, uiStage);
		rupeesIcon.loadTexture("assets/sprites/objects/rupeeGreen002.png");
		rupeesIcon.setScale(2.5f);
		BaseActor keysIcon = new BaseActor(0, 0, uiStage);
		keysIcon.loadTexture("assets/sprites/objects/key.png");
		keysIcon.setScale(2.2f);
		
		uiTable.pad(20);
		uiTable.add(healthIcon);
		uiTable.add(healthLabel);
		uiTable.add().expandX();
		
		uiTable.add(arrowIcon);
		uiTable.add(arrowsLabel);
		uiTable.add();
		uiTable.row();

		
		uiTable.add(rupeesIcon);
		uiTable.add(rupeesLabel);
		uiTable.add().expandX();
		
		uiTable.add(bombIcon);
		uiTable.add(bombsLabel);
		uiTable.row();
		
		uiTable.add(keysIcon);
		uiTable.add(keysLabel);
		uiTable.row();
		
		uiTable.add(messageLabel).colspan(8).expandX().expandY();
		uiTable.row();
		uiTable.add(dialogBox).colspan(8);

		dialogBox = new DialogBox(0, 0, uiStage);
		dialogBox.setBackgroundColor(Color.TAN);
		dialogBox.setFontColor(Color.WHITE);
		dialogBox.setDialogSize(250, 105);
		dialogBox.setFontScale(0.35f);
		dialogBox.alignCenter();
		dialogBox.setVisible(false);
	}

	/**
	 * Metodo que se encarga de iniciar la interfaz del menu de pause 
	 */
	private void openMenu() {
		
		TextButton resetButton = new TextButton("      Reset      ", BaseGame.textButtonStyle);
		uiTableMenu.add(resetButton);
		uiTableMenu.row();
		resetButton.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				uiTableMenu.clear();
				backgroundMusic.stop();
				DefinitelyNotZelda.setActiveScreen(new Level1());
				return false;
			}
		);
		
		TextButton back = new TextButton("Title Screen", BaseGame.textButtonStyle);
		uiTableMenu.add(back);
		uiTableMenu.row();
		back.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				backgroundMusic.stop();
				uiTableMenu.clear();
				DefinitelyNotZelda.setActiveScreen(new Menuscreen());
				return false;
			}
		);
		
		TextButton exit = new TextButton("        Exit        ", BaseGame.textButtonStyle);
		uiTableMenu.add(exit);
		uiTableMenu.row();
		exit.addListener((Event e) -> {
				if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
					return false;
				}
				uiTableMenu.clear();;
				Gdx.app.exit();
				return false;
			}
		);
	}
}
