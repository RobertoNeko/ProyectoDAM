package base;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Extiende la funcionalidad de la clase Actor de LibGDX
 * @author Roberto Ramiro Hernández
 *
 */

public class BaseActor extends Group {

	private Animation<TextureRegion> animation;
	
	private boolean animationPaused;
	
	private Vector2 velocityVec;
	private Vector2 accelerationVec;

	private Polygon boundaryPolygon;
	
	private float elapsedTime;
	private float acceleration;
	private float maxSpeed;
	private float deceleration;
	
	private static Rectangle worldBounds;
	
	/**
	 * Asigna la posicion inicial del actor y lo añade al stage
	 * @param x
	 * @param y
	 * @param stage
	 */
	public BaseActor(float x, float y, Stage stage) {
		super();
		setPosition(x, y);
		stage.addActor(this);
		inicializarTodo();
	}
	
	/**
	 * Asigna todos los valores
	 */
	private void inicializarTodo() {
		animation = null;
		elapsedTime = 0;
		animationPaused = false;
		velocityVec = new Vector2(0,0);
		accelerationVec = new Vector2(0,0);
		acceleration = 0;
		maxSpeed = 1000;
		deceleration = 0;
		boundaryPolygon = null;
	}
	
	/**
	 * Asigna la animacion al actor
	 * @param anim
	 */
	public void setAnimation(Animation<TextureRegion> anim) {
		animation = anim;
		TextureRegion texReg = animation.getKeyFrame(0);
		float width = texReg.getRegionWidth();
		float height = texReg.getRegionHeight();
		setSize(width, height);
		setOrigin(width/2, height/2);
		
		if(boundaryPolygon == null) {
			setBoundaryRectangle();
		}
	}
	
	/**
	 * Se pausa la animacion del actor
	 * @param pause
	 */
	public void setAnimationPaused(boolean pause) {
		animationPaused = pause;
	}
	
	/**
	 * Procesa todas las acciones del objeto;
	 * Se llama automaticamente por el metodo act del stage
	 * @param delta tiempo que ha pasado desde el ultimo frame
	 */
	public void act(float delta) {
		super.act(delta);
		if(!animationPaused) {
			elapsedTime += delta;
		}
	}
	
