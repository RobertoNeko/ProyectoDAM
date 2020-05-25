package screens;


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
import actors.Bat;
import actors.Beetle;
import actors.Bomb;
import actors.BombDrop;
import actors.BombExp;
import actors.DialogBox;
import actors.End;
import actors.EnemyExp;
import actors.HeartDrop;
import actors.Keaton;
import actors.Nokken;
import actors.Plant;
import actors.Player;
import actors.Skeleton;
import actors.Solid;
import actors.Sword;
import actors.TilemapActor;
import base.BaseActor;
import base.BaseGame;
import base.BaseScreen;
import main.DefinitelyNotZelda;

/**
 * Nivel extra del videojuego 
 * @author Roberto Ramiro Hernaández
 *
 */
public class ExtraLevel1 extends BaseScreen{

	Player player;
	Sword sword;
	
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
	
	//UI
	Label healthLabel;
	Label rupeesLabel;
	Label arrowsLabel;
	Label bombsLabel;
	Label messageLabel;
	Label keysLabel;
	
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
	Sound secretSound;
	
	/**
	 * Se inician los valores 
	 */
	@Override
	public void initialize() {
		
		arrows = 7;
		health = 5;
		bombs = 3;
		rupees = 25;
		keys = 0;
		
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
				DefinitelyNotZelda.setActiveScreen(new ExtraLevel1());
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
			for(BaseActor bat : BaseActor.getList(mainStage, Bat.class)) {
				bat.remove();
			}
			for(BaseActor nokken : BaseActor.getList(mainStage, Nokken.class)) {
				nokken.remove();
			}
			
			DefinitelyNotZelda.setActiveScreen(new ExtraLevel2());
			
			return;
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
				
				for(BaseActor batActor : BaseActor.getList(mainStage, Bat.class)) {
					Bat bat = (Bat)batActor;
					bat.setSpeed(0);
					bat.setAnimationPaused(true);
				}
				for(BaseActor nokken : BaseActor.getList(mainStage, Nokken.class)) {
					nokken.setSpeed(0);
					nokken.setAnimationPaused(true);
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
				
				for(BaseActor batActor : BaseActor.getList(mainStage, Bat.class)) {
					Bat bat = (Bat)batActor;
					if(bat.isAggro()) {
						bat.setSpeed(50);
					}
					bat.setAnimationPaused(false);
				}
				for(BaseActor nokkenActor : BaseActor.getList(mainStage, Nokken.class)) {
					Nokken nokken = (Nokken)nokkenActor;
					nokken.setAnimationPaused(false);
					if(nokken.isAggro()) {
						nokken.setSpeed(30);
					}else {
						nokken.setSpeed(10);
					}
				}
				
			}

		}
		
