/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wip.pkg2d;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author ian
 */
public class Wip2d {

  public static final int DISPLAY_HEIGHT = 720;
  public static final int DISPLAY_WIDTH = 900;
  public static final Logger LOGGER = Logger.getLogger(Wip2d.class.getName());
 
  private int imgSize;
  private int xLoc;
  private int yLoc;
  private int imgZ;
  Texture background;
  Texture astro;
 
  static {
    try {
      LOGGER.addHandler(new FileHandler("errors.log",true));
    }
    catch(IOException ex) {
      LOGGER.log(Level.WARNING,ex.toString(),ex);
    }
  }
 
  public static void main(String[] args) {
    Wip2d main = null;
    try {
      System.out.println("Keys:");
      System.out.println("down  - Fly down");
      System.out.println("up    - Fly up");
      System.out.println("left  - Fly left");
      System.out.println("right - Fly right");
      System.out.println("esc   - Exit");
      main = new Wip2d();
      main.create();
      main.run();
    }
    catch(Exception ex) {
      LOGGER.log(Level.SEVERE,ex.toString(),ex);
    }
    finally {
      if(main != null) {
        main.destroy();
      }
    }
  }
 
  public Wip2d() {
    imgSize = 100;
    xLoc = 0;
    yLoc = 0;
    imgZ = 0;
  }
 
  public void create() throws LWJGLException {
    //Display
    Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH,DISPLAY_HEIGHT));
    Display.setFullscreen(false);
    Display.setTitle("Hello LWJGL World!");
    Display.create();
 
    //Keyboard
    Keyboard.create();
 
    //Mouse
    Mouse.setGrabbed(false);
    Mouse.create();
 
    //OpenGL
    initGL();
    resizeGL();
  }
 
  public void destroy() {
    //Methods already check if created before destroying.
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }
 
  public void initGL() {
    //2D Initialization
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);
  }
 
  public void processKeyboard() {
    //Location up and down
    if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
      --yLoc;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
      ++yLoc;
    }
 
    //Location left and right
    if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
      --xLoc;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
      ++xLoc;
    }
    
    //Rotation left and right
    if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
      --imgZ;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
      ++imgZ;
    }
    
  }
 
  public void processMouse() {
    //xLoc = Mouse.getX();
    //yLoc = Mouse.getY();
  }
 
  
//  Texture background = LoadTexture("res/space.png", "PNG");
//  Texture astro = LoadTexture("res/hotdog.png", "PNG");
  public void render() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();
    
    //Space background image
    DrawBackgroundTex(background, 0, 0, DISPLAY_WIDTH+900, DISPLAY_HEIGHT+400);
    
    
    //Astronaut image
    DrawQuadTex(astro, xLoc, yLoc, imgSize, imgSize, imgZ);
  }
 
  public void resizeGL() {
    //2D Scene
    glViewport(0,0,DISPLAY_WIDTH,DISPLAY_HEIGHT);
 
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0.0f,DISPLAY_WIDTH,0.0f,DISPLAY_HEIGHT);
    glPushMatrix();
 
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
  }
 
  public void run() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    background = LoadTexture("res/space.png", "PNG");
    astro = LoadTexture("res/hotdog.png", "PNG");
      
    while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
      if(Display.isVisible()) {
        processKeyboard();
        processMouse();
        update();
        render();
      }
      else {
        if(Display.isDirty()) {
          render();
        }
        try {
          Thread.sleep(100);
        }
        catch(InterruptedException ex) {
        }
      }
      Display.update();
      Display.sync(60);
    }
  }
 
  public void update() {
    if(imgSize < 5) {
      imgSize = 5;
    }
    else if(imgSize >= DISPLAY_HEIGHT) {
      imgSize = DISPLAY_HEIGHT;
    }
  }
  
  //Takes in a path to an image and it's type
  //Returns a Texture object
  public static Texture LoadTexture(String path, String fileType){
      Texture tex = null;
      InputStream in = ResourceLoader.getResourceAsStream(path);
        try {
            tex = TextureLoader.getTexture(fileType, in);
        } catch (IOException ex) {
            Logger.getLogger(Wip2d.class.getName()).log(Level.SEVERE, null, ex);
        }
      return tex;
  }
  
  //Takes in a Texture object and draws it to the display
  public static void DrawQuadTex(Texture tex, int x, int y, int width, int height, int rot) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        tex.bind();
        glTranslatef(x, y, 0);
        glRotatef(rot,0.0f,0.0f,1.0f);
        glBegin(GL_QUADS);    
    
        glTexCoord2f(0, 1);
        glVertex2f(0, 0);
        glTexCoord2f(1, 1);
        glVertex2f(width, 0);
        glTexCoord2f(1, 0);
        glVertex2f(width, height);
        glTexCoord2f(0, 0);
        glVertex2f(0, height);
        glEnd();
      
        glLoadIdentity(); 
  }
  
  
  //Takes in a Texture object and draws it to the display as background
  public static void DrawBackgroundTex(Texture tex, float x, float y, float width, float height) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        tex.bind();
        glTranslatef(x, y, 0);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        glTexCoord2f(1, 0);
        glVertex2f(width, 0);
        glTexCoord2f(1, 1);
        glVertex2f(width, height);
        glTexCoord2f(0, 1);
        glVertex2f(0, height);
        glEnd();      
        glEnd();
        glLoadIdentity();      
  }
  
    
}
