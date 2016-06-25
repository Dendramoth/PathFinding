/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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

    private double intersectionXDetected = 0;
    private double intersectionYDetected = 0;

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
                getIntersectionPointCoordinates(intersection);
                graphicsContext.fillOval(intersectionXDetected - 5, intersectionYDetected - 5, 11, 11);

                findPolygonTouchedLineCorners(gameStaticObject, new Rectangle(intersectionXDetected - 1, intersectionYDetected - 1, 3, 3));
            }

        }
    }

    private void getIntersectionPointCoordinates(Shape intersection) {
        if (startGameObject.getPossitionX() < endGameObject.getPossitionX()) {
            intersectionXDetected = intersection.getLayoutBounds().getMinX();
        } else {
            intersectionXDetected = intersection.getLayoutBounds().getMaxX();
        }

        if (startGameObject.getPossitionY() < endGameObject.getPossitionY()) {
            intersectionYDetected = intersection.getLayoutBounds().getMinY();
        } else {
            intersectionYDetected = intersection.getLayoutBounds().getMaxY();
        }
    }

    private void findPolygonTouchedLineCorners(GameObject gameObject, Shape shape) {
        for (Line line : gameObject.getPolygonLineList()) {
            Shape intersection = Shape.intersect(line, shape);
            if (!(intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0)) {
                graphicsContext.fillOval(line.getEndX() - 5, line.getEndY() - 5, 10, 10);
                graphicsContext.fillOval(line.getStartX() - 5, line.getStartY() - 5, 10, 10);
                break;
            }
        }
    }

}
