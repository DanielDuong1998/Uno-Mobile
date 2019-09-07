package com.ss.gameLogic.objects.staticObject;

import com.badlogic.gdx.utils.Array;
import com.ss.gameLogic.objects.Card;

public class Rules {
  public Rules(){

  }

  public Array<Card> getAvailableCards(Array<Card> cards, Card cardInTheTable, int elementInTheTable){
    Array<Card> cardsAvailableCards = new Array<>();
    for(Card card : cards){
      if(check2Card(card, cardInTheTable, elementInTheTable)){
        cardsAvailableCards.add(card);
      }
    }
    return cardsAvailableCards;
  }

  private boolean check2Card(Card cardCheck, Card cardInTheTable, int elementInTheTable){
    if(cardCheck.element == 9){
      return true;
    }
    else {
      if(cardCheck.element == elementInTheTable || cardCheck.value == cardInTheTable.value)
        return true;
      else return false;
    }
  }
}
