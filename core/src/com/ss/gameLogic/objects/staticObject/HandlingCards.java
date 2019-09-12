package com.ss.gameLogic.objects.staticObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.commons.Tweens;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.BoardConfig;
import com.ss.gameLogic.objects.Card;
import com.ss.gameLogic.scene.GGameStatic;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class HandlingCards {
  TextureAtlas cardsUnoAtlas;
  Group group;
  Board board;
  BoardConfig cfg;
  Array<Array<Card>> cardsTemp;
  int turnDistributeCards = 0;
  int countTemp = 0;
  Card cardInTheTable;

  public HandlingCards(TextureAtlas cardsUnoAtlas, Group group, Board board){
    this.cardsUnoAtlas = cardsUnoAtlas;
    this.group = group;
    this.board = board;
  }

  public void shuffleCards(Array<Vector2> tiles){
    for(int i = 1; i < 9; i++) {
      for(int j = 0; j < 14; j++) {
        if(i > 4){
          if(j != 0){
            if(j != 13){
              Vector2 tile = new Vector2(i - 4, j);
              tiles.add(tile);
            }
            else {
              Vector2 tile = new Vector2(9, 0);
              tiles.add(tile);
            }
          }
          else {
            continue;
          }
        }
        else {
          if(j != 13){
            Vector2 tile = new Vector2(i, j);
            tiles.add(tile);
          }
          else {
            Vector2 tile = new Vector2(9, 1);
            tiles.add(tile);
          }
        }
      }
    }
    tiles.shuffle();
    initCardTemp();
    Gdx.app.log("debug", "size tiles: "+ tiles.size);

  }

  private void initCardTemp(){
    cardsTemp = new Array<>();
    Array<Card> cards1 = new Array<>();
    Array<Card> cards2 = new Array<>();
    Array<Card> cards3 = new Array<>();
    Array<Card> cards4 = new Array<>();
    cardsTemp.add(cards1, cards2, cards3, cards4);
  }

//  public void renderCards(Array<Card> cards, Array<Vector2> tiles){
//    int count = 0;
//    Gdx.app.log("debug", "x-y: " + tiles.get(0).x + "-" + tiles.get(0).y);
//    for(Vector2 tile : tiles){
//      Card card = new Card(cardsUnoAtlas, group, (int)tile.x, (int)tile.y);
//      card.setPosition(count*cfg.CW*cfg.ratioCP, 0);
//      cards.add(card);
//      count++;
//    }
//  }

  public void distributeCards(Array<Array<Card>> cards, Array<Vector2> tiles, Array<Vector2> positionCards, Array<Group> groupCards){
    moveCard(turnDistributeCards, cards, tiles, positionCards, groupCards);
  }

  public void moveCard(int turnDistributeCards, Array<Array<Card>> cards, Array<Vector2> tiles, Array<Vector2> positionCards, Array<Group> groupCards){
    if(turnDistributeCards == 29){
      GGameStatic.quantityCard = 29;
      for(Card card : cards.get(0)){
        card.addDrag();
      }
      rotation(cards);
      return;
    }

    float pX = turnDistributeCards != 28 ? positionCards.get(turnDistributeCards%4).x : GGameStatic.positionCenter.x;
    float pY = turnDistributeCards != 28 ? positionCards.get(turnDistributeCards%4).y : GGameStatic.positionCenter.y;

    Gdx.app.log("debug", "turn: " + turnDistributeCards + " x-y: " + GGameStatic.positionCenter.x + "-" + GGameStatic.positionCenter.y);

    Card tempCard = new Card(cardsUnoAtlas, group, board, (int)tiles.get(0).x, (int)tiles.get(0).y);
    Card card = new Card(cardsUnoAtlas, groupCards.get(turnDistributeCards%4), board, (int)tiles.get(0).x, (int)tiles.get(0).y);
    tiles.removeIndex(0);
    card.setVisible(false);

    if(turnDistributeCards != 28){
      cards.get(turnDistributeCards%4).add(card);
      cardsTemp.get(turnDistributeCards%4).add(tempCard);
    }
    else {
      tempCard.setSize(0.6f);
      card.setVisible(false);
      cardInTheTable = tempCard;
      cardInTheTable.setTouch(Touchable.disabled);
    }

    if((GGameStatic.modeGame == 2 && (turnDistributeCards%4 == 0 || turnDistributeCards%4 == 2)) || (GGameStatic.modeGame == 1 && turnDistributeCards%4 == 0)) {
      tempCard.setVisibleTiledown(false);
    }

    float rotation = (float) Math.floor(Math.random()*270);

    card.setSize(GGameStatic.ratioCardsPlayers[turnDistributeCards%4]);
    tempCard.setSize(GGameStatic.ratioCardsPlayers[turnDistributeCards%4]);

    tempCard.image.setOrigin(Align.center);
    tempCard.tileDown.setOrigin(Align.center);
    card.image.setOrigin(Align.center);
    card.tileDown.setOrigin(Align.center);

    tempCard.image.addAction(parallel(
      moveTo(pX, pY, 0.2f, linear),
      rotateTo(rotation, 0.2f, linear)
    ));
    tempCard.tileDown.addAction(sequence(
      parallel(
        moveTo(pX, pY, 0.2f, linear),
        rotateTo(rotation, 0.2f, linear)
      ),
      GSimpleAction.simpleAction((d, a)->{
        Gdx.app.log("debug", "done_90_Handling");
        addTurnDistributeCards(cards, tiles, positionCards, groupCards);
        return true;
      })
    ));
  }

  public Card getCardInTheTable(){
    return cardInTheTable;
  }

  private void addTurnDistributeCards( Array<Array<Card>> cards, Array<Vector2> tiles, Array<Vector2> positionCards, Array<Group> groupCards){
    turnDistributeCards++;
    moveCard(turnDistributeCards, cards, tiles, positionCards, groupCards);
  }

  private void rotation(Array<Array<Card>> cards){
    for(int i = 0; i < 4; i++) {
      float rotation = (i == 0 || i == 2) ? 0 : i == 1 ? 90 : -90; //todo: i == 0 || i == 2 -> rotation = 0, i == 1 -> rotation = 90, i == 3 -> rotation = -90
      for(int j = 0; j < cardsTemp.get(i).size; j++){
        cardsTemp.get(i).get(j).rotationCard(rotation, 0.3f, linear);
        cards.get(i).get(j).rotationCard(rotation, 0.3f, linear);

      }
    }

    Tweens.setTimeout(group, 0.3f, ()->{
      AlignCards(cards);
    });
  }

  private void AlignCards(Array<Array<Card>> cards){
    for(int i = 0; i < 4; i++) {
      for(int j = 0; j < cardsTemp.get(i).size; j++) {
        if(i%4 == 0 || i%4 == 2) {
          cardsTemp.get(i).get(j).image.addAction(
              moveBy((cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[i])*j, 0, 0.3f, linear));
          cardsTemp.get(i).get(j).tileDown.addAction(
              moveBy((cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[i])*j, 0, 0.3f, linear));
          cards.get(i).get(j).image.addAction(
              moveBy((cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[i])*j, 0, 0.3f, linear));
          cards.get(i).get(j).tileDown.addAction(
              moveBy((cfg.CW*0.4f*GGameStatic.ratioCardsPlayers[i])*j, 0, 0.3f, linear));

        }
        else {
          int operator = i%4 == 1 ? 1 : -1;
          cardsTemp.get(i).get(j).image.addAction(
              moveBy(0, operator*(cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[i])*j, 0.3f, linear));
          cardsTemp.get(i).get(j).tileDown.addAction(
              moveBy(0, operator*(cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[i])*j, 0.3f, linear));
          cards.get(i).get(j).image.addAction(
              moveBy(0, operator*(cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[i])*j, 0.3f, linear));
          cards.get(i).get(j).tileDown.addAction(
              moveBy(0, operator*(cfg.CW*0.2f*GGameStatic.ratioCardsPlayers[i])*j, 0.3f, linear));
        }
      }
    }
    Tweens.setTimeout(group, 0.4f, ()->{
      showGroupCards(cards);
      clearCardsTemp();
      sortCards(cards, 0, true);
      if(GGameStatic.modeGame == 2) {
        sortCards(cards, 2, true);
      }
    });
  }

  private void showGroupCards(Array<Array<Card>> cards){
    for(int i = 0; i < cards.size; i++) {
      for(int j = 0; j < cards.get(i).size; j++) {
        if((GGameStatic.modeGame == 2 && (i == 0 || i == 2)) || (GGameStatic.modeGame == 1 && i == 0)){
          cards.get(i).get(j).setVisibleImage(true);
        }
        else {
          cards.get(i).get(j).setVisibleTiledown(true);
        }
      }
    }
  }

  private void clearCardsTemp(){
    for(int i = 0; i < cardsTemp.size; i++) {
      for(int j = 0; j < cardsTemp.get(i).size; j++) {
        cardsTemp.get(i).get(j).setVisible(false);
      }
    }
  }

  public void sortCards(Array<Array<Card>> cards, int positionCards, boolean isBeforePlay){
    sortCards2(cards, positionCards, 0, isBeforePlay);
  }

  private void sortCards2(Array<Array<Card>> cards, int positionCards, int index, boolean isBeforePlay){
    if(index >= cards.get(positionCards).size - 1){
      if(((GGameStatic.modeGame == 2 && positionCards == 2) ||(GGameStatic.modeGame == 1 && positionCards == 0)) && isBeforePlay){
        Tweens.setTimeout(group, 1, ()->{
          board.doneRenderStart();
        });
      }
      return;
    }
    if(cards.get(positionCards).get(index+1).element != cards.get(positionCards).get(index).element){
      for(int j = index + 1; j < cards.get(positionCards).size; j++){
        if(cards.get(positionCards).get(j).element == cards.get(positionCards).get(index).element){
          countTemp++;
          Vector2 position1 = new Vector2(cards.get(positionCards).get(index+1).image.getX(), cards.get(positionCards).get(index+1).image.getY());
          Vector2 position2 = new Vector2(cards.get(positionCards).get(j).image.getX(), cards.get(positionCards).get(j).image.getY());

          cards.get(positionCards).get(index+1).image.addAction(moveTo(position2.x, position2.y, 0.2f, linear));
          cards.get(positionCards).get(index+1).tileDown.addAction(moveTo(position2.x, position2.y, 0.2f, linear));
          cards.get(positionCards).get(j).image.addAction(moveTo(position1.x, position1.y, 0.2f, linear));
          cards.get(positionCards).get(j).tileDown.addAction(moveTo(position1.x, position1.y, 0.2f, linear));

          int ZIndexTemp = cards.get(positionCards).get(index+1).image.getZIndex();
          cards.get(positionCards).get(index+1).image.setZIndex(cards.get(positionCards).get(j).image.getZIndex());
          cards.get(positionCards).get(j).image.setZIndex(ZIndexTemp);
          final int indexTemp = index + 1;
          cards.get(positionCards).swap(index+1, j);
          Tweens.setTimeout(group, 0.2f, ()->{
            sortCards2(cards, positionCards, indexTemp, isBeforePlay);
          });
          return;
        }
      }
    }
    sortCards2(cards, positionCards, ++index, isBeforePlay);
  }
}