	/**
	 * 
	 *  Pinta el ultimo frame de animacion; autmaticamente llamado por el metodo draw del Stage.
     *  @param batch 
     *  @param parentAlpha 
     *  
     */
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a);
		
		if(animation != null && isVisible()) {
			batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		}
		
		super.draw(batch, parentAlpha);
	}
	
	/**
	 * Crea una animacion a partir de unos archivos
	 * @param fileNames
	 * @param frameDuration
	 * @param loop
	 * @return
	 */
	public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop){
		
		int fileCount = fileNames.length;
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		
		for(int i = 0; i < fileCount; i++) {
			String fileName = fileNames[i];
			Texture texture = new Texture(Gdx.files.internal(fileName));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			textureArray.add(new TextureRegion(texture));
		}
		
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		
		if(loop) {
			anim.setPlayMode(Animation.PlayMode.LOOP);
		}else {
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		}
 		if(animation == null) {
 			setAnimation(anim);
 		}
		return anim;
	}
	
	/**
	 * Carga una animacion a partir de una Hoja 
	 * @param fileName
	 * @param rows
	 * @param cols
	 * @param frameDuration
	 * @param loop
	 * @return
	 */
	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop) {
		Texture texture = new Texture(Gdx.files.internal(fileName), true);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		int frameWidth = texture.getWidth() / cols;
		int frameHeight = texture.getHeight() / rows;
		TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		for (int r = 0; r < rows; r++)
		for (int c = 0; c < cols; c++)
		textureArray.add( temp[r][c] );
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		if (loop) {
			anim.setPlayMode(Animation.PlayMode.LOOP);
		}else {
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		}
		if (animation == null) {
			setAnimation(anim);
		}
		return anim;
	}

	/**
	 * Carga una textura
	 * @param fileName
	 * @return
	 */
	public Animation<TextureRegion> loadTexture(String fileName){
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}

	/**
	 * Comprueba que la animacion ya haya terminado
	 * @return
	 */
	public boolean isAnimationFinished() {
		return animation.isAnimationFinished(elapsedTime);
	}
	
	//Dar valor a la velocidad / la Speed puede ser vista como la longitud
	public void setSpeed(float speed) {
		//si la longitud es 0 entonces asumimos que el angulo tiene 0 grados
		if(velocityVec.len() == 0) {
			velocityVec.set(speed, 0);
		}else {
			velocityVec.setLength(speed);
		}
	}
	
	public float getSpeed() {
		return velocityVec.len();
	}
	
	/**
	 * Se le da valor al angulo
	 * @param angle
	 */
	public void setMotionAngle(float angle) {
		velocityVec.setAngle(angle);
	}
	
	public float getMotionAngle() {
		return velocityVec.angle();
	}
	
	public boolean isMoving() {
		return (getSpeed() > 0);
	}
	
	public void setAcceleration(float acc) {
		acceleration = acc;
	}
	
	/**
	 * Acelera el actor en un angulo determinado
	 * @param angle
	 */
	public void accelerateAtAngle(float angle) {
		accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
	}
	
	/**
	 * Acelera el actor en la velocidad que esta mirando
	 */
	public void accelerateForward() {
		accelerateAtAngle(getRotation());
	}
	
	public void setMaxSpeed(float max) {
		maxSpeed = max;
	}
	
	public void setDeceleration(float dec) {
		deceleration = dec;
	}
	
	/**
	 * Se encarga de las fisicas relacionadas con la velocidad / aceleracion
	 * @param delta
	 */
	public void applyPhysics(float delta) {

		//Aplicar Aceleracion
		velocityVec.add(accelerationVec.x * delta, accelerationVec.y * delta);
		
		float speed = getSpeed();
		
		//Disminuir la velocidad cuando no se este acelerando
		if(accelerationVec.len() == 0) {
			speed -= deceleration * delta;
		}
		
		//Mantener la velocidad dentro de unos valores
		speed = MathUtils.clamp(speed, 0, maxSpeed);
		
		//update la velocity
		setSpeed(speed);
		
		//Aplicar la velocidad
		moveBy(velocityVec.x * delta, velocityVec.y * delta);
		
		//reset acc
		accelerationVec.set(0, 0);
	}
	
	/**
	 * Define la colision del actor dandole forma de rectangulo
	 */
	public void setBoundaryRectangle() {
		float w = getWidth();
		float h = getHeight();
		float[] vertices = {0,0, w,0, w,h, 0,h};
		boundaryPolygon = new Polygon(vertices);
	}
	
	/**
	 * Define la colision con un poligono de n lados
	 * @param numSides
	 */
	public void setBoundaryPolygon(int numSides) {
		float w = getWidth();
		float h = getHeight();
		
		float[] vertices = new float[2 * numSides];
		
		for(int i = 0; i < numSides; i++) {
			float angle = i * 6.28f / numSides;
			// x-coordinate
			vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
			// y-coordinate
			vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;
		}
		boundaryPolygon = new Polygon(vertices);
	}
	
	/**
	 * Devuelve la colision del poligono para su actor
	 * @return
	 */
	public Polygon getBoundaryPolygon() {
		boundaryPolygon.setPosition(getX(), getY());
		boundaryPolygon.setOrigin(getOriginX(), getOriginY());
		boundaryPolygon.setRotation(getRotation());
		boundaryPolygon.setScale(getScaleX(), getScaleY());
		return boundaryPolygon;
	}
	
	/**
	 * Metodo para comprobar que dos actores se solapan
	 * @param other el otro actor
	 * @return true or false 
	 */
	public boolean overlaps(BaseActor other) {
		
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		
		//Test inicial para mejorar el rendimiento
		if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
			return false;
		}
			
		return Intersector.overlapConvexPolygons(poly1, poly2);
	}
	
	/**
	 * Centra la posicion del actor
	 * @param x Valor x
	 * @param y Valor y
	 */
	public void centerAtPosition(float x, float y) {
		setPosition(x - getWidth()/2, y - getHeight()/2);
	}
	
	/**
	 * Hace que algo se centre en un actor 
	 * @param other Actor en el que se quiere centrar algo
	 */
	public void centerAtActor(BaseActor other) {
		centerAtPosition(other.getX() + other.getWidth()/2, other.getY() + other.getHeight()/2);
	}
	
	/**
	 * Darle valor a la opacidad del actor
	 * @param opacity valor de la opcacidad
	 */
	public void setOpacity(float opacity) {
		this.getColor().a = opacity;
	}
	
	/**
	 * Previene que dos actores se solapen
	 * @param other
	 * @return
	 */
	public Vector2 preventOverlap(BaseActor other) {
		
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		
		if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
			return null;
		}
		MinimumTranslationVector mtv = new MinimumTranslationVector();
		boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
		
		if(!polygonOverlap) {
			return null;
		}
		
		this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
		return mtv.normal;
	}
	
	/**
	 * (Acordarse de que que he cambiado el String className por Class className)
	 * Devuelve una lista con todos los actores de un stage
	 * @param stage Stage en el que se encuentran los actores
	 * @param className Nombre de la clase
	 * @return devuelve la lista con los actores
	 */
	public static ArrayList<BaseActor> getList(Stage stage, Class className){
		
		ArrayList<BaseActor> list = new ArrayList<BaseActor>();
		Class theClass = null;
		
		try {
			theClass = className;
		}catch (Exception error) {
			error.printStackTrace();
		}
		
		//.isInstance comprueba que un objeto en particular es uan instancia de una clase en concreto o que extiende de una clase. 
		for(Actor actor : stage.getActors()) {
			if(theClass.isInstance(actor)) {
				list.add((BaseActor) actor);
			}
		}
		
		return list;
	}
	
	/**
	 * Devuelve al cantidad de actores de una determinada clase.
	 * @param stage
	 * @param className
	 * @return devuelve la cantidad de actores de una determinada clase
	 */
	public static int count(Stage stage, Class className) {
		return getList(stage, className).size();
	}
	
	/**
	 * Permite crear el limite del mundo a partir de una anchura y una altura
	 * @param width
	 * @param height
	 */
	public static void setWorldBounds(float width, float height) {
		worldBounds = new Rectangle(0, 0, width, height);
	}
	
	/**
	 * Permite crear el limite del mundo a partir de un actor.
	 * @param ba
	 */
	public static void setWorldBounds(BaseActor ba) {
		setWorldBounds(ba.getWidth(), ba.getHeight());
	}
	
	/**
	 * Restringe el movimiento dentro de los limites del mapa
	 */
	public void boundToWorld() {
		if(getX() < 0) {
			setX(0);
		}if(getX() + getWidth() > worldBounds.width) {
			setX(worldBounds.width - getWidth());
		}if(getY() < 0) {
			setY(0);
		}if(getY() + getHeight() > worldBounds.height) {
			setY(worldBounds.height - getHeight());
		}
	}
	
	/**
	 * Restringe el movimiento dentro de los limites establecidos
	 */
	public void boundToLimits(float limitX, float limitY) {
		if(getX() < 0) {
			setX(0);
		}if(getX() + getWidth() > limitX) {
			setX(limitX - getWidth());
		}if(getY() < 0) {
			setY(0);
		}if(getY() + getHeight() > limitY) {
			setY(limitY - getHeight());
		}
	}
	
	/**
	 * Centra la camara
	 */
	public void alignCamera() {
		Camera camera = this.getStage().getCamera();
		Viewport view = this.getStage().getViewport();
		camera.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
		camera.position.x = MathUtils.clamp(camera.position.x, camera.viewportWidth/2, worldBounds.width - camera.viewportWidth/2);
		camera.position.y = MathUtils.clamp(camera.position.y, camera.viewportHeight/2, worldBounds.height - camera.viewportHeight/2);
		camera.viewportHeight = 650 /3 ;
		camera.viewportWidth = 900 /3;
		camera.update();
	}
	
	/**
	 * Sirve para conectar los lados de la pantalla
	 */
	public void wrapAroundWorld() {
		if(getX() + getWidth() < 0) {
			setX(worldBounds.width);
		}if (getX() > worldBounds.width) {
			setX(-getWidth());
		}if(getY() + getHeight() < 0) {
			setY(worldBounds.height);
		}if(getY() > worldBounds.height) {
			setY(-getHeight());
		}
	}
	
	/**
	 * Devielve las dimensiones del mundo
	 * @return
	 */
    public static Rectangle getWorldBounds(){
        return worldBounds;
    }  
	
	/**
	 * Para saber si un actor esta a distancia de otro
	 * @param distance distancia a la que queremos que se ejecute
	 * @param other el otro Actor
	 * @return
	 */
	public boolean isWithinDistance(float distance, BaseActor other) {
		Polygon poly1 = this.getBoundaryPolygon();
		float scaleX = (this.getWidth() + 2 * distance) / this.getWidth();
		float scaleY = (this.getHeight() + 2 * distance) / this.getHeight();
		poly1.setScale(scaleX, scaleY);
		Polygon poly2 = other.getBoundaryPolygon();
		
		if(!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) {
			return false;
		}
		return Intersector.overlapConvexPolygons(poly1, poly2);	
	}
}
