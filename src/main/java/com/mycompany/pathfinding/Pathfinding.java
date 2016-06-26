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

    public Pathfinding(List<GameObject> gameStaticObjectsList, GameObject startGameObject, GameObject endGameObject, GraphicsContext graphicsContext) {
        this.gameStaticObjectsList = gameStaticObjectsList;
        this.startGameObject = startGameObject;
        this.endGameObject = endGameObject;
        this.graphicsContext = graphicsContext;
    }

    public void createPathDetectCollisionWithObjects(double startPointX, double startPointY, double targetPointX, double targetPointY, PathPoint pathPoint) {
        Line line = new Line(startPointX, startPointY, targetPointX, targetPointY);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeLine(startPointX, startPointY, targetPointX, targetPointY);
        boolean point2IsVisibleFromPoint1 = true;

        for (GameObject gameStaticObject : gameStaticObjectsList) {
            Shape intersection = gameStaticObject.detectIntersection(line);
            if (!(intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0)) {
                IntersectionPoint intersectionPoint = getIntersectionPointCoordinates(intersection);
                point2IsVisibleFromPoint1 = false;

                graphicsContext.fillOval(intersectionPoint.getCoordX() - 5, intersectionPoint.getCoordY() - 5, 11, 11);
                FindPathAroundObject findPathAroundObject = new FindPathAroundObject(intersectionPoint.getCoordX(), intersectionPoint.getCoordY(), targetPointX, targetPointY, gameStaticObject, graphicsContext);
            }
        }

        if (point2IsVisibleFromPoint1) {
        } else {

        }
    }
    
    private IntersectionPoint getIntersectionPointCoordinates(Shape intersection) {
        IntersectionPoint intersectionPoint = new IntersectionPoint();

        if (startGameObject.getPossitionX() < endGameObject.getPossitionX()) {
            intersectionPoint.setCoordX(intersection.getLayoutBounds().getMinX());
        } else {
            intersectionPoint.setCoordX(intersection.getLayoutBounds().getMaxX());
        }

        if (startGameObject.getPossitionY() < endGameObject.getPossitionY()) {
            intersectionPoint.setCoordY(intersection.getLayoutBounds().getMinY());
        } else {
            intersectionPoint.setCoordY(intersection.getLayoutBounds().getMaxY());
        }

        return intersectionPoint;
    }

    private static class IntersectionPoint {

        private double coordX = 0;
        private double coordY = 0;

        public IntersectionPoint() {
        }

        public double getCoordX() {
            return coordX;
        }

        public void setCoordX(double coordX) {
            this.coordX = coordX;
        }

        public double getCoordY() {
            return coordY;
        }

        public void setCoordY(double coordY) {
            this.coordY = coordY;
        }

    }

}
