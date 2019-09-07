package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.ss.core.commons.Tweens;
import com.ss.gameLogic.objects.staticObject.HandlingCards;
import com.ss.gameLogic.objects.staticObject.Rules;
import com.ss.gameLogic.scene.GGameMain;

public class Board {
  TextureAtlas cardsUnoAtlas;
  Group group;
  BoardConfig cfg;
  Rules rules;
  GGameMain game;
  Array <Vector2> tiles;
  Array<Array<Card>> cards;
  Array<Card> cardsAvailable;
  HandlingCards handlingCards;
  Card cardInTheTable;
  int elementInTheTable;
  boolean isRightDirection = true;
  int turnGame;

  public Board(TextureAtlas cardsUnoAtlas, Group group, GGameMain game){
    this.cardsUnoAtlas = cardsUnoAtlas;
    this.group = group;
    this.rules = new Rules();
    this.game = game;
    init();
    handlingCards = new HandlingCards(this.cardsUnoAtlas, this.group, this);
    turnGame = (int) Math.floor(Math.random()*4);
    shuffleCards();

    Tweens.setTimeout(group, 0.5f, ()->{
      renderCards();
    });
//    group.addListener(new DragListener(){
//      @Override
//      public void drag(InputEvent event, float x, float y, int pointer) {
//        super.drag(event, x, y, pointer);
//        Gdx.app.log("debug", "x-y" + x + "-" + y + " pointer x-y: " + pointer);
//        float delta = x - group.getWidth()/2;
//        group.moveBy(delta-getDragStartX(), 0);
//      }
//    });
  }

  private void init(){
    tiles = new Array<>();
    Array<Card> cards1 = new Array<>();
    Array<Card> cards2 = new Array<>();
    Array<Card> cards3 = new Array<>();
    Array<Card> cards4 = new Array<>();
    cards = new Array<>();
    cards.add(cards1, cards2, cards3, cards4);
  }

  private void shuffleCards(){
    handlingCards.shuffleCards(tiles);
  }

  private void renderCards(){
    handlingCards.distributeCards(cards, tiles, game.positionCards, game.groupCards);
  }

  public void doneRenderStart(){
      cardInTheTable = handlingCards.getCardInTheTable();
      if(cardInTheTable.element == 9){
        elementInTheTable = (int)Math.floor(Math.random()*4 + 1);
      }
      else elementInTheTable = cardInTheTable.element;
      Gdx.app.log("debug", "render cards done!!! turn: " + turnGame);
      startGame();
  }

  private void startGame(){

  }


}