		/**
		 * Interaccion con endPoint
		 */
		for(BaseActor endPoint : BaseActor.getList(mainStage, End.class)) {
			if(player.overlaps(endPoint)) {
				youWin = true;
			}
		}
		
		
		/**
		 * Interaccion con los solidos;
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
			
			//Enemigo Nokken
			for(BaseActor nokken : BaseActor.getList(mainStage, Nokken.class)) {
				nokken.preventOverlap(solid);
				if(nokken.isWithinDistance(0.01f, solid)) {
					nokken.setMotionAngle(nokken.getMotionAngle() + angulo);
				}
			}
				
			//Bombas del player
			for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
				bomb.preventOverlap(solid);
			}
		}
		
		/**
		 * Interaccion con las plantas
		 */
		for(BaseActor plantActor : BaseActor.getList(mainStage, Plant.class)) {
			Plant plant = (Plant)plantActor;
			if(!plant.isCut()) {
				player.preventOverlap(plant);
				
				for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
					beetle.preventOverlap(plant);
				}
				for(BaseActor bomb : BaseActor.getList(mainStage, Bomb.class)) {
					bomb.preventOverlap(plant);
				}

				for(BaseActor nokken : BaseActor.getList(mainStage, Nokken.class)) {
					nokken.preventOverlap(plant);
				}
				for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
					keaton.preventOverlap(plant);
				}
			}
		}
		
		/**
		 * IA beetle
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
		 * IA bat
		 */
		for(BaseActor batActor : BaseActor.getList(mainStage, Bat.class)) {
			Bat bat = (Bat)batActor;
			
			if(paused) {
				bat.setSpeed(0);
				bat.setAggro(false);
			}
			
			if(player.isWithinDistance(60f, bat) && !paused) {
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 batPosition = new Vector2(bat.getX(), bat.getY());
				Vector2 hitVector = playerPosition.sub(batPosition);
				bat.setMotionAngle(hitVector.angle());
				bat.setAggro(true);
			}
			
			if(player.overlaps(bat)) {
				linkHitSound.play();
				health--;
				player.preventOverlap(bat);
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 beetlePosition = new Vector2(bat.getX(), bat.getY());
				Vector2 hitVector = playerPosition.sub(beetlePosition);
				player.setMotionAngle(hitVector.angle());
				player.setSpeed(300);
			}
		}
		
		/**
		 * IA Nokken
		 */
		for(BaseActor nokkenActor : BaseActor.getList(mainStage, Nokken.class)) {
			Nokken nokken = (Nokken)nokkenActor;
			if(player.isWithinDistance(40f, nokken)) {
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 nokkenPosition = new Vector2(nokken.getX(), nokken.getY());
				Vector2 hitVector = playerPosition.sub(nokkenPosition);
				nokken.setMotionAngle(hitVector.angle());
				nokken.setAggro(true);
			}else {
				if(!nokken.isAggro() || !nokken.isVul()) {
					nokken.setVul(false);
					nokken.setAggro(false);
				}
			}
			
			if(player.overlaps(nokken)) {
				health--;
				linkHitSound.play();
				player.preventOverlap(nokken);
				Vector2 playerPosition = new Vector2(player.getX(), player.getY());
				Vector2 keatonPosition = new Vector2(nokken.getX(), nokken.getY());
				Vector2 hitVector = playerPosition.sub(keatonPosition);
				player.setMotionAngle(hitVector.angle());
				player.setSpeed(100);
			}
			
		}
		
		/**
		 * IA keaton
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
			
			//Flecha golpea a un bat
			for(BaseActor bat : BaseActor.getList(mainStage, Bat.class)) {
				if(arrow.overlaps(bat)) {
					arrow.remove();
					arrowHitSound.play();
					deathEnemy(bat);
				}
			}
			
			//Flecha golpea a un beetle
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(arrow.overlaps(beetle)) {
					arrowHitSound.play();
					arrow.remove();
					deathEnemy(beetle);
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
						deathEnemy(keaton);
					}
					
				}
			}
			
			//Flecha golpea a un nokken
			for(BaseActor nokkenActor : BaseActor.getList(mainStage, Nokken.class)) {
				Nokken nokken = (Nokken)nokkenActor;
				if(arrow.overlaps(nokken)) {
					if(nokken.isVul()) {
						arrowHitSound.play();
						arrow.remove();
						deathEnemy(nokken);
					}else {
						arrowHitSound.play();
						arrow.remove();
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
			for(BaseActor nokken : BaseActor.getList(mainStage, Nokken.class)) {
				bomb.preventOverlap(nokken);
			}
		}
		
		/**
		 * Interaccion con la explosion de la bomba
		 */
		
		for(BaseActor bombExp : BaseActor.getList(mainStage, BombExp.class)) {
			
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(bombExp.isWithinDistance(0.05f, beetle)) {
					deathEnemy(beetle);
				}
			}
			for(BaseActor keaton : BaseActor.getList(mainStage, Keaton.class)) {
				if(bombExp.isWithinDistance(0.05f, keaton)) {
					deathEnemy(keaton);
				}
			}
			
			for(BaseActor plantActor : BaseActor.getList(mainStage, Plant.class)) {
				Plant plant = (Plant)plantActor;
				if(bombExp.isWithinDistance(0.1f, plant)) {
					plant.setCut(true);
				}
			}
			
			for(BaseActor batActor : BaseActor.getList(mainStage, Bat.class)) {
				Bat bat = (Bat)batActor;
				if(bombExp.isWithinDistance(0.1f, bat)) {
					deathEnemy(bat);
				}
			}
			
			for(BaseActor nokkenActor : BaseActor.getList(mainStage, Nokken.class)) {
				Nokken nokken = (Nokken)nokkenActor;
				if(bombExp.isWithinDistance(0.1f, nokken)) {
					deathEnemy(nokken);
				}
			}
			
		}
		
		/**
		 * Espada
		 */
		if(sword.isVisible()) {
			
			for(BaseActor plantActor : BaseActor.getList(mainStage, Plant.class)) {
				Plant plant = (Plant)plantActor;
				if(sword.overlaps(plant)) {
					plant.setCut(true);	
				}
			}
			
			for(BaseActor batActor : BaseActor.getList(mainStage, Bat.class)) {
				Bat bat = (Bat)batActor;
				if(sword.overlaps(bat)) {
					deathEnemy(bat);
				}
			}
			
			for(BaseActor nokkenActor : BaseActor.getList(mainStage, Nokken.class)) {
				Nokken nokken = (Nokken)nokkenActor;
				if(nokken.isVul()) {
					if(sword.overlaps(nokken)) {
						deathEnemy(nokken);
					}
				}else {
					nokken.preventOverlap(sword);
					Vector2 swordPosition = new Vector2(sword.getX(), sword.getY());
					Vector2 nokkenPosition = new Vector2(nokken.getX(), nokken.getY());
					Vector2 hitVector = swordPosition.sub(nokkenPosition);
					nokken.setMotionAngle(hitVector.angle());
				}
			}
			
			for(BaseActor beetle : BaseActor.getList(mainStage, Beetle.class)) {
				if(sword.overlaps(beetle)) {
					deathEnemy(beetle);
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
						deathEnemy(keaton);
					}	
				}
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
	 * Metodo que se encarga de varias cosas cuando se mata a un enemigo.
	 * 1 - Modifica la puntuacion dependiendo del enemigo que se a matado
	 * 2 - Spawnea un item aleatoriamente
	 * 3 - Aplica un efecto
	 * @param actor Actor
	 * @param type Nombre del Actor
	 */
	private void deathEnemy(BaseActor actor) {
		actor.remove();
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

		return false;
	}
	
	/**
	 * Metodo que se encarga de cargar el mapa e iniciar todos los elementos del mismo
	 */
	private void mapInit() {
		TilemapActor tma = new TilemapActor("assets/maps/extraLevel1/extraMap1.tmx", mainStage);
		
		for(MapObject obj : tma.getRectangleList("solid")) {
			MapProperties prop = obj.getProperties();
			new Solid((float)prop.get("x"), (float)prop.get("y"), (float)prop.get("width"), (float)prop.get("height"), mainStage);	
		}
		
		for(MapObject obj : tma.getRectangleList("plant")) {
			MapProperties prop = obj.getProperties();
			new Plant((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject obj : tma.getRectangleList("bat")) {
			MapProperties prop = obj.getProperties();
			new Bat((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject obj : tma.getRectangleList("nokken")) {
			MapProperties prop = obj.getProperties();
			new Nokken((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject obj : tma.getRectangleList("keaton")) {
			MapProperties prop = obj.getProperties();
			new Keaton((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		for(MapObject obj : tma.getRectangleList("beetle")) {
			MapProperties prop = obj.getProperties();
			new Beetle((float)prop.get("x"), (float)prop.get("y"), mainStage);
		}
		
		MapObject startPoint = tma.getRectangleList("start").get(0);
		MapProperties props = startPoint.getProperties();
		player = new Player((float)props.get("x"), (float)props.get("y"), mainStage);
	
		MapObject endPoint = tma.getRectangleList("end").get(0);
		props = endPoint.getProperties();
		new End((float)props.get("x"), (float)props.get("y"), mainStage);
		
		sword = new Sword(0, 0, mainStage);
		sword.setVisible(false);
		sword.setScale(0.8f);
	}
	
	/**
	 * Metodo que se encarga de iniciar la musica y los sonidos.
	 */
	private void soundtrackInit() {
		
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/94 Lost Woods (Unused).mp3"));
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
				DefinitelyNotZelda.setActiveScreen(new ExtraLevel1());
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
