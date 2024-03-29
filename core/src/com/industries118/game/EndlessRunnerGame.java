package com.industries118.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

//Endless Runner Screen class
class EndlessRunnerGame implements Screen, GestureDetector.GestureListener
{
    private GameEntry game;                     //Variable to store GameEntry for access
    private ArrayList<GameObject> gameObjects;  //ArrayList of GameObjects
    private Texture bg;                         //BackGround Texture
    private CustomFont mFont;                   //Font for score/distance

    private int playerPosition;                 //1, 2 or 3 for left, centre and right
    private Random r;                           //Random num generator
    private boolean running;                    //True if game is not paused

    private Music music;                        //LibGdx Music variable
    private int dx = 128;                       //The distance objects get moved each frame
    private int d = 0;                          //The current distance of bg
    private int fireballPos = GameEntry.HEIGHT; //Y Position of FireBalls
    private int place;                          //Variable for random placement of FireBalls
    private int[] places;                       //Places of the two FireBalls

    //Constructor
    EndlessRunnerGame(final GameEntry game)
    {
        this.game = game;
        GameEntry.ENDLESS_RUNNER_SCORE = 0;
        game.camera = new OrthographicCamera();
        game.camera.setToOrtho(false, GameEntry.WIDTH, GameEntry.HEIGHT);
        gameObjects = new ArrayList<GameObject>();
        mFont = new CustomFont("BLOODY.TTF",24, Color.YELLOW);
        bg = new Texture("floortile.png");
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        music = Gdx.audio.newMusic(Gdx.files.internal("sfx/deathcell.ogg"));
        music.play();
        music.setLooping(true);
        r = new Random();
        places = new int[]{r.nextInt(3) + 1, r.nextInt(3) + 1};
        gameObjects.add(new Player(GameEntry.WIDTH/2,50,0.1f));
        gameObjects.add(new Fireball(0,fireballPos));
        gameObjects.add(new Fireball(0,fireballPos));
        Gdx.input.setInputProcessor(new GestureDetector(this));
        playerPosition = 2;
        place = 0;
        running = true;
    }

    //Called 30 or 60 times every second
    @Override
    public void render(float delta)
    {
        if(running)
        {
            if(fireballPos<-100)
            {
                fireballPos = GameEntry.HEIGHT+100;
                for(int i = 0; i < places.length;i++)
                    places[i] = r.nextInt(3)+1;
            }
            if(d<-GameEntry.HEIGHT)
            {
                d = 0;
                if(dx<800)
                    dx+=16;
            }
            else
            {
                d-=dx*delta;
                fireballPos -=1.1*(dx*delta);
            }
            game.setCameraBits();
            game.batch.begin();
            game.batch.draw(bg, 0, GameEntry.HEIGHT+d, GameEntry.WIDTH, GameEntry.HEIGHT);
            game.batch.draw(bg, 0, d, GameEntry.WIDTH, GameEntry.HEIGHT);
            for(GameObject g: gameObjects)
            {
                if(g instanceof Fireball)
                {
                    g.setX((places[(place++)%places.length])*(GameEntry.WIDTH/4));
                    g.setY(fireballPos);
                }
                if(g instanceof Player)
                {
                    g.setX(playerPosition*(GameEntry.WIDTH/4));
                    if((fireballPos<=g.getY()+(g.getHeight()/2)&&fireballPos>g.getY())&&(playerPosition==places[0]||playerPosition==places[1]))
                    {
                        running = false;
                        game.setScreen(new LeaderBoard(game,(int) GameEntry.ENDLESS_RUNNER_SCORE/100,1));
                        dispose();
                    }
                }
                g.draw(game.batch,delta);
            }
            mFont.draw(game.batch,"Score: "+(int) GameEntry.ENDLESS_RUNNER_SCORE/100,(GameEntry.WIDTH/2)-(mFont.getWidth()/2), GameEntry.HEIGHT-60);
            game.batch.end();
            GameEntry.ENDLESS_RUNNER_SCORE+=(dx/100);
        }
    }

    //Called when game goes out of focus
    @Override
    public void pause(){running = false;}

    //Called when game regains focus
    @Override
    public void resume(){running = true;}

    //Called when screen is disposed
    @Override
    public void dispose()
    {
        bg.dispose();
        music.dispose();
        mFont.dispose();
        for(GameObject g : gameObjects)
            g.dispose();
    }

    //Called when a fling gesture is detected
    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        if(Math.abs(velocityX)>Math.abs(velocityY)){
            if(velocityX>0&&playerPosition<3)
                playerPosition++;
            else if(velocityX<0&&playerPosition>1)
                playerPosition--;
        }
        return false;
    }

    @Override
    public void show(){/*Unused implement method*/}

    @Override
    public void resize(int width, int height){/*Unused implement method*/}

    @Override
    public void hide(){/*Unused implement method*/}

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {return false;}

    @Override
    public boolean tap(float x, float y, int count, int button) {return false;}

    @Override
    public boolean longPress(float x, float y) {return false;}

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY){return false;}

    @Override
    public boolean panStop(float x, float y, int pointer, int button){return false;}

    @Override
    public boolean zoom(float initialDistance, float distance){return false;}

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2){return false;}

    @Override
    public void pinchStop(){/*Unused implement method*/}
}
