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

import actors.Beetle;
import actors.Bomb;
import actors.BombDrop;
import actors.BombExp;
import actors.Bone;
import actors.DialogBox;
import actors.EnemyExp;
import actors.Fire;
import actors.Fireball;
import actors.Fireplace;
import actors.HeartDrop;
import actors.Keaton;
import actors.NPC;
import actors.Player;

import actors.Sign;
import actors.Skeleton;
import actors.SkeliHead;
import actors.Solid;
import actors.Spawn;
import actors.Sword;
import actors.TilemapActor;
import actors.Tornado;
import actors.Trophy;
import actors.Vaati;
import actors.Wall;
import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;
import online.Cliente;

/**
 * Tercero nivel del videojuego 
 * @author Roberto Ramiro Hernández
 *
 */
public class Level3 extends BaseScreen{

	Player player;
	Sword sword;
	Trophy trophy;
	
	int arrows;
	int rupees;
	int health;
	int bombs;
	int keys;
	int count;
	int litCount;
	int score; 
	
	//Timing
	private float timeSecondsTornado = 0f;
	private float periodTornado = 6.5f;
	private float timerTornado = 0f;
	private float timerPeriodTornado = 25f;
	private float timeSecondsSpawn = 0f;
	private float periodSpawn = 35f;
	private float timeSecondsFireball = 0f;
	private float periodFireBall = 7f;
	private float timeSecondsBone = 0f;
	private float periodBone = 1f;
	
