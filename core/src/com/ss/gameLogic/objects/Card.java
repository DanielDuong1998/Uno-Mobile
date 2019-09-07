package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GUI;
import com.ss.gameLogic.scene.GGameStatic;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class Card {
  TextureAtlas cardsUnoAtlas;
  Group group;
  public int element;
  public int value;
  public Image image;
  public Image tileDown;

  BoardConfig cfg;

  public Card(TextureAtlas cardsUnoAtlas, Group group, int element, int value){
    this.cardsUnoAtlas = cardsUnoAtlas;
    this.group = group;
    this.element = element;
    this.value = value;

    Gdx.app.log("debug", "element: " + element + " value: " + value);
    image = GUI.createImage(cardsUnoAtlas, this.element + "" + this.value);
    tileDown = GUI.createImage(cardsUnoAtlas, GGameStatic.tileDownStr);

    image.setSize(cfg.CW*cfg.ratioCP, cfg.CH*cfg.ratioCP);
    tileDown.setSize(cfg.CW*cfg.ratioCP, cfg.CH*cfg.ratioCP);

    image.setAlign(Align.center);
    image.setOrigin(Align.center);
    tileDown.setAlign(Align.center);
    tileDown.setOrigin(Align.center);

    this.group.addActor(image);
    this.group.addActor(tileDown);

    addDrag();
  }

  public void setPosition(float x, float y){
    image.setPosition(x, y);
    tileDown.setPosition(x, y);
  }

  public void setVisible(boolean isVisible){
    setVisibleImage(isVisible);
    setVisibleTiledown(isVisible);
  }

  public void setVisibleImage(boolean isVisibleImage){
    image.setVisible(isVisibleImage);
  }

  public void setVisibleTiledown(boolean isVisibleTiledown){
    tileDown.setVisible(isVisibleTiledown);
  }

  public void rotationCard(float rotation, float duration, Interpolation interpolation){
    image.addAction(rotateTo(rotation, duration, interpolation));
    tileDown.addAction(rotateTo(rotation, duration, interpolation));
  }

  public void setSize(float ratio){
    image.setSize(image.getWidth()*ratio, image.getHeight()*ratio);
    tileDown.setSize(tileDown.getWidth()*ratio, tileDown.getHeight()*ratio);
  }

  private void addClick(){
    image.addListener(new ClickListener(){

    });
  }

  private void addDrag(){
    image.addListener(new DragListener(){
      float startX, startY, stopX, stopY;
      int zIndex;
      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        super.dragStart(event, x, y, pointer);
        zIndex = image.getZIndex();
        image.setZIndex(100);
        Gdx.app.log("debug", "position start: " + image.getX() + " zindex: " + zIndex );
        startX = image.getX();
        startY = image.getY();
      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);
        float deltaX = x - image.getWidth()/2;
        float deltaY = y - image.getHeight()/2;
        image.moveBy(deltaX, deltaY);
      }

      @Override
      public void dragStop(InputEvent event, float x, float y, int pointer) {
        super.dragStop(event, x, y, pointer);
        Gdx.app.log("debug", "zindex: " + zIndex);
        stopX = image.getX();
        stopY = image.getY();
        float deltaX = stopX - startX;
        float deltaY = stopY - startY;

        Gdx.app.log("debug", "delta x-y: " + deltaX + "-" + deltaY);

        if(Math.abs(deltaY) < 100){
          image.addAction(sequence(
            moveBy(-deltaX, -deltaY, 0.15f, fastSlow),
            GSimpleAction.simpleAction((d, a)->{
              image.setZIndex(zIndex);
              return true;
            })
          ));
        }
        else {
          image.setZIndex(zIndex);
        }
      }
    });
  }

  public void setTouch(Touchable touchable){
    image.setTouchable(touchable);
  }
}
