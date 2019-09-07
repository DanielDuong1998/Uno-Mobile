package com.ss.gameLogic.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.objects.Board;
import com.ss.gameLogic.objects.Card;

public class GGameMain extends GScreen {
    TextureAtlas uiUno;
    TextureAtlas cardsUnoAtlas;
    Group menuGroup;
    Board board;
    public Array<Vector2> positionCards;
    public Array<Group> groupCards;

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        menuGroup = new Group();
        GStage.addToLayer(GLayer.ui, menuGroup);

        uiUno = GGameStatic.uiUno;
        cardsUnoAtlas = GGameStatic.cardsUnoAtlas;

        Image bg = GUI.createImage(uiUno, GGameStatic.bgStr);
        menuGroup.addActor(bg);
        initPosition();
        initGroup();
        board = new Board(cardsUnoAtlas, menuGroup, this);
    }

    public void initPosition(){
        initPositionCards();
    }

    public void initGroup(){
        initGroupCards();
    }

    public void initPositionCards(){
        positionCards = new Array<>();
        Vector2 p1 = new Vector2(GMain.screenWidth/2 - 300, GMain.screenHeight-200);
        Vector2 p2 = new Vector2(GMain.screenWidth*0.15f, GMain.screenHeight/2 - 200);
        Vector2 p3 = new Vector2(GMain.screenWidth/2 - 100, 0);
        Vector2 p4 = new Vector2(GMain.screenWidth*0.7f, GMain.screenHeight/2 - 70);
        positionCards.add(p1, p2, p3, p4);
    }

    public void initGroupCards(){
        groupCards = new Array<>();
        Group gc1 = new Group();
        Group gc2 = new Group();
        Group gc3 = new Group();
        Group gc4 = new Group();

        gc1.setPosition(positionCards.get(0).x, positionCards.get(0).y, Align.center);
        gc2.setPosition(positionCards.get(1).x, positionCards.get(1).y, Align.center);
        gc3.setPosition(positionCards.get(2).x, positionCards.get(2).y, Align.center);
        gc4.setPosition(positionCards.get(3).x, positionCards.get(3).y, Align.center);

        menuGroup.addActor(gc1);
        menuGroup.addActor(gc2);
        menuGroup.addActor(gc3);
        menuGroup.addActor(gc4);
        groupCards.add(gc1, gc2, gc3, gc4);

    }

    @Override
    public void run() {

    }
}
