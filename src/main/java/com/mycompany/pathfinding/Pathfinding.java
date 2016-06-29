/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Point> listOfPathPoints = new ArrayList<Point>();

    public Pathfinding(List<GameObject> gameStaticObjectsList, GameObject startGameObject, GameObject endGameObject, GraphicsContext graphicsContext) {
        this.gameStaticObjectsList = gameStaticObjectsList;
        this.startGameObject = startGameObject;
        this.endGameObject = endGameObject;
        this.graphicsContext = graphicsContext;
    }

    public void createPath(double startPointX, double startPointY, double targetPointX, double targetPointY) {
        
        boolean point2IsVisibleFromPoint1 = true;
        
        sortStaticObjectsBasedOnDistanceFromPlayer();

        Point currentPoint = new Point(startPointX, startPointY);
        listOfPathPoints.add(currentPoint);
        
        for (GameObject gameStaticObject : gameStaticObjectsList) {
            Line line = new Line(currentPoint.getCoordX(), currentPoint.getCoordY(), targetPointX, targetPointY);
            
            Shape intersection = gameStaticObject.detectIntersection(line);
            if (!(intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0)) {
                Point intersectionPoint = getIntersectionPointCoordinates(intersection);
                point2IsVisibleFromPoint1 = false;
                
                graphicsContext.setStroke(Color.RED);
                graphicsContext.strokeLine(currentPoint.getCoordX(), currentPoint.getCoordY(), intersectionPoint.getCoordX(), intersectionPoint.getCoordY());

                graphicsContext.setFill(Color.BLACK);
                graphicsContext.fillOval(intersectionPoint.getCoordX() - 5, intersectionPoint.getCoordY() - 5, 11, 11);
                FindPathAroundObject findPathAroundObject = new FindPathAroundObject(intersectionPoint.getCoordX(), intersectionPoint.getCoordY(), targetPointX, targetPointY, gameStaticObject, graphicsContext, listOfPathPoints);
                currentPoint = findPathAroundObject.findPathAroundObject();
                listOfPathPoints.add(currentPoint);
            }
        }
        
        listOfPathPoints.add(new Point(endGameObject.getPossitionX(),endGameObject.getPossitionY()));
        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeLine(currentPoint.getCoordX(), currentPoint.getCoordY(), endGameObject.getPossitionX(), endGameObject.getPossitionY());

        if (point2IsVisibleFromPoint1) {
            graphicsContext.setStroke(Color.RED);
            graphicsContext.strokeLine(startPointX, startPointY, targetPointX, targetPointY);
        }
    }

    private Point getIntersectionPointCoordinates(Shape intersection) {
        Point intersectionPoint = new Point(0, 0);

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

    private void sortStaticObjectsBasedOnDistanceFromPlayer() {
        for (int i = 0; i < gameStaticObjectsList.size(); i++) {
            gameStaticObjectsList.get(i).setObjectForComparisonPosX(startGameObject.getPossitionOnCanvasX());
            gameStaticObjectsList.get(i).setObjectForComparisonPosY(startGameObject.getPossitionOnCanvasY());
        }
        Collections.sort(gameStaticObjectsList);
    }
    
    public void paintAllPathPoints(){
        for (int i = 0; i < listOfPathPoints.size(); i++) {
            Point point = listOfPathPoints.get(i);
            graphicsContext.setFill(Color.AQUA);
            graphicsContext.fillOval(point.getCoordX() - 5, point.getCoordY() - 5, 10, 10);
        }
    }

}
