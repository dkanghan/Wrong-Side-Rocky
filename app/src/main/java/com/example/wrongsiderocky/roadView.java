package com.example.wrongsiderocky;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//-----------------------------------------------------------------------
//RoadView
//Handles CLass Initialization for the game and starts the game
//-----------------------------------------------------------------------
public class roadView extends SurfaceView implements Runnable {

    private final int screenX;
    private final int screenY;
    volatile boolean playing;
    volatile boolean gameEnded;
    Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private final Context context;
    int shield;
    private GameObjects gameObjects;
    private LevelManager lm;
    private boolean collisionDetected, hitDetected;
    private SoundManager sm;

    private boolean isBGPlaying;
    private int distance;
    private Bitmap playBtn;
    private boolean boosting;
    private int boostingdistance;
    private healthbar healthbar1, healthbar2;
    private long timeTaken, timeStarted;
    private long longestDistance;
    private int score;
    private final String playerName;
    private final SharedPreferences.Editor editor;
    private ArrayList<Player> leaderboard;


    //-----------------------------------------------------------------------
    //Constructor
    //gets playerName and current highest score
    //initializes classes and other variables necessary for the game
    //initializes paint and Holder for the draw method
    //-----------------------------------------------------------------------
    public roadView(Context context, int x, int y) {
        super(context);
        this.context = context;

        SharedPreferences prefs = context.getSharedPreferences("HiScores", Context.MODE_PRIVATE);
        editor = prefs.edit();
        longestDistance = prefs.getLong("LongestDistance", 2200);
        screenX = x;
        screenY = y;
        playerName = prefs.getString("PlayerName", "");
        leaderboard = new ArrayList<>();
        initializeStubLeaderboard();
        startGame();

    }

