/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class GameMainInfrastructure {

    public static double WINDOW_WIDTH = 1024;
    public static double WINDOW_HEIGH = 720;
    public static int FRAMERATE = 1;
    public static double windowPositionX = 0.0;
    public static double windowPositionY = 0.0;

    private static Timeline gameLoop;

    private List<GameObject> gameStaticObjectsList = new ArrayList<GameObject>();
    private GameObject startObject;
    private GameObject endObject;

    private Pathfinding pathfinding;

    public GameMainInfrastructure(Stage stage, VBox gamePanel) throws Exception {
        StackPane gameCanvasPanel = new StackPane();
        changeCanvasWidthAndHeighToFullSize();

        final Canvas baseCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGH);
        GraphicsContext enviromentGraphicsContext = baseCanvas.getGraphicsContext2D();
        createObjects(enviromentGraphicsContext);

        gameCanvasPanel.getChildren().add(baseCanvas);
        VBox gameVerticalPanel = new VBox();
        gameVerticalPanel.getChildren().add(gameCanvasPanel);
        gamePanel.getChildren().add(gameVerticalPanel);

        pathfinding = new Pathfinding(gameStaticObjectsList, startObject, endObject, enviromentGraphicsContext);

        buildAndSetGameLoop(stage);
    }

    private void createObjects(GraphicsContext graphicsContext) {
        gameStaticObjectsList.add(new GameObject(300, 300, 150, 300, graphicsContext, Color.BLACK));
        gameStaticObjectsList.add(new GameObject(600, 600, 100, 100, graphicsContext, Color.BLACK));
        gameStaticObjectsList.add(new GameObject(400, 800, 150, 50, graphicsContext, Color.BLACK));
        gameStaticObjectsList.add(new GameObject(50, 700, 300, 25, graphicsContext, Color.BLACK));
        gameStaticObjectsList.add(new GameObject(1000, 100, 100, 300, graphicsContext, Color.BLACK));

        Random random = new Random();
        startObject = new GameObject(1, random.nextDouble() * 800, 50, 50, graphicsContext, Color.GREEN);
        endObject = new GameObject(1400, random.nextDouble() * 800, 50, 50, graphicsContext, Color.BLUE);
    }

    private void paintAllObjects() {
        for (GameObject gameObject : gameStaticObjectsList) {
            gameObject.paintGameObject();
        }
        startObject.paintGameObject();
        endObject.paintGameObject();
    }

    private void changeCanvasWidthAndHeighToFullSize() {
        WINDOW_WIDTH = Screen.getPrimary().getVisualBounds().getMaxX();
        WINDOW_HEIGH = Screen.getPrimary().getVisualBounds().getMaxY() - 100;
    }

    private void buildAndSetGameLoop(final Stage stage) {
        final Duration oneFrameDuration = Duration.millis(1000 / FRAMERATE);
        final KeyFrame oneFrame = new KeyFrame(oneFrameDuration,
                new EventHandler() {

            /**
             * Everything inside this handle is what will be repeated in every
             * game loop. Move objects here, detect colisions etc.
             */
            @Override
            public void handle(Event event) {
                paintAllObjects();
                pathfinding.createPath();
            }

        });

        setGameLoop(TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(oneFrame)
                .build());
    }

    protected static void setGameLoop(Timeline gameLoop) {
        GameMainInfrastructure.gameLoop = gameLoop;
    }

    public void beginGameLoop() {
        gameLoop.play();
    }

    public void stopGameLoop() {
        gameLoop.stop();
    }

}
