package com.industries118.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

class TapImp extends GameObject
{
    //GameLogic
    private long popTime;
    private long deathTime;
    private boolean popped = false;
    private Random r;

    //Sfx
    private Sound impPop;
    private Sound score;

    TapImp(float x, float y)
    {
        super(x,y);
        setAnim("impspawn.png",16,1,0.066f);
        r = new Random();
        deathTime = System.currentTimeMillis();
        impPop = Gdx.audio.newSound(Gdx.files.internal("sfx/imppop.ogg"));
        score = Gdx.audio.newSound(Gdx.files.internal("sfx/score.ogg"));
    }

    void update(long time)
    {
        if(!popped)
        {
            int random = r.nextInt(1000);
            if((random > 666&& random <690)&&time-deathTime>333)
            {
                impPop.play(0.2f);
                popTime = time;
                stateTime = 0f;
                popped = true;
            }
        }
        else
        {
            if((time-popTime)>666)
            {
                deathTime = time;
                popped = false;
            }
        }
    }

    void touchUpdate(Vector3 input)
    {
        if(popped)
            if(input.x > getX()&&input.x < getX()+160)
                if(input.y>getY()&&input.y < getY()+130)
                    kill();
    }

    @Override
    void draw(SpriteBatch batch,float delta)
    {
        stateTime += delta;
        TextureRegion currentFrame = idleAnim.getKeyFrame(stateTime, true);
        if(popped)
            batch.draw(currentFrame,getX(),getY(),160,130);
    }

    void dispose()
    {

        idleSheet.dispose();
        impPop.dispose();
        score.dispose();
    }

    private void kill()
    {
        score.play(1.0f);
        gameEntry.TAP_AN_IMP_SCORE++;
        popped = false;
    }

    void setPopped(boolean popped)
    {
        this.popped = popped;
    }
}