    //-----------------------------------------------------------------------
    //startGame()
    //Reinitializes variables and classes to replay the game
    //-----------------------------------------------------------------------
    private void startGame() {
        lm = new LevelManager();
        isBGPlaying = false;
        playing = true;
        ourHolder = getHolder();
        paint = new Paint();
        boostingdistance = 0;
        gameObjects = new GameObjects(context, screenX, screenY);
        distance = 0;
        score = 0;
        gameObjects.initializeObjects(lm.getCurrentLevel());
        shield = 0;
        collisionDetected = false;
        hitDetected = false;
        gameEnded = false;
        playBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.playbtn);
        playBtn = Bitmap.createScaledBitmap(playBtn, 48, 48, true);
        healthbar1 = new healthbar(context, 1);
        healthbar2 = new healthbar(context, 2);
        sm = new SoundManager();
        sm.loadSound(context);
        timeTaken = 0;
        timeStarted = System.currentTimeMillis();
        loadLeaderboard();
    }



    //-----------------------------------------------------------------------
    //update()
    //Updates background music based on level
    //Updates gameObjects for them to move
    //Handles healthbar frames accordingly
    //Handles Boosting of player
    //Updates longest distance when reached
    //plays sound effects when collision detected
    //moves to next level when desired condition fulfilled
    //-----------------------------------------------------------------------
    private void update() throws RuntimeException, InterruptedException, IOException {
        //if player is playing
        if (playing) {
            //play background music of current level if not playing
            if (!isBGPlaying) {
                sm.playBgMusic(lm.getCurrentLevel());
                isBGPlaying = true;
            }

            //Checks condition to move to next level
            if (distance++ == 1000 || distance == 6000 || distance == 6001) {
                if(lm.getCurrentLevel()<3) {
                    nextLevel();
                }
            }

            //Update gameObjects
            gameObjects.updateObjects();

            if (lm.getCurrentLevel() != 1) {
                //checks if player intersected shield power-up
                //if yes increase health bar and points accordingly
                if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getShield().getHitbox())) {
                    if (shield < 4) {
                        score += 50;
                        sm.playSound("extra_life");
                        gameObjects.getShield().setvisible(false);
                        gameObjects.getShield().setX(-screenX);
                        if (shield == 0) {
                            shield += 2;
                            healthbar1.increaseFrame();
                            healthbar1.increaseFrame();
                        } else if (shield == 1) {
                            shield += 2;
                            healthbar1.increaseFrame();
                            healthbar2.increaseFrame();
                        } else if (shield == 2) {
                            shield += 2;
                            healthbar2.increaseFrame();
                            healthbar2.increaseFrame();
                        } else if (shield == 3) {
                            shield += 1;
                            healthbar2.increaseFrame();
                        }

                    }

                }

                //checks if player intersected speedBoost power-up
                //if yes increase speed and set distance of boost
                //increase score
                if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getSpeedBoost().getHitbox())) {
                    boosting = true;
                    boostingdistance = distance;
                    score += 50;
                }
                if (boosting) {
                    //boosting until condition reached
                    if (distance - boostingdistance <= 50) {
                        distance += 5;
                        gameObjects.getPlayer().startBoosting();
                    } else {
                        gameObjects.getPlayer().stopBoosting();
                    }

                }

            }

            //checks if player collided with a car
            //if yes decrease health bar accordingly
            if (collisionDetected) {
                if (shield > 1) {
                    sm.playSound("explode");

                    if (shield == 2) {
                        healthbar1.decreaseFrame();
                        healthbar1.decreaseFrame();
                    } else if (shield == 3) {
                        healthbar2.decreaseFrame();
                        healthbar1.decreaseFrame();
                    } else {
                        healthbar2.decreaseFrame();
                        healthbar2.decreaseFrame();
                    }
                    shield -= 2;
                    collisionDetected = false;
                } else {
                    //if not enough health
                    //end game
                    sm.playSound("explode");
                    sm.stopAll();
                    playing = false;
                    gameEnded = true;
                }
            }
            //checks if player collided with a blockade
            //if yes decrease health bar accordingly
            if (hitDetected) {
                if (shield > 0) {
                    shield--;
                    sm.playSound("hit");
                    hitDetected = false;
                    if (shield <= 2) {
                        healthbar1.decreaseFrame();
                    } else {
                        healthbar2.decreaseFrame();
                    }

                } else {
                    //if not enough health
                    //end game
                    sm.playSound("explode");
                    sm.stopAll();
                    playing = false;
                    gameEnded = true;
                }

            }
            //if game ends
            //stop background music
            //update leaderboard
            if (gameEnded) {
                sm.stopBGMusic();
                updateLeaderboard();
                //calculate if distance travelled is a high score
                timeTaken = System.currentTimeMillis() - timeStarted;
                if (distance > longestDistance) {
                    editor.putLong("LongestDistance", distance);
                    editor.commit();
                    longestDistance = distance;
                }
            }
        }
    }


    //-----------------------------------------------------------------------
    //draw()
    //draw gameObjects
    //Handles healthbar frames accordingly
    //if game ended
    //show score and leaderboard
    //-----------------------------------------------------------------------
    private void draw() throws RuntimeException {
        if (ourHolder.getSurface().isValid()) {
            //initialize and set canvas
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255,
                    0,
                    0,
                    0));
            paint.setColor(Color.argb(255, 255, 255, 255));
            if (playing) {
                //draw pause button
                canvas.drawBitmap(gameObjects.getPauseBitmap(), screenX - 100, 60, paint);
                //draw road bitmap
                canvas.drawBitmap(gameObjects.getRoadBitmap(), gameObjects.getRoad().getX(), gameObjects.getRoad().getY(), paint);

                //draw player bitmap
                canvas.drawBitmap(gameObjects.getPlayer().getBitmap(), gameObjects.getPlayer().getX(), gameObjects.getPlayer().getY(),paint );

                //draw healthbar
                canvas.drawBitmap(healthbar1.getBitmap(), healthbar1.getX(), healthbar1.getY(), paint);
                canvas.drawBitmap(healthbar2.getBitmap(), healthbar2.getX(), healthbar2.getY(), paint);

                if (lm.getCurrentLevel() != 1) {
                    //if shield is visible
                    //draw shield
                    if (gameObjects.getShield().isVisible()) {
                        canvas.drawBitmap( gameObjects.getShield().getBitmap(), gameObjects.getShield().getX(), gameObjects.getShield().getY(), paint);
                    }
                    //draw police object
                    //play sound effect when police enters the screen
                    //check if police intersects player
                    for (Police police : gameObjects.getPolice()) {
                        if (police.getX() <= 0) {
                            sm.stop("police");
                        }
                        if (police.getX() >= screenX - police.getBitmap().getWidth() && police.getX() <= screenX) {
                            sm.playSound("police");
                        }
                        canvas.drawBitmap(police.getBitmap(), police.getX(), police.getY(), paint);
                        if (Rect.intersects(gameObjects.getPlayer().getHitbox(), police.getHitbox())) {
                            collisionDetected = true;
                            police.setX(-screenX);
                        }
                    }

                    //draw blockade objects
                    canvas.drawBitmap(gameObjects.getBlockade().getBitmap(), gameObjects.getBlockade().getX(), gameObjects.getBlockade().getY(), paint);

                    //draw speed boost object
                    canvas.drawBitmap(gameObjects.getSpeedBoost().getBitmap(), gameObjects.getSpeedBoost().getX(), gameObjects.getSpeedBoost().getY(), paint);

                    //check if player intersects blockade
                    if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getBlockade().getHitbox())) {
                        hitDetected = true;
                        gameObjects.getBlockade().setX(-screenX - 100);
                    }
                    //check if player picks up speed boost
                    if (Rect.intersects(gameObjects.getPlayer().getHitbox(), gameObjects.getSpeedBoost().getHitbox())) {
                        gameObjects.getSpeedBoost().setX(-screenX - 100);
                    }

                }

                //draw traffic cars
                //checks if player intersects traffic cars
                for (trafficCars car : gameObjects.getCars()) {
                    canvas.drawBitmap(car.getBitmap(), car.getX(), car.getY(), paint);
                    if (Rect.intersects(gameObjects.getPlayer().getHitbox(), car.getHitbox())) {
                        collisionDetected = true;
                        car.setX(-screenX - 100);
                    }
                }

                //draw road objects
                if(lm.getCurrentLevel()!=3)
                {
                    for (roadObjects roadObjects : gameObjects.getLamp()) {
                        canvas.drawBitmap(roadObjects.getBitmap(), roadObjects.getX(), roadObjects.getY(), paint);
                    }
                }

            }

            // if game not ended
            // show current stat of player
            if (!gameEnded) {
                timeTaken = System.currentTimeMillis() - timeStarted;
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Power Up Score : " + score +"  Distance:" +
                        distance +
                        "m", screenX - 500, screenY - 50, paint);

            } else {
                //If game ends
                //give option to replay the game
                //show current player stat
                //show leaderboard
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", (float) screenX / 2, 100, paint);
                paint.setTextSize(25);
                canvas.drawText("Distance Travelled:" +
                        distance + "m", (float) screenX / 2, 160, paint);
                canvas.drawText("Longest Distance Travelled:" +
                        longestDistance + "m", (float) screenX / 2, 250, paint);
                canvas.drawText("Time:" + formatTime(timeTaken) +
                        "s", (float) screenX / 2, 200, paint);
                paint.setTextSize(80);
                canvas.drawText("Tap to replay", (float) screenX / 2, 350, paint);
                drawLeaderboard(canvas);
            }
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException ignored) {

        }
    }

    //-----------------------------------------------------------------------
    //pause()
    //pause game
    //-----------------------------------------------------------------------
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    //-----------------------------------------------------------------------
    //resume()
    //resume game
    //-----------------------------------------------------------------------
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    //-----------------------------------------------------------------------
    //run()
    //calls update(),draw() and control()
    //-----------------------------------------------------------------------
    @Override
    public void run() {
        while (playing) {
            try {
                update();
                draw();
            } catch (RuntimeException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            control();
        }
    }

    //-----------------------------------------------------------------------
    //onTouchEvent()
    //checks for touch event happening in game
    //handles player motion
    //if pause button is pressed or not
    //restart game if ended
    //-----------------------------------------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                //get touch coordinates and sets player coordinates accordingly
                if (gameObjects.getPlayer().getX() < touchX) {
                    gameObjects.getPlayer().setX(Math.min(touchX, screenX / 2));
                }
                gameObjects.getPlayer().setY(touchY);
                break;
            case MotionEvent.ACTION_DOWN:
                //checks for clicks event
                //if player clicked play button, play the game
                //if player clicked pause button, pause the game
                boolean condition = touchX >= screenX - 148 && touchX <= screenX + 148 && touchY >= 60 - 48 && touchY <= 60 + 48;
                if (!playing) {
                    if (touchX >= screenX / 2 - 24 && touchX <= screenX / 2 + 24 && touchY >= (screenY / 2 + 200) - 24 && touchY <= (screenY / 2 + 200) + 24) {
                        gameObjects.initializeObjects(lm.getCurrentLevel());
                        sm.stop("next_level");
                        resume();
                    } else if (condition) {
                        resume();
                    }
                } else {
                    if (condition) {
                        pause();
                    }

                }
                if (gameEnded) {
                    //if game ended restart game
                    Log.d("PRESSED", "onTouchEvent: ");
                    startGame();
                    resume();
                }
                break;
        }
        return true;
    }

    //-----------------------------------------------------------------------
    //initializeStubLeaderboard()
    //initialize stubleaderboard for the player to view
    //-----------------------------------------------------------------------
    private void initializeStubLeaderboard() {
        // Add some sample players to the leaderboard
        leaderboard.add(new Player("Player5", 2200, 800));
        leaderboard.add(new Player("Player4", 1800, 550));
        leaderboard.add(new Player("Player3", 1500, 600));
        leaderboard.add(new Player("Player2", 2000, 700));
        leaderboard.add(new Player("Player1", 50, 50));
        saveLeaderboard();
    }

    //-----------------------------------------------------------------------
    //loadLeaderboard()
    //gets leaderboard for the player to view
    //-----------------------------------------------------------------------
    private void loadLeaderboard() {
        SharedPreferences prefs = context.getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE);
        String leaderboardString = prefs.getString("LEADERBOARD", "");
        leaderboard = parseLeaderboardString(leaderboardString);
    }

    //-----------------------------------------------------------------------
    //parseLeaderboardString()
    //gets leaderboard for the game from string and converts them to arraylist of player object
    //-----------------------------------------------------------------------
    private ArrayList<Player> parseLeaderboardString(String leaderboardString) {
        ArrayList<Player> leaderboard = new ArrayList<>();
        if (!leaderboardString.isEmpty()) {
            //splits the string and retrieves variables to create player objects
            String[] playerEntries = leaderboardString.split(";");
            for (String playerEntry : playerEntries) {
                String[] playerData = playerEntry.split(",");
                if (playerData.length == 4) {
                    String playerName = playerData[0];
                    long highestDistance = Long.parseLong(playerData[1]);
                    int highestScore = Integer.parseInt(playerData[2]);
                    Player player = new Player(playerName, highestDistance, highestScore);
                    leaderboard.add(player);
                }
            }
        }
        return leaderboard;
    }

    //-----------------------------------------------------------------------
    //saveLeaderboard()
    //gets leaderboard for the game and converts them to string to use them with getSharedPreferences
    //-----------------------------------------------------------------------
    private void saveLeaderboard() {
        SharedPreferences prefs = context.getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String leaderboardString = convertLeaderboardToString(leaderboard);
        editor.putString("LEADERBOARD", leaderboardString);
        editor.apply();

    }

    //-----------------------------------------------------------------------
    //convertLeaderboardToString()
    //gets leaderboard and converts player variables them to string
    //-----------------------------------------------------------------------
    private String convertLeaderboardToString(List<Player> leaderboard) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : leaderboard) {
            //converts player variables them to string
            stringBuilder.append(player.getPlayerName()).append(",")
                    .append(player.getHighestDistance()).append(",")
                    .append(player.getHighestScore()).append(",")
                    .append(player.getTotalScore()).append(";");
        }
        return stringBuilder.toString();
    }

    //-----------------------------------------------------------------------
    //updateLeaderboard()
    //gets current leaderboard and checks if player qualifies to be in the leaderboard, if yes add the player
    //-----------------------------------------------------------------------
    private void updateLeaderboard() {
        int totalScore = distance + score;
        if (leaderboard.size() < 5 || totalScore > leaderboard.get(leaderboard.size() - 1).getTotalScore()) {
            Player newPlayer = new Player(playerName, distance, score);
            leaderboard.add(newPlayer);
            leaderboard.sort((p1, p2) -> Long.compare(p2.getTotalScore(), p1.getTotalScore()));
            if (leaderboard.size() > 5) {
                leaderboard = new ArrayList<>(leaderboard.subList(0, 5));
            }
            saveLeaderboard();
        }

    }

    //-----------------------------------------------------------------------
    //drawLeaderboard()
    //gets current leaderboard and draw it when game ends
    //-----------------------------------------------------------------------
    private void drawLeaderboard(Canvas canvas) {
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        int startY = 500;
        for (int i = 0; i < leaderboard.size(); i++) {
            Player player = leaderboard.get(i);
            String playerInfo = "| Player: " + player.getPlayerName() + " | Distance: " + player.getHighestDistance() + "m | Score: " + player.getHighestScore() + " | Total Score: " + player.getTotalScore() + "|";
            canvas.drawText(playerInfo, (float)screenX/2, startY + i * 50, paint);
        }
    }

    //-----------------------------------------------------------------------
    //formatTime()
    //formats time in a readable manner
    //-----------------------------------------------------------------------
    private String formatTime(long time) {
        long seconds = (time) / 1000;
        long thousandths = (time) - (seconds * 1000);
        String strThousandths = String.valueOf(thousandths);
        if (thousandths < 100) {
            strThousandths = "0" + thousandths;
        }
        if (thousandths < 10) {
            strThousandths = "0" + strThousandths;
        }
        return seconds + "." + strThousandths;
    }

    //-----------------------------------------------------------------------
    //nextLevel()
    //handles tasks required for next level
    //plays sound effects for player when they reach next level
    //reinitializes game objects based on new level
    //pause the game until player clicks on play
    //-----------------------------------------------------------------------
    private void nextLevel(){
        sm.stopAll();
        sm.stopBGMusic();
        isBGPlaying = false;
        lm.nextLevel();
        canvas = ourHolder.lockCanvas();
        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(120);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("NEXT LEVEL : " + (lm.getCurrentLevel()), (float) canvas.getWidth() / 2, (float) canvas.getHeight() / 2, paint);
        canvas.drawBitmap(playBtn, (float) screenX / 2, (float) (screenY / 2 + 200), paint);
        ourHolder.unlockCanvasAndPost(canvas);
        sm.playSound("next_level");
        pause();
    }

    //getters and setters for test
    public GameObjects getGameObjects() {
        return gameObjects;
    }

    public LevelManager getLm() {
        return lm;
    }
    public SoundManager getSm() {
        return sm;
    }
}
