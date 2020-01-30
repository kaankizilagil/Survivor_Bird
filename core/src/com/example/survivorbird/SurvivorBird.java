package com.example.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background, bird, bee1, bee2, bee3;
	float birdX = 0, birdY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.2f;

	int score = 0;
	int scoreEnemy = 0;
	BitmapFont font1;
	BitmapFont font2;

	Random random;

	int numberOfEnemies  = 4;
	float[] enemyX 		 = new float[numberOfEnemies];
	float[] enemyOffSet1 = new float[numberOfEnemies];
	float[] enemyOffSet2 = new float[numberOfEnemies];
	float[] enemyOffSet3 = new float[numberOfEnemies];

	ShapeRenderer shapeRenderer;

	Circle birdCircle;
	Circle[] enemyCircles1;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;

	float distance = 0;
	float enemyVelocity = 2;
	
	@Override
	public void create () {		// While Opening Game

		// Initialize

		batch 		= new SpriteBatch();
		background 	= new Texture("Background.png");
		bird 		= new Texture("Bird_1.png");
		bee1 		= new Texture("Bee.png");
		bee2 		= new Texture("Bee.png");
		bee3 		= new Texture("Bee.png");

		random = new Random();

		distance = Gdx.graphics.getWidth() / 3;

		birdX = Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2;
		birdY = Gdx.graphics.getHeight() / 3;

		birdCircle 	  = new Circle();
		enemyCircles1 = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		font1 = new BitmapFont();
		font1.setColor(Color.WHITE);
		font1.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.RED);
		font2.getData().setScale(6);

		shapeRenderer = new ShapeRenderer();

		for(int i = 0 ; i < numberOfEnemies ; i++) {

			enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - (bee1.getWidth() / 2) + (i * distance);

			enemyCircles1[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}
	}

	@Override
	public void render () {		// While Continuing or Playing Game

		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1) {

			if(enemyX[scoreEnemy] < ((Gdx.graphics.getWidth() / 2) - (bird.getHeight() / 2))) {

				score++;

				if(scoreEnemy < numberOfEnemies - 1) {

					scoreEnemy++;

				} else {

					scoreEnemy = 0;
				}
			}

			if(Gdx.input.justTouched()) {

				velocity = -7;
			}

			for(int i = 0 ; i < numberOfEnemies ; i++) {

				if(enemyX[i] < Gdx.graphics.getWidth() / 15) {

					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

				} else {

					enemyX[i] = enemyX[i] - enemyVelocity;
				}

				batch.draw(bee1, enemyX[i],
                            Gdx.graphics.getHeight() / 2 + enemyOffSet1[i],
                         Gdx.graphics.getWidth() / 15,
                         Gdx.graphics.getHeight() / 10);
				batch.draw(bee2, enemyX[i],
                            Gdx.graphics.getHeight() / 2 + enemyOffSet2[i],
						 Gdx.graphics.getWidth() / 15,
                         Gdx.graphics.getHeight() / 10);
				batch.draw(bee3, enemyX[i],
                            Gdx.graphics.getHeight() / 2 + enemyOffSet3[i],
						 Gdx.graphics.getWidth() / 15,
                         Gdx.graphics.getHeight() / 10);

				enemyCircles1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30,
											  Gdx.graphics.getHeight() / 2 + enemyOffSet1[i] + Gdx.graphics.getHeight() / 20,
									      Gdx.graphics.getWidth() / 30);
				enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30,
											  Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 20,
										  Gdx.graphics.getWidth() / 30);
				enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30,
											  Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 20,
										  Gdx.graphics.getWidth() / 30);
			}

			if(birdY > 0) {

				velocity = velocity + gravity;
				birdY = birdY - velocity;

			} else { // END OF THE GAME

				gameState = 2;
			}

		} else if(gameState == 0){

			if(Gdx.input.justTouched()) {

				gameState = 1;
			}

		} else if(gameState == 2) {

			font2.draw(batch, "Game Over! Tap To Play Again!", 350, 600);

			if(Gdx.input.justTouched()) {

				gameState = 1;

				birdY = Gdx.graphics.getHeight() / 2;

				for(int i = 0 ; i < numberOfEnemies ; i++) {

					enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - (bee1.getWidth() / 2) + (i * distance);

					enemyCircles1[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0;
				scoreEnemy = 0;
				score = 0;
			}
		}

		batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

		font1.draw(batch, "Score : " + score, 150,250);

		batch.end();

		birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);

		/*
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		*/

		for(int i = 0 ; i < numberOfEnemies ; i++) {

			// These lines to identify for bird and bees to inside a black circle.

			/*
			shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,
								 Gdx.graphics.getHeight() / 2 + enemyOffSet1[i] + Gdx.graphics.getHeight() / 20,
							 Gdx.graphics.getWidth() / 30);
			shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,
								 Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 20,
							 Gdx.graphics.getWidth() / 30);
			shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30,
								 Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 20,
							 Gdx.graphics.getWidth() / 30);
			 */

			if(Intersector.overlaps(birdCircle, enemyCircles1[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])) {

				gameState = 2;
			}
		}

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
