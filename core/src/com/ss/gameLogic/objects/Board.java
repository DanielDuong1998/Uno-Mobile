package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.commons.Tweens;
import com.ss.core.util.GUI;
import com.ss.gameLogic.objects.staticObject.HandlingCards;
import com.ss.gameLogic.objects.staticObject.Rules;
import com.ss.gameLogic.scene.GGameMain;
import com.ss.gameLogic.scene.GGameStatic;


import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

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

  Image arrowUp;
  Image arrowBottom;

  public Board(TextureAtlas cardsUnoAtlas, Group group, GGameMain game){
    this.cardsUnoAtlas = cardsUnoAtlas;
    this.group = group;
    this.rules = new Rules();
    this.game = game;
    init();
    handlingCards = new HandlingCards(this.cardsUnoAtlas, this.group, this);
    turnGame = (int) Math.floor(Math.random()*4);
    //turnGame = 3;
    shuffleCards();

    Tweens.setTimeout(group, 0.5f, ()->{
      renderCards();
    });
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
      Gdx.app.log("debug", "render cards done!!! turn: " + turnGame + " element: " + elementInTheTable);
      startGame();
  }

  private void startGame(){

    getCard();
  }

  private void showArrow(){
    if(arrowBottom!= null){
      arrowBottom.remove();
    }
    if(arrowUp!= null){
      arrowUp.remove();
    }

    //if(elementInTheTable == 1){
      arrowUp = GUI.createImage(cardsUnoAtlas, GGameStatic.arrowName[elementInTheTable-1]);
      arrowBottom = GUI.createImage(cardsUnoAtlas, GGameStatic.arrowName[elementInTheTable-1]);
      group.addActor(arrowUp);
      group.addActor(arrowBottom);
      arrowUp.setSize(arrowUp.getWidth()*0.25f,arrowUp.getHeight()*0.2f);
      arrowBottom.setSize(arrowBottom.getWidth()*0.25f,arrowBottom.getHeight()*0.2f);
      arrowUp.setAlign(Align.center);
      arrowBottom.setAlign(Align.center);
      arrowUp.setPosition(GMain.screenWidth/2-30, GMain.screenHeight/2 - 100, Align.center);
      arrowBottom.setPosition(GMain.screenWidth/2-30 + arrowBottom.getWidth(), GMain.screenHeight/2 + 200, Align.center);
      arrowBottom.setScale(-1);

      //if()

    //}
    //}
  }

  private void getCard(){

    showArrow();

    if(checkEndGame() != -1){
      Gdx.app.log("debug_board: 89", "winner: " + checkEndGame());
      return;
    }

    if(turnGame%4 != 0){
      cardsAvailable = rules.getAvailableCards(cards.get(turnGame%4), cardInTheTable, elementInTheTable);
      if(cardsAvailable.size == 0){
        Gdx.app.log("debug", "turn: " + turnGame%4 + " rut bai");
        takeCards();
      }
      else {
        Gdx.app.log("debug", "turn: " + turnGame%4 + " danh bai");
        int idCardMove = (int)Math.floor(Math.random()*cardsAvailable.size);
        moveCard(cardsAvailable.get(idCardMove));
      }
    }
    else {
      cardsAvailable = rules.getAvailableCards(cards.get(0), cardInTheTable, elementInTheTable);
      if(cardsAvailable.size == 0){
        Gdx.app.log("debug", "turn: " + turnGame%4 + " rut bai");
        //todo:: goi ham lay bai
        addCard(0, true);
      }
      else {
        Gdx.app.log("debug", "turn: " + turnGame%4 + " danh bai");
        for(Card card : cards.get(0)){
          card.setTouch(Touchable.disabled);
        }

        for(Card card : cardsAvailable){
          card.setTouch(Touchable.enabled);
        }
        upAvailableCards(0);
      }
    }
  }

  private void upAvailableCards(int turnGame){
    if(turnGame == 0){
      for(Card card : cardsAvailable){
        card.image.addAction(moveBy(0, -50, 0.15f, fastSlow));
        card.tileDown.addAction(moveBy(0, -50, 0.15f, fastSlow));
      }
    }
  }

  private void downAvailableCards(int turnGame){
    if(turnGame == 0){
      for(Card card : cardsAvailable){
        card.image.addAction(moveBy(0, 50, 0.15f, fastSlow));
        card.tileDown.addAction(moveBy(0, 50, 0.15f, fastSlow));
      }
    }
  }

  private void takeCards(){
    addCard(turnGame%4, true);
  }

  private Vector3 positionAddCard(int turnGame){
    Vector3 position;
    float rotation = 0;
    float x = 0;
    float y = 0;
    if(turnGame == 1){
      rotation = 90;
    }
    else if(turnGame == 3){
      rotation = -90;
    }

    switch (turnGame) {
      case 0: {
        x = cards.get(turnGame).size*cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[turnGame];
        y = 0;
        break;
      }
      case 1: {
        x = 0;
        y = cards.get(turnGame).size*cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[turnGame];
        break;
      }
      case 2: {
        x = cards.get(turnGame).size*cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[turnGame];
        y = 0;
        break;
      }
      case 3: {
        x = 0;
        y = -1*cards.get(turnGame).size*cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[turnGame];
        break;
      }
    }

    position = new Vector3(x, y, rotation);
    return position;
  }

  private void addCard(int turnGame, boolean flag){
    Card cardTemp = new Card(cardsUnoAtlas, group, this, (int)tiles.get(0).x, (int)tiles.get(0).y);
    Card card = new Card(cardsUnoAtlas, game.groupCards.get(turnGame%4), this, (int) tiles.get(0).x, (int)tiles.get(0).y);
    if(turnGame%4 != 0){
      card.setTouch(Touchable.disabled);
    }
    else {
      card.setTouch(Touchable.enabled);
      card.addDrag();
    }
    tiles.removeIndex(0);
    Vector3 positon = positionAddCard(turnGame%4);
    cards.get(turnGame%4).add(card);
    card.setVisible(false);

    card.setSize(GGameStatic.ratioCardsPlayers[turnGame%4]);
    card.setAlign(Align.center);
    card.setPosition(positon.x, positon.y);
    card.setRotation(positon.z);

    cardTemp.setSize(GGameStatic.ratioCardsPlayers[turnGame%4]);
    cardTemp.setAlign(Align.center);
    cardTemp.setRotation(card.image.getRotation());

    if(turnGame%4 == 0 || (turnGame%4 == 2 && GGameStatic.modeGame == 2)){
      cardTemp.setVisibleTiledown(false);
    }

    cardTemp.image.addAction(sequence(
        moveTo(game.groupCards.get(turnGame%4).getX() + card.image.getX(), game.groupCards.get(turnGame%4).getY() + card.image.getY(), 0.15f, fastSlow),
        GSimpleAction.simpleAction((d, a)->{
          return true;
        })
    ));

    cardTemp.tileDown.addAction(sequence(
        moveTo(game.groupCards.get(turnGame%4).getX() + card.image.getX(), game.groupCards.get(turnGame%4).getY() + card.image.getY(), 0.15f, fastSlow),
        GSimpleAction.simpleAction((d, a)->{
          completeAddCard(cardTemp, card, flag);
          return true;
        })
    ));

  }

  private void completeAddCard(Card cardTemp, Card card, boolean flag){
    cardTemp.setVisible(false);
    if(turnGame%4 == 0 || (turnGame%4 == 2 && GGameStatic.modeGame == 2)){
      card.setVisibleImage(true);
    }
    else {
      card.setVisibleTiledown(true);
    }

    if(turnGame%4 == 0 || (turnGame%4 == 2 && GGameStatic.modeGame == 2)){
      handlingCards.sortCards(cards, turnGame%4, false);
    }

    if(flag){
      Tweens.setTimeout(group, 0.5f, ()->{
        handLingCardInTheTable();
      });
    }

  }



  public void moveCard(Card card){
    Gdx.app.log("debug", "turn: " + turnGame%4);
    cards.get(turnGame%4).removeValue(card, true);

    Card cardTemp = new Card(cardsUnoAtlas, group, this, card.element, card.value);
    cardTemp.setTouch(Touchable.disabled);
    cardTemp.setVisibleTiledown(false);
    cardTemp.setPosition(game.groupCards.get(turnGame%4).getX() + card.image.getX(),game.groupCards.get(turnGame%4).getY() + card.image.getY());
    card.setVisible(false);

    cardTemp.setSize(0.6f);
    cardTemp.setAlign(Align.center);
    cardTemp.rotationCard((float) Math.floor(Math.random()*90 - 90), 0.15f, linear);

    cardInTheTable = cardTemp;
    if(cardTemp.element == 9){
      elementInTheTable = (int) Math.floor(Math.random()*4 + 1);
    }
    else elementInTheTable = cardInTheTable.element;

    cardTemp.image.addAction(sequence(
        moveTo(GGameStatic.positionCenter.x, GGameStatic.positionCenter.y, 0.15f, fastSlow),
        GSimpleAction.simpleAction((d, a)->{
          rightPositionCards(turnGame%4);
          return true;
        })
    ));
  }

  private void rightPositionCards(int indexCards){
    if(indexCards == 0){
      for(Card card : cards.get(0)){
        card.setTouch(Touchable.disabled);
      }
    }

    if(indexCards == 0 || indexCards == 2) {
      for(int i = 0; i < cards.get(indexCards).size; i++) {
        float pX = cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[indexCards]*i;
        cards.get(indexCards).get(i).image.addAction(moveTo(pX, 0, 0.15f, fastSlow));
        cards.get(indexCards).get(i).tileDown.addAction(moveTo(pX, 0, 0.15f, fastSlow));
      }
    }
    else{
      int operator = indexCards%4 == 1 ? 1 : -1;
      for(int i = 0; i < cards.get(indexCards).size; i++) {
        float pY = operator*cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[indexCards]*i;
        cards.get(indexCards).get(i).image.addAction(moveTo(0, pY, 0.15f, fastSlow));
        cards.get(indexCards).get(i).tileDown.addAction(moveTo(0, pY, 0.15f, fastSlow));
      }
    }

    Tweens.setTimeout(group, 0.5f, ()->{
      handLingCardInTheTable();
    });

  }

  private int checkEndGame(){
    int result = -1;
    int minCards = 0;
    if(tiles.size == 0){
      for(int i = 1; i < 4; i++) {
        if(cards.get(i).size < cards.get(minCards).size){
          minCards = i;
        }
      }
      result = minCards;
    }
    else{
      for(int i = 0; i < 4; i++) {
        if(cards.get(i).size == 0){
          result = i;
        }
      }
    }
    return result;
  }

  private void handLingCardInTheTable(){
    if(cardInTheTable.value == 10){ //todo: nguoi choi moi + 2
      if(isRightDirection)
        turnGame++;
      else {
        turnGame--;
        if(turnGame == -1)
          turnGame = 3;
      }

      addCard(turnGame%4, false);
      Tweens.setTimeout(group, 1, ()->{
        addCard(turnGame%4, false);
        Tweens.setTimeout(group, 1, ()->{
          //todo:
          cardsAvailable.clear();
          getCard();
        });
      });
      return;
    }
    else if(cardInTheTable.value == 11){ // todo: nguoi choi moi boc 1 va bo luot
      if(isRightDirection)
        turnGame++;
      else {
        turnGame--;
        if(turnGame == -1)
          turnGame = 3;
      }
      addCard(turnGame%4, false);

      Tweens.setTimeout(group, 1, ()->{
        if(isRightDirection)
          turnGame++;
        else {
          turnGame--;
          if(turnGame == -1)
            turnGame = 3;
        }
        //todo:
        cardsAvailable.clear();
        getCard();
      });
      return;
    }
    else if(cardInTheTable.value == 12){ // todo: doi chieu
      isRightDirection = !isRightDirection;
    }
    else if(cardInTheTable.element == 9 && cardInTheTable.value == 0){ //todo: nguoi choi moi + 4 va chon mau
      elementInTheTable = (int)Math.floor(Math.random()*4 + 1);

      if(isRightDirection)
        turnGame++;
      else {
        turnGame--;
        if(turnGame == -1)
          turnGame = 3;
      }

      addCard(turnGame%4, false);
      Tweens.setTimeout(group, 1, ()->{
        addCard(turnGame%4, false);
        Tweens.setTimeout(group, 1, ()->{
          addCard(turnGame%4, false);
          Tweens.setTimeout(group, 1, ()->{
            addCard(turnGame%4, false);
            Tweens.setTimeout(group, 1, ()->{
              //todo:
              cardsAvailable.clear();
              getCard();
            });
          });
        });
      });
      return;

    }
    else if(cardInTheTable.element == 9 && cardInTheTable.value == 1){//todo: nguoi choi moi chon mau
      elementInTheTable = (int)Math.floor(Math.random()*4 + 1);
    }
    if(isRightDirection)
      turnGame++;
    else {
      turnGame--;
      if(turnGame == -1)
        turnGame = 3;
    }
    //todo:
    cardsAvailable.clear();
    getCard();



  }
}