	DialogBox dialogBox;
	boolean gameOver;
	boolean youWin;
	boolean paused;
	
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
	Sound zeldaHey;
	Sound skeliSound;
	Sound vaatiSound1;
	Sound vaatiSound2;
	Sound fireSound;
	Sound bossHitSound;
	Sound bossDefeatedSound;
	Sound secretSound;
	
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
		rupees = 25;
		score = 0;
		keys = 0;
		count = 0;
		litCount = 0;
		gameOver = false;
		youWin = false;
		
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
			if(Gdx.input.isKeyPressed(Keys.R)) {
				backgroundMusic.stop();
				DefinitelyNotZelda.setActiveScreen(new Level3());
			}
			return;
		}
		
		if(youWin) {
			backgroundMusic.stop();
			winMusic.play();
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				beetle.remove();
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				keaton.remove();
			}
			for(BaseActor skeleton : BaseActor.getList(mainStage, Skeleton.class)) {
				skeleton.remove();
			}
		}
		
		/**
		 * Game over
		 */
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
		
		 /**
		  * Actualizar UI
		  */
		healthLabel.setText(" x " + health);
		rupeesLabel.setText("   x " + rupees);
		arrowsLabel.setText("  x " + arrows);
		bombsLabel.setText("  x " + bombs);
		keysLabel.setText(" x " + keys);
		
		/**
		 * Invencibilidad boss
		 */
		if(litCount == count) { 
			for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
				Vaati vaati = (Vaati)vaatiActor;
				vaati.setInv(false);
				secretSound.play();
				count++;
			}
		}
		
		/**
		 * Controles
		 */
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
				
				for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
					Keaton keaton = (Keaton)keatonActor;
					keaton.setStopped(true);
				}
				
				for(BaseActor skeli : BaseActor.getList(mainStage, Skeleton.class)) {
					skeli.setSpeed(0);
					skeli.setAnimationPaused(true);
				}
				
				for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
					NPC npc = (NPC)npcActor;
					npc.setSpeed(0);
					npc.setViewing(false);
					npc.setAnimationPaused(true);
				}
				
				for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
					Vaati vaati = (Vaati)vaatiActor;
					vaati.setSpeed(0);
					vaati.setAnimationPaused(true);
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

				for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
					Keaton keaton = (Keaton)keatonActor;
					keaton.setStopped(false);
				}
				
				for(BaseActor skeli : BaseActor.getList(mainStage, Skeleton.class)) {
					skeli.setAnimationPaused(false);
					skeli.setSpeed(30);
				}
				
				for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
					NPC npc = (NPC)npcActor;
					npc.setSpeed(0);
					npc.setViewing(false);
					npc.setAnimationPaused(false);
				}
				
				for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
					Vaati vaati = (Vaati)vaatiActor;
					vaati.setAnimationPaused(false);
				}
				
			}

		}
		
		/**
		 * Final
		 */
		for(BaseActor endPoint : BaseActor.getList(mainStage, Trophy.class)) { 
			if(player.overlaps(endPoint)) {
				if(youWin) {
					winMusic.stop();
					if(Menuscreen.cliente.getSocket() != null) {
						cliente = Menuscreen.cliente;
						try {
							
							File file = new File("assets/data/info.txt");
							
							if(file.exists()) {
								BufferedReader br = new BufferedReader(new FileReader(file));
								
								String datos = br.readLine();
								String[] data = datos.split("-");
								
								int scor = Integer.parseInt(data[2]);
								score += scor;
								BufferedWriter bw = new BufferedWriter(new FileWriter(file));
								bw.write(data[0] + "-" + data[1] + "-" + score);
								
								cliente.enviarData(data[0], data[1], score);
								br.close();
								bw.close();
							}
							
						} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException
								| IllegalBlockSizeException | IOException e) {}
					}
					DefinitelyNotZelda.setActiveScreen(new TheEnd());
				}
			
			}
		}
		
		/**
		 * Interaccion con los solidos;
		 */
		for(BaseActor solid : BaseActor.getList(mainStage, Solid.class)) {
			player.preventOverlap(solid);
			float angulo = MathUtils.random(135, 225);
			
			//Fuego
			for(BaseActor fire : BaseActor.getList(mainStage, Fire.class)) {
				if(fire.overlaps(solid)) {
					fire.remove();
				}
			}
			
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
			
			//Enemigo Skeleton
			for(BaseActor skeleton : BaseActor.getList(mainStage, Skeleton.class)) {
				skeleton.preventOverlap(solid);
				if(skeleton.isWithinDistance(0.1f, solid)) {
					skeleton.setMotionAngle(skeleton.getMotionAngle() + angulo);
				}
			}
			
			//Bombas del player
			for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
				bomb.preventOverlap(solid);
			}
			
		}
		
		/**
		 * Interaccion con las paredes
		 */
		for(BaseActor wall : BaseActor.getList(mainStage, Wall.class)) {
			player.preventOverlap(wall);
			float angulo = MathUtils.random(135, 225);
			
			for(BaseActor vaati : BaseActor.getList(mainStage, Vaati.class)) {
				vaati.preventOverlap(wall);
				if(vaati.isWithinDistance(0.1f, wall)) {
					vaati.setMotionAngle(vaati.getMotionAngle() + angulo);
				}
			}
			
			for(BaseActor fire : BaseActor.getList(mainStage, Fire.class)) {
				if(fire.overlaps(wall)) {
					fire.remove();
				}
			}
			
			//Enemigo Beetle
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				beetle.preventOverlap(wall);
				if(beetle.isWithinDistance(0.01f, wall)) {
					beetle.setMotionAngle(beetle.getMotionAngle() + angulo);
				}
			}
			
			//Enemigo Keaton
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				keaton.preventOverlap(wall);
				if(keaton.isWithinDistance(0.01f, wall)) {
					keaton.setMotionAngle(keaton.getMotionAngle() + angulo);
				}
			}
			
			//Enemigo Skeleton
			for(BaseActor skeleton : BaseActor.getList(mainStage, Skeleton.class)) {
				skeleton.preventOverlap(wall);
				if(skeleton.isWithinDistance(0.1f, wall)) {
					skeleton.setMotionAngle(skeleton.getMotionAngle() + angulo);
				}
			}
			
			//Bombas del player
			for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
				bomb.preventOverlap(wall);
			}
			
			for(BaseActor fireballActor : BaseActor.getList(mainStage, Fireball.class)) {
				Fireball fireball = (Fireball)fireballActor;
				if(fireball.overlaps(wall)) {
					fireball.preventOverlap(wall);
					fireball.setGrow(true);	
				}
			}
		}
		
		/**
		 * Interaccion con las antorchas
		 */
		for(BaseActor fireActor : BaseActor.getList(mainStage,  Fireplace.class)) {
			player.preventOverlap(fireActor);
			Fireplace fireplace = (Fireplace)fireActor;
			
			float angulo = MathUtils.random(135, 225);
			
			//Enemigo Beetle
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				beetle.preventOverlap(fireActor);
				if(beetle.isWithinDistance(0.01f, fireActor)) {
					beetle.setMotionAngle(beetle.getMotionAngle() + angulo);
				}
			}
			
			//Enemigo Keaton
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				keaton.preventOverlap(fireActor);
				if(keaton.isWithinDistance(0.01f, fireActor)) {
					keaton.setMotionAngle(keaton.getMotionAngle() + angulo);
				}
			}
			
			//Enemigo Skeleton
			for(BaseActor skeleton : BaseActor.getList(mainStage, Skeleton.class)) {
				skeleton.preventOverlap(fireActor);
				if(skeleton.isWithinDistance(0.1f, fireActor)) {
					skeleton.setMotionAngle(skeleton.getMotionAngle() + angulo);
				}
			}	
			
			//Ataque Boss
			for(BaseActor fireball : BaseActor.getList(mainStage, Fireball.class)) {
				if(fireball.overlaps(fireplace)) {
					if(!fireplace.isLit()) {
						fireball.remove();
						fireplace.setLit(true);
						litCount++;
					}
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
		 * Interaccion con el enemigo Skeleton
		 */
		for(BaseActor skeletonActor : BaseActor.getList(mainStage, Skeleton.class)) { 
			
			Skeleton skeleton = (Skeleton)skeletonActor;
			player.preventOverlap(skeleton);
			
			if(player.isWithinDistance(75, skeleton)) {
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 skeletonPosition = new Vector2(skeleton.getX(), skeleton.getY());
				Vector2 distance = playerPosition.sub(skeletonPosition);
				skeleton.setMotionAngle(distance.angle() + 180);
				
				timeSecondsBone += Gdx.graphics.getRawDeltaTime();
			    if(timeSecondsBone > periodBone){
			        timeSecondsBone-=periodBone;
			        skeleton.throwBone();
			    }
			}
		}
		
		/**
		 * Interaccion con los npcs
		 */
		for(BaseActor npcActor : BaseActor.getList(mainStage, NPC.class)) {
			NPC npc = (NPC)npcActor;
			player.preventOverlap(npc);
			
			boolean nearby = player.isWithinDistance(2, npc);
			
			if(nearby && !npc.isViewing()) {
				
				if(npc.getID().equals("zelda")) {
					zeldaHey.play();
					dialogBox.setText(npc.getText());
				}else {
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
		
		/**
		 * Interaccion con el cartel
		 */
		for(BaseActor signActor : BaseActor.getList(mainStage, Sign.class)) {
			Sign sign = (Sign) signActor;
			player.preventOverlap(sign);
			boolean nearby = player.isWithinDistance(2, sign);
			if(nearby && !sign.isViewing()) {
				dialogBox.setText(sign.getText());
				dialogBox.setVisible(true);
				sign.setViewing(true);
			}
			if(sign.isViewing() && !nearby) {
				dialogBox.setText("");
				dialogBox.setVisible(false);
				sign.setViewing(false);
			}
		}
		
		/**
		 * IA BOSS
		 */
		for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
			Vaati vaati = (Vaati)vaatiActor;
			
			if(vaati.health <= 0) {
				vaati.setAggro(false);
				vaati.remove();
				EnemyExp exp = new EnemyExp(0, 0, mainStage);
				exp.setScale(1.5f);
				exp.centerAtActor(vaati);
				youWin = true;
				trophy.setVisible(true);
			}
			
			if(player.overlaps(vaati)) {
				health--;
				linkHitSound.play();
				Vector2 playerPos = new Vector2(player.getX(), player.getY());
				Vector2 vaatiPos = new Vector2(vaati.getX(), vaati.getY());
				Vector2 hitVector = playerPos.sub(vaatiPos);
				player.setMotionAngle(hitVector.angle());
				player.setSpeed(500);
				vaati.setMotionAngle(hitVector.angle() + 180);
			}
			

			if(player.isWithinDistance(80, vaati)) {
				if(!vaati.isTrans() && !vaati.isAggro()) {				
					vaatiSound1.play();
					vaati.setTrans(true);
					
				}
			}

			if(vaati.isAggro() && !paused) {
				
				//Ataque: Tornado
				timerTornado += Gdx.graphics.getRawDeltaTime();
			    if(timerTornado > timerPeriodTornado){
			    	timerTornado -= timerPeriodTornado;
			    	vaatiSound1.play();
			        vaati.createTornado();
			    }
				
			    //Ataque: Fireball
			    timeSecondsFireball += Gdx.graphics.getRawDeltaTime();
			    if(timeSecondsFireball > periodFireBall){
			    	timeSecondsFireball -= periodFireBall;
			    	vaatiSound2.play();
			    	Vector2 playerPos = new Vector2(player.getX(), player.getY());
					Vector2 vaatiPos = new Vector2(vaati.getX(), vaati.getY());
					Vector2 hitVector = playerPos.sub(vaatiPos);
					if(vaati.health < 350) {
						vaati.throwFireball(hitVector.angle());
						vaati.throwFireball(hitVector.angle() + 25);
						vaati.throwFireball(hitVector.angle() - 25);
					}else {
						vaati.throwFireball(hitVector.angle());
					}
			    }
			    
			    /**
				 * Spawn
				 */
				for(BaseActor spawnActor : BaseActor.getList(mainStage, Spawn.class)) {
					if(!youWin) {
						timeSecondsSpawn += Gdx.graphics.getRawDeltaTime();
					    if(timeSecondsSpawn > periodSpawn){
					    	timeSecondsSpawn -= periodSpawn;
					    	spawn(spawnActor);
					    }
					}else {
						spawnActor.remove();
					}
				}
			}
			
			
		}
		
		/**
		 * Interaccion con las flechas
		 */
		
		for(BaseActor arrow : BaseActor.getList(mainStage, Arrow.class)) {
			for(BaseActor solid : BaseActor.getList(mainStage, Solid.class)) {
				if(arrow.overlaps(solid)) {
					arrow.preventOverlap(solid);
					arrow.setSpeed(0);
					arrow.addAction(Actions.fadeOut(0.5f));
					arrow.addAction(Actions.after(Actions.removeActor()));
				}
			}
			
			for(BaseActor wall : BaseActor.getList(mainStage, Wall.class)) {
				if(arrow.overlaps(wall)) {
					arrow.preventOverlap(wall);
					arrow.setSpeed(0);
					arrow.addAction(Actions.fadeOut(0.5f));
					arrow.addAction(Actions.after(Actions.removeActor()));
				}
			}
			
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
			
			//Flecha que golpea a un skeleto
			for(BaseActor skeletonActor : BaseActor.getList(mainStage, Skeleton.class)) {
				Skeleton skeleton = (Skeleton) skeletonActor;
				if(arrow.overlaps(skeleton)) {
					arrow.remove();
					arrowHitSound.play();
					skeleton.health -= 5;
					if(skeleton.health <= 0) {
						deathEnemy(skeleton, "skeleton");
					}
				}
			}
			
			//Flecha golpea un hueso
			for(BaseActor boneActor : BaseActor.getList(mainStage, Bone.class)) {
				
				if(arrow.overlaps(boneActor)) {
					boneActor.remove();
					arrow.remove();
				}
			}
			
			for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
				Vaati vaati = (Vaati)vaatiActor;
				if(arrow.overlaps(vaati)) {
					if(vaati.isInv()) {
						vaatiSound2.play();
					}else {
						vaati.health -= 5;
						bossHitSound.play();
					}
					arrowHitSound.play();
					arrow.remove();
				}
			}
		}
		
		/**
		 * Interaccion con el fuego del jugador
		 */
		for(BaseActor fire : BaseActor.getList(mainStage, Fire.class)) {
			//Fuego golpea a la antorcha
			for(BaseActor fireplaceActor : BaseActor.getList(mainStage, Fireplace.class)) {
				Fireplace fireplace = (Fireplace)fireplaceActor;
				if(fire.overlaps(fireplace)) {
					if(!fireplace.isLit()) {
						fireplace.setLit(true);
						litCount++;
					}
					fire.remove();
				}
			}
			
			//Fuego golpea al boss
			for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
				Vaati vaati = (Vaati)vaatiActor;
				if(fire.overlaps(vaati)) {
					vaatiSound1.play();
					fire.remove();
				}
			}
			
			//Fuego golpea a un beetle
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(fire.overlaps(beetle)) {
					fire.remove();
					deathEnemy(beetle, "beetle");
				}
			}
			
			//Fuego golpea a un keaton
			for(BaseActor keatonActor : BaseActor.getList(mainStage, Keaton.class)) {
				Keaton keaton = (Keaton)keatonActor;
				if(fire.overlaps(keaton)) {
					fire.remove();
					keaton.health -= 1;
					if(keaton.health <= 0) {
						deathEnemy(keaton, "keaton");
					}
					
				}
			}
			
			//Fuego que golpea a un skeleto
			for(BaseActor skeletonActor : BaseActor.getList(mainStage, Skeleton.class)) {
				Skeleton skeleton = (Skeleton) skeletonActor;
				if(fire.overlaps(skeleton)) {
					fire.remove();
					skeleton.health -= 1;
					if(skeleton.health <= 0) {
						deathEnemy(skeleton, "skeleton");
					}
				}
			}
		}
	
		/**
		 * Interaccion con la bomba
		 */
		for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
			bomb.preventOverlap(player);
			
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				bomb.preventOverlap(beetle);
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				bomb.preventOverlap(keaton);
			}
			for(BaseActor skeleton : BaseActor.getList(mainStage, Skeleton.class)) {
				bomb.preventOverlap(skeleton);
			}
			
		}
		
		/**
		 * Interaccion con la explosion de la bomba
		 */
		for(BaseActor bombExp : BaseActor.getList(mainStage, BombExp.class)) {
			for(BaseActor fireActor : BaseActor.getList(mainStage,  Fireplace.class)) {
				Fireplace fire = (Fireplace)fireActor;
				if(bombExp.isWithinDistance(2, fire)) {
					fire.setLit(true);
				}
			}
			
			for(BaseActor skeli : BaseActor.getList(mainStage, SkeliHead.class)) {
				if(bombExp.isWithinDistance(0.05f, skeli)) {
					skeliSound.play();
					skeli.remove();
					int random = MathUtils.random(0, 100);
					if(random < 50) {
						ArrowDrop drop = new ArrowDrop(0, 0, mainStage);
						drop.centerAtActor(skeli);
					}
					if(random >= 50 && random < 80) {
						HeartDrop drop = new HeartDrop(0, 0, mainStage);
						drop.centerAtActor(skeli);
					}
					if(random >= 80 && random < 90) {
						BombDrop drop = new BombDrop(0, 0, mainStage);
						drop.centerAtActor(skeli);
					}
				}
			}
			
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(bombExp.isWithinDistance(0.05f, beetle)) {
					deathEnemy(beetle, "beetle");
				}
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				if(bombExp.isWithinDistance(0.05f, keaton)) {
					deathEnemy(keaton, "keaton");
				}
			}
			for(BaseActor skeleton : BaseActor.getList(mainStage, Skeleton.class)) {
				if(bombExp.isWithinDistance(0.05f, skeleton)) {
					deathEnemy(skeleton, "skeleton");
				}
			}
			
			for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
				Vaati vaati = (Vaati)vaatiActor;
				if(bombExp.isWithinDistance(0.1f, vaati)) {
					if(vaati.isInv()) {
						vaatiSound2.play();
					}else {
						vaati.health -= 20;
						bossHitSound.play();
					}
				}
			}
			
		}
		
		/**
		 * Interaccion con la espada
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
			
			for(BaseActor skeletonActor : BaseActor.getList(mainStage, Skeleton.class)) {
				Skeleton skeleton = (Skeleton) skeletonActor;
				if(sword.overlaps(skeleton)) {	
					skeleton.health--;
					skeleton.preventOverlap(sword);
					Vector2 swordPosition = new Vector2(sword.getX(), sword.getY());
					Vector2 skeletonPosition = new Vector2(skeleton.getX(), skeleton.getY());
					Vector2 hitVector = swordPosition.sub(skeletonPosition);
					skeleton.setMotionAngle(hitVector.angle());
					if(skeleton.health == 0) {
						deathEnemy(skeleton, "skeleton");
					}
				}
			}
			
			for(BaseActor boneActor : BaseActor.getList(mainStage, Bone.class)) {
				if(sword.overlaps(boneActor)) {
					boneActor.remove();
				}
			}
			
			for(BaseActor skeli : BaseActor.getList(mainStage, SkeliHead.class)) {
				if(sword.overlaps(skeli)) {
					skeliSound.play();
					skeli.remove();
					int random = MathUtils.random(0, 100);
					if(random < 50) {
						ArrowDrop drop = new ArrowDrop(0, 0, mainStage);
						drop.centerAtActor(skeli);
					}
					if(random >= 50 && random < 80) {
						HeartDrop drop = new HeartDrop(0, 0, mainStage);
						drop.centerAtActor(skeli);
					}
					if(random >= 80 && random < 90) {
						BombDrop drop = new BombDrop(0, 0, mainStage);
						drop.centerAtActor(skeli);
					}
				}
			}
			
			for(BaseActor vaatiActor : BaseActor.getList(mainStage, Vaati.class)) {
				Vaati vaati = (Vaati)vaatiActor;
				if(sword.overlaps(vaati)){
					if(vaati.isInv()) {
						vaatiSound1.play();
					}else {
						vaati.health -= 20;
						bossHitSound.play();
					}
				}
			}
		}
		
		/**
		 * Interaccion con el tornado
		 */
		for(BaseActor tornadoActor : BaseActor.getList(mainStage, Tornado.class)) {
			Tornado tornado = (Tornado) tornadoActor;
			
			if(tornado.isHurt()) {
				Vector2 playerPos = new Vector2(player.getX(), player.getY());
				Vector2 tornadoPos = new Vector2(tornado.getX(), tornado.getY());
				Vector2 hitVector = playerPos.sub(tornadoPos);
				tornado.setMotionAngle(hitVector.angle());
			}
			
			if(player.overlaps(tornado)) {
				if(tornado.isHurt()) {
					health--;
					linkHitSound.play();
					Vector2 playerPos = new Vector2(player.getX(), player.getY());
					Vector2 tornadoPos = new Vector2(tornado.getX(), tornado.getY());
					Vector2 hitVector = playerPos.sub(tornadoPos);
					player.setMotionAngle(hitVector.angle());
					player.setSpeed(300);
				}
			}
			
			timeSecondsTornado += Gdx.graphics.getRawDeltaTime();
		    if(timeSecondsTornado > periodTornado){
		    	timeSecondsTornado -= periodTornado;
		        tornado.remove();
		    }
		}
		
		/**
		 * Ataque fuego boss 
		 */
		for(BaseActor fireballActor : BaseActor.getList(mainStage, Fireball.class)) {
			Fireball fireball = (Fireball)fireballActor;
			
			if(player.overlaps(fireball)) {
				health--;
				linkHitSound.play();
				fireball.remove();
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
		
		/**
		 * Drop de bombas
		 */
		for(BaseActor bombDropActor : BaseActor.getList(mainStage, BombDrop.class)) {
			if(player.overlaps(bombDropActor)) {
				bombs += 2;
				bombDropActor.remove();
			}
		}
		
		/**
		 * Drop de corazones
		 */
		for(BaseActor heartDropActor : BaseActor.getList(mainStage, HeartDrop.class)) {
			if(player.overlaps(heartDropActor)) {
				health++;
				heartDropActor.remove();
			}
		}
		
		
	}

	/**
	 * Metodo que se encarga de spawnear enemigos en los puntos asignados
	 * @param actor
	 */
	private void spawn(BaseActor actor) {
		int random = MathUtils.random(0, 100);
    	if(random < 35) {
    		new Keaton(actor.getX(), actor.getY(), mainStage);
    	}else if(random >= 35 && random < 70) {
    		new Skeleton(actor.getX(), actor.getY(), mainStage);
    	}else {
    		new Beetle(actor.getX(), actor.getY(), mainStage);
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
		}else if(type.equals("skeleton")){
			score += 150;
		}else {
			score += 25;
		}
		int random = MathUtils.random(0, 100);
		if(random < 55) {
			HeartDrop heart = new HeartDrop(0, 0, mainStage);
			heart.centerAtActor(actor);
		}
		if(random >= 55 && random < 70) {
			BombDrop bomb = new BombDrop(0, 0, mainStage);
			bomb.centerAtActor(actor);
		}
		if(random >= 70 && random <= 100) {
			ArrowDrop arrow = new ArrowDrop(0, 0, mainStage);
			arrow.centerAtActor(actor);
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
	 * Metodo que le permite al jugador disparar fuego
	 */
	public void shootFire() {
		Fire fire = new Fire(0, 0, mainStage);
		fire.centerAtActor(player);
		fire.setMotionAngle(player.getFacingAngle());
		fireSound.play();
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
		if(keyCode == Keys.F) {
			shootFire();
		}

		return false;
	}
		
	/**
	 * Metodo que se encarga de cargar el mapa e iniciar todos los elementos del mismo
	 */
	private void mapInit() {
		TilemapActor tma = new TilemapActor("assets/maps/level3/level3.tmx", mainStage);
		
		for(MapObject obj : tma.getRectangleList("solid")) {
			MapProperties prop = obj.getProperties();
			new Solid((float)prop.get("x"), (float)prop.get("y"), (float)prop.get("width"), (float)prop.get("height"), mainStage);	
		}

		for(MapObject obj : tma.getRectangleList("wall")) {
			MapProperties prop = obj.getProperties();
			new Wall((float)prop.get("x"), (float)prop.get("y"), (float)prop.get("width"), (float)prop.get("height"), mainStage);	
		}
		
		for(MapObject obj : tma.getRectangleList("npc")){
			MapProperties prop = obj.getProperties();
			NPC npc = new NPC((float)prop.get("x"), (float)prop.get("y"), mainStage);
			npc.setID((String)prop.get("id"));
			npc.setText((String)prop.get("text"));
		}
		
		for(MapObject obj : tma.getRectangleList("fire")){
			MapProperties prop = obj.getProperties();
			new Fireplace((float)prop.get("x"), (float)prop.get("y"), mainStage);
			count++;
		}
		
		for(MapObject obj : tma.getRectangleList("fire2")){
			MapProperties prop = obj.getProperties();
			Fireplace fire = new Fireplace((float)prop.get("x"), (float)prop.get("y"), mainStage);
			fire.setLit(true);
			count++;
			litCount++;
		}
		
		for(MapObject obj : tma.getRectangleList("rock")){
			MapProperties prop = obj.getProperties();
			new SkeliHead((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject obj: tma.getRectangleList("spawn")) {
			MapProperties prop = obj.getProperties();
			new Spawn((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		MapObject startPoint = tma.getRectangleList("start").get(0);
		MapProperties props = startPoint.getProperties();
		player = new Player((float)props.get("x"), (float)props.get("y"), mainStage);
		
		MapObject vaati = tma.getRectangleList("vaati").get(0);
		props = vaati.getProperties();
		new Vaati((float)props.get("x"), (float)props.get("y"), mainStage);
		
		MapObject signObject = tma.getRectangleList("sign").get(0);
		props = signObject.getProperties();
		Sign sign = new Sign((float)props.get("x"), (float)props.get("y"), mainStage);
		sign.setText("Ahora posees el bastón de fuego.\nPulsa F para usarlo.\nQuizas te ayude en tu batalla con Vaati.");
		
		MapObject endPoint = tma.getRectangleList("end").get(0);
		props = endPoint.getProperties();
		trophy = new Trophy((float)props.get("x"), (float)props.get("y"), mainStage);
		trophy.setVisible(false);
		
		sword = new Sword(0, 0, mainStage);
		sword.setVisible(false);
		sword.setScale(0.8f);
	}
		
	/**
	 * Metodo que se encarga de iniciar la musica y los sonidos.
	 */
	private void soundtrackInit() {
		
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/52 Vaati Battle.mp3"));
		backgroundMusic.setVolume(1f);
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		winMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/53 Zelda Awakens.mp3"));
		winMusic.setVolume(1f);
		winMusic.setLooping(true);
		
		arrowShotSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Arrow_Shoot.wav"));
		arrowHitSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Arrow_Hit.wav"));
		linkHitSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Hurt.wav"));
		swordSwungSound1 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Sword1.wav"));
		swordSwungSound2 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Sword2.wav"));
		swordSwungSound3 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Link_Sword3.wav"));
		putBombSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Bomb_Drop.wav"));
		enemyKilledSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Enemy_Kill.wav"));
		rupeeCollectedSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Rupee.wav"));
		zeldaHey = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Zelda_Hey.wav"));
		skeliSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Boulder.wav"));
		vaatiSound1 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Vaati_Laugh.wav"));
		vaatiSound2 = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Vaati_Laugh2.wav"));
		fireSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_FlameLantern_On.wav"));
		bossDefeatedSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Boss_Kill.wav"));
		bossHitSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Boss_Hit.wav"));
		secretSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/converted/converted_MC_Secret.wav"));
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
		dialogBox.setDialogSize(350, 105);
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
				DefinitelyNotZelda.setActiveScreen(new Level3());
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
	}}
