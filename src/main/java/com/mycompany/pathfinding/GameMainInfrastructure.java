/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.Event;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class GameMainInfrastructure {

    public static double WINDOW_WIDTH = 1024;
    public static double WINDOW_HEIGH = 720;
    public static int FRAMERATE = 60;
    public static double windowPositionX = 0.0;
    public static double windowPositionY = 0.0;

    private static Timeline gameLoop;

    private List<GameObject> gameStaticObjectsList = new ArrayList<GameObject>();
    private GameObject startObject;
    private GameObject endObject;
    private Pathfinding pathfinding;

    private GraphicsContext graphicsContext;
    List<Point> listOfPathPoints = new ArrayList<Point>();

    public GameMainInfrastructure(Stage stage, VBox gamePanel) throws Exception {
        StackPane gameCanvasPanel = new StackPane();
        changeCanvasWidthAndHeighToFullSize();

        final Canvas baseCanvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGH);
        GraphicsContext enviromentGraphicsContext = baseCanvas.getGraphicsContext2D();
        createObjects(enviromentGraphicsContext);
        this.graphicsContext = enviromentGraphicsContext;

        gameCanvasPanel.getChildren().add(baseCanvas);
        VBox gameVerticalPanel = new VBox();
        gameVerticalPanel.getChildren().add(gameCanvasPanel);
        gamePanel.getChildren().add(gameVerticalPanel);

        pathfinding = new Pathfinding(gameStaticObjectsList, startObject, endObject, enviromentGraphicsContext);
        listOfPathPoints = pathfinding.createPath(startObject.getPossitionX(), startObject.getPossitionY(), endObject.getPossitionX(), endObject.getPossitionY());

        buildAndSetGameLoop();
    }

    private void createObjects(GraphicsContext graphicsContext) {
        List<Point> pointList = new ArrayList<Point>();

        Point centerPoint = new Point(450, 700);
        pointList.add(new Point(600, 100));
        pointList.add(new Point(800, 100));
        pointList.add(new Point(800, 600));
        pointList.add(new Point(600, 600));
        gameStaticObjectsList.add(new GameObject(pointList, centerPoint, graphicsContext, Color.BLACK));

        centerPoint = new Point(300, 300);
        pointList.clear();
        pointList.add(new Point(200, 200));
        pointList.add(new Point(500, 200));
        pointList.add(new Point(500, 500));
        pointList.add(new Point(300, 500));
        pointList.add(new Point(300, 300));
        pointList.add(new Point(200, 300));
        gameStaticObjectsList.add(new GameObject(pointList, centerPoint, graphicsContext, Color.BLACK));

        centerPoint = new Point(350, 800);
        pointList.clear();
        pointList.add(new Point(200, 700));
        pointList.add(new Point(300, 600));
        pointList.add(new Point(400, 600));
        pointList.add(new Point(500, 700));
        pointList.add(new Point(500, 800));
        pointList.add(new Point(400, 900));
        pointList.add(new Point(300, 900));
        pointList.add(new Point(200, 800));
        gameStaticObjectsList.add(new GameObject(pointList, centerPoint, graphicsContext, Color.BLACK));

        centerPoint = new Point(1550, 550);
        pointList.clear();
        pointList.add(new Point(1500, 100));
        pointList.add(new Point(1600, 100));
        pointList.add(new Point(1600, 750));
        pointList.add(new Point(1500, 750));
        gameStaticObjectsList.add(new GameObject(pointList, centerPoint, graphicsContext, Color.BLACK));

        Random random = new Random();
        double randomPoss = random.nextDouble() * 800;
        centerPoint = new Point(25, randomPoss + 25);
        pointList.clear();
        pointList.add(new Point(1, randomPoss));
        pointList.add(new Point(50, randomPoss));
        pointList.add(new Point(50, randomPoss + 50));
        pointList.add(new Point(1, randomPoss + 50));
        startObject = new GameObject(pointList, centerPoint, graphicsContext, Color.GREEN);

        randomPoss = random.nextDouble() * 800;
        centerPoint = new Point(1725, randomPoss + 25);
        pointList.clear();
        pointList.add(new Point(1700, randomPoss));
        pointList.add(new Point(1750, randomPoss));
        pointList.add(new Point(1750, randomPoss + 50));
        pointList.add(new Point(1700, randomPoss + 50));
        endObject = new GameObject(pointList, centerPoint, graphicsContext, Color.BLUE);
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

    private void buildAndSetGameLoop() {
        final Duration oneFrameDuration = Duration.millis(1000 / FRAMERATE);
        final KeyFrame oneFrame = new KeyFrame(oneFrameDuration,
                new EventHandler() {

                    /**
                     * Everything inside this handle is what will be repeated in
                     * every game loop. Move objects here, detect collisions
                     * etc.
                     */
                    @Override
                    public void handle(Event event) {
                        graphicsContext.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGH);
                        paintAllObjects();

                        pathfinding.paintAllPathPoints();
                        startObject.moveGameObject(firstPointWhichWasntReachedYet(listOfPathPoints, startObject));
                    }

                });

        setGameLoop(TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(oneFrame)
                .build());
    }

    private Point firstPointWhichWasntReachedYet(List<Point> listOfPathPoints, GameObject gameObject) {
        Iterator<Point> iterator = listOfPathPoints.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            if (!point.isPointWasReached()) {
                if ((point.getCoordX() > gameObject.getPossitionX() - 1.5 && point.getCoordX() < gameObject.getPossitionX() + 1.5)
                        && (point.getCoordY() > gameObject.getPossitionY() - 1.5 && point.getCoordY() < gameObject.getPossitionY() + 1.5)) { //point was reached
                    iterator.remove();
                } else {
                    return point;
                }
            }
        }
        
        return new Point(0, 0);
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
