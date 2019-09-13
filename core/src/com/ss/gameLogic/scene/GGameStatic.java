package com.ss.gameLogic.scene;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;

public class GGameStatic {
  //todo:: Atlas
  public static TextureAtlas cardsUnoAtlas = GAssetsManager.getTextureAtlas("Cards/cardsUno.atlas");
  public static TextureAtlas uiUno = GAssetsManager.getTextureAtlas("UIUno/uiUno.atlas");

  //todo:: UI
  public static String bgStr = "bg1";
  public static String tileDownStr = "04";

  //todo:: ratioScaleCardPlayers
  public static float[] ratioCardsPlayers = new float[]{1, 0.6f, 0.6f, 0.6f};

  //todo:: mode game
  public static int modeGame = 2;

  //todo:: position card in the table
  public static Vector2 positionCenter = new Vector2(GMain.screenWidth/2 - 70, GMain.screenHeight/2 - 85);

  //todo:: quantityCard
  public static int quantityCard = 0;

  //todo:: nameArrow
  public static String[] arrowName = new String[]{"blueArrow", "greenArrow", "yellowArrow", "redArrow"};
}
