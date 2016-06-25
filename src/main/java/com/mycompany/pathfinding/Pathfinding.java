/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author Dendra
 */
public class Pathfinding {

    private List<GameObject> gameStaticObjectsList = new ArrayList<GameObject>();
    private GameObject startGameObject;
    private GameObject endGameObject;
    private GraphicsContext graphicsContext;

    public Pathfinding(List<GameObject> gameStaticObjectsList, GameObject startGameObject, GameObject endGameObject, GraphicsContext graphicsContext) {
        this.gameStaticObjectsList = gameStaticObjectsList;
        this.startGameObject = startGameObject;
        this.endGameObject = endGameObject;
        this.graphicsContext = graphicsContext;
    }

    public void createPath() {
        Line line = new Line(startGameObject.getPossitionX(), startGameObject.getPossitionY(), endGameObject.getPossitionX(), endGameObject.getPossitionY());
        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeLine(startGameObject.getPossitionX(), startGameObject.getPossitionY(), endGameObject.getPossitionX(), endGameObject.getPossitionY());
        for (GameObject gameStaticObject : gameStaticObjectsList) {
            Shape intersection = gameStaticObject.detectIntersection(line);
            if (intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0) {
            } else {
                System.out.println("------------------------");
                System.out.println(intersection.getLayoutBounds().getMinX());
                System.out.println(intersection.getLayoutBounds().getMinY());
                graphicsContext.fillOval(intersection.getLayoutBounds().getMinX() - 5, intersection.getLayoutBounds().getMinY() - 5, 10, 10);
            }

        }
    }

}
