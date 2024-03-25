package com.example.wrongsiderocky;

import android.content.Context;
import android.view.MotionEvent;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;



@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context context;
    private int screenWidth,screenHeight;
    private roadView roadViewInstance;

    //setUp()
    //Initializes variables before performing the test
    @Before
    public void setUp() {
       context = InstrumentationRegistry.getInstrumentation().getTargetContext();
       screenWidth = 1080;
       screenHeight = 1920;
       roadViewInstance = new roadView(context, screenWidth, screenHeight);
    }

    //useAppContext()
    //initializes context to use for testing purposes
    @Test
    public void useAppContext() {
        // Context of the app under test.
        assertEquals("com.example.wrongsiderocky", context.getPackageName());
    }


    //testInitialization()
    //checks if initialization of roadView and other variable that are initialized in constructor are initialized properly
    //roadView initializes LevelManaer and GameObjects which are important for the game.
    //check if the objects created are properly initialized and not null
    @Test
    public void testInitialization(){

        // Check if roadViewInstance is not null
        assertNotNull(roadViewInstance);
        LevelManager lm = roadViewInstance.getLm();
        assertNotNull(lm);
        GameObjects go = roadViewInstance.getGameObjects();
        go.initializeObjects(lm.getCurrentLevel());
        assertNotNull(roadViewInstance.getSm());

        lm.nextLevel();
        go.initializeObjects(lm.getCurrentLevel());
        assertNotNull(go.getBlockade());
        assertNotNull(go.getSpeedBoost());
        lm.nextLevel();
        go.initializeObjects(lm.getCurrentLevel());
        assertNotNull(go.getShield());
        assertNotNull(go.getPolice());
    }

    //testgameobjectsInitialization()
    //To test if the objects created by GameObjects class are initialized properly
    //GameObjects create Objects based on the level of the game
    //Test for Objects in each level if they are null or not accordingly.
    @Test
    public void testgameobjectsInitialization(){

        GameObjects go = roadViewInstance.getGameObjects();
        assertNotNull(go);
        go.initializeObjects(1);
        assertNotNull(go.getRoad());
        assertNotNull(go.getPlayer());
        assertNotNull(go.getCars().get(0));

        go.initializeObjects(2);
        assertNotNull(go.getRoad());
        assertNotNull(go.getPlayer());
        assertNotNull(go.getCars().get(0));
        assertNotNull(go.getShield());
        assertNotNull(go.getBlockade());

        go.initializeObjects(3);
        assertNotNull(go.getRoad());
        assertNotNull(go.getPlayer());
        assertNotNull(go.getCars().get(0));
        assertNotNull(go.getShield());
        assertNotNull(go.getBlockade());
        assertNotNull(go.getPolice().get(0));

    }

    //testUpdateMethod()
    //To test if the update method of class trafficCars, Police, Blockade, Shield are working properly
    //Test if all the Objects are moving in the x-axis as expected
    //Test if police is changing Y-axis according to player
    @Test
    public void testUpdateMethod()  {

        GameObjects go = roadViewInstance.getGameObjects();
        go.initializeObjects(3);

        Rocky rocky = go.getPlayer();
        trafficCars cars = go.getCars().get(0);
        Police police = go.getPolice().get(0);
        Blockade blockade = go.getBlockade();
        Shield shield = go.getShield();
        int carx = cars.getX();
        int policex = cars.getX();
        int policey = police.getY();
        int blockadey = blockade.getY();
        int blockadex = blockade.getX();
        int shieldx = shield.getX();
        cars.update(rocky.getSpeed(),context,screenHeight,rocky.getBitmap().getHeight(),1);
        rocky.setY(screenHeight);
        police.update(rocky,rocky.getSpeed(),go.getCars());
        blockade.update(rocky.getSpeed(),3);
        shield.update(rocky.getSpeed(),rocky.getBitmap().getHeight());

        assertNotEquals("Car should move in x axis such that initial x position is greater than updated",carx,cars.getX());
        assertTrue(carx>cars.getX());

        assertNotEquals("Police should move in x axis such that initial x position is greater than updated",policex,police.getX());
        assertNotEquals("Police should move in y axis such that they follow player",policey,police.getY());

        assertNotEquals("Blockade should move in x axis such that initial x position is greater than updated",blockadex,blockade.getX());
        assertTrue(blockadex>blockade.getX());

        assertNotEquals("Shield should move in x axis such that initial x position is greater than updated",shieldx,shield.getX());
        assertTrue(shieldx>shield.getX());

        assertEquals(blockadey,blockade.getY());
        assertFalse("Blockade should not move in y axis",blockadey!=blockade.getY());

    }

    //levelProgression()
    //Test if levels are increasing accordingly
    @Test
    public void levelProgression(){

        LevelManager lm = roadViewInstance.getLm();
        assertEquals("Level should be initialized at level = 1",1,lm.getCurrentLevel());
        lm.nextLevel();
        assertEquals("After calling nextLevel(), current level should change to 2",2,lm.getCurrentLevel());
    }

    //playerMove()
    //Test if touch event is working as expected
    //Simulate a MotionEvent with ACTION_MOVE
    //Test if our player moves to the touchevent.
    //Test if player does not move out of road as we as do not move ahead more than screenWidth/2
    @Test
    public void playerMove(){
        Rocky player = roadViewInstance.getGameObjects().getPlayer();
        int initialPlayerX = player.getX();

        // Simulate a MotionEvent with ACTION_MOVE
        int touchX = screenWidth / 2 - 50;
        int touchY = screenHeight / 2 + 50;
        MotionEvent moveEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, touchX, touchY, 0);
        roadViewInstance.onTouchEvent(moveEvent);

        int updatedPlayerX = player.getX();
        int updatedPlayerY = player.getY();

        assertNotEquals("Player X position should have changed after ACTION_MOVE", initialPlayerX, updatedPlayerX);
        assertEquals("Player Y position should have changed to touchY after ACTION_MOVE", touchY, updatedPlayerY);
        assertEquals("Player X position should have changed to touchX after ACTION_MOVE", touchX, updatedPlayerX);

        moveEvent.recycle();

        touchX = screenWidth / 2 + 100;
        touchY = screenHeight ;
        MotionEvent moveEvent2 = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, touchX, touchY, 0);
        roadViewInstance.onTouchEvent(moveEvent2);

        updatedPlayerX = player.getX();
        updatedPlayerY = player.getY();

        assertNotEquals("Player X position should have changed after ACTION_MOVE", initialPlayerX, updatedPlayerX);
        assertNotEquals("Player Y position should not be touchY after ACTION_MOVE", touchY, updatedPlayerY);
        assertNotEquals("Player X position should not be touchX after ACTION_MOVE", touchX, updatedPlayerX);

        moveEvent2.recycle();
    }

    //testPauseAndResume()
    //Test if player stops playing on pause and starts playing on resume.
    @Test
    public void testPauseAndResume() {
        assertTrue(roadViewInstance.playing);

        roadViewInstance.resume();
        assertTrue("The game should be resumed", roadViewInstance.playing);

        roadViewInstance.pause();
        assertFalse("The game should be paused", roadViewInstance.playing);

        roadViewInstance.resume();
        assertTrue("The game should be resumed", roadViewInstance.playing);
    }


}