package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;

    Texture block;
    Rectangle blockpos = new Rectangle();

    Texture img;
    Rectangle rectangle = new Rectangle();
    BitmapFont font;


    boolean isGameOver = false;


    int score = 0;

    int speedx = 5;
    int speedy = 7;


    int max_image_x;
    int max_image_y;
    boolean isUp=false;


    void initImage() {
        img = new Texture("ball.png");
        int rw = Gdx.graphics.getWidth() / 10;

        rectangle.width = rectangle.height = rw;
        rectangle.x = rectangle.y = 0;

        max_image_x = Gdx.graphics.getWidth() - rw;
        max_image_y = Gdx.graphics.getHeight() - rw;
    }

    void initBlock() {
        block = new Texture("block.jpg");

        blockpos.x = blockpos.y = 0;
        blockpos.width = Gdx.graphics.getWidth() / 7;
        blockpos.height = Gdx.graphics.getWidth() / 20;
    }

    void drawBlock() {

        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            if (x >= blockpos.x && x <= blockpos.x + blockpos.width) {
                blockpos.x = x - blockpos.width / 2;
            }

            if (blockpos.x < 0) blockpos.x = 0;
            if (blockpos.x > Gdx.graphics.getWidth() - blockpos.width)
                blockpos.x = Gdx.graphics.getWidth() - blockpos.width;
            isUp=true;
        }else{
            isUp=false;
        }


        batch.draw(block, blockpos.x, blockpos.y, blockpos.width, blockpos.height);
    }


    void drawMovedLogo() {

        rectangle.x += speedx;
        rectangle.y += speedy;

        if (rectangle.y > max_image_y) {
            rectangle.y = max_image_y;
            speedy = -speedy;
        } else if (rectangle.y < 0) {
            isGameOver=true;
            return;
        }

        if (rectangle.x > max_image_x) {
            rectangle.x = max_image_x;
            speedx = -speedx;
        } else if (rectangle.x < 0) {
            rectangle.x = 0;
            speedx = -speedx;
        }


        //colission with block
        if (speedy < 0
                && (blockpos.x + blockpos.width) > rectangle.x
                && blockpos.x < (rectangle.x + rectangle.width)
                && rectangle.y < blockpos.height
                ) {
            speedy = -speedy;
            rectangle.y = blockpos.height;
            score++;
            speedx=score+5;
            speedy=score+7;
        }


        batch.draw(img, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        font=  new BitmapFont();
        initImage();
        initBlock();

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.8f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (!isGameOver) {
            drawMovedLogo();
            drawBlock();
            font.getData().setScale(3);
            font.draw(batch,String.format("%d",score),0,Gdx.graphics.getHeight()-100);
        }else{
            font.getData().setScale(3);
            font.draw(batch,"Game Over",Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
            if(Gdx.input.isTouched()){
                if(isUp) {
                    rectangle.x = 0;
                    rectangle.y = 0;
                    speedx = 5;
                    speedy = 7;
                    score = 0;
                    isGameOver = false;
                }
                isUp=false;
            }else {
                isUp=true;
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }


}
