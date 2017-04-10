package com.industries118.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

class Game extends ApplicationAdapter
{
	private static final int WIDTH = 400, HEIGHT = 800;
	private ArrayList<GameObject> gameObjects;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private float delta;
	private long time;
	private Vector3 touchInput;
	private Texture bg;

	
	@Override
	public void create ()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false,WIDTH,HEIGHT);
		batch = new SpriteBatch(16);
		gameObjects = new ArrayList<GameObject>();
		bg = new Texture("floor.png");
		touchInput = new Vector3(0,0,0);
		createImpArray(gameObjects,WIDTH, HEIGHT, 3,3);
	}

	@Override
	public void render ()
	{
		time = System.currentTimeMillis();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		delta = Gdx.graphics.getDeltaTime();
		batch.begin();
		batch.draw(bg,0,0,400,800);
		for(GameObject g:gameObjects)
		{
			g.update(time);
			if(g instanceof TapImp)
				if(Gdx.input.isTouched())
				{
					touchInput = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
					camera.unproject(touchInput);
					((TapImp)g).touchUpdate(touchInput);
				}
			g.draw(batch,delta);
		}
		batch.end();
	}
	
	@Override
	public void dispose ()
	{
		batch.dispose();
		for(GameObject g:gameObjects)
			g.dispose();
	}

	private void createImpArray(ArrayList a, int w, int h, int r, int c)
	{
		int wSpace = w/r;
		int hSpace = (h/c)/2;
		for(int i = 0; i<r;i++)
			for(int j = 0; j<c ;j++)
				a.add(new TapImp(i*wSpace-(40/r),(int)(j*hSpace+(w/1.2))));
	}
}
