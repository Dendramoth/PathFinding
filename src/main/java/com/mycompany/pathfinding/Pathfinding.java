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
    private GraphicsContext graphicsContext;
    private List<Point> listOfPathPoints = new ArrayList<Point>();

    public Pathfinding(List<GameObject> gameStaticObjectsList, GraphicsContext graphicsContext) {
        this.gameStaticObjectsList = gameStaticObjectsList;
        this.graphicsContext = graphicsContext;
    }

    public List<Point> createPath(GameObject startGameObject, double targetPointX, double targetPointY) {
        
        boolean point2IsVisibleFromPoint1 = true;
        
        sortStaticObjectsBasedOnDistanceFromPlayer(startGameObject);

        Point currentPoint = new Point(startGameObject.getPossitionX(), startGameObject.getPossitionY());
    //    listOfPathPoints.add(currentPoint);
        
        for (GameObject gameStaticObject : gameStaticObjectsList) {
            Line line = new Line(currentPoint.getCoordX(), currentPoint.getCoordY(), targetPointX, targetPointY);
            
            Shape intersection = gameStaticObject.detectIntersection(line);
            if (!(intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0)) {
                Point intersectionPoint = getIntersectionPointCoordinates(intersection, startGameObject, targetPointX, targetPointY);
                point2IsVisibleFromPoint1 = false;
                
                graphicsContext.setStroke(Color.RED);
                graphicsContext.strokeLine(currentPoint.getCoordX(), currentPoint.getCoordY(), intersectionPoint.getCoordX(), intersectionPoint.getCoordY());

                graphicsContext.setFill(Color.BLACK);
                graphicsContext.fillOval(intersectionPoint.getCoordX() - 5, intersectionPoint.getCoordY() - 5, 11, 11);
                FindPathAroundObject findPathAroundObject = new FindPathAroundObject(targetPointX, targetPointY, gameStaticObject, graphicsContext, listOfPathPoints);
                currentPoint = findPathAroundObject.findPathAroundObject(intersectionPoint);
                listOfPathPoints.add(currentPoint);
            }
        }
        
        listOfPathPoints.add(new Point(targetPointX, targetPointY));
        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeLine(currentPoint.getCoordX(), currentPoint.getCoordY(), targetPointX, targetPointY);

        if (point2IsVisibleFromPoint1) {
            graphicsContext.setStroke(Color.RED);
            graphicsContext.strokeLine(startGameObject.getPossitionX(), startGameObject.getPossitionY(), targetPointX, targetPointY);
        }
        
        return listOfPathPoints;
    }

    private Point getIntersectionPointCoordinates(Shape intersection, GameObject startGameObject, double targetPointX, double targetPointY) {
        Point intersectionPoint = new Point(0, 0);

        if (startGameObject.getPossitionX() < targetPointX) {
            intersectionPoint.setCoordX(intersection.getLayoutBounds().getMinX());
        } else {
            intersectionPoint.setCoordX(intersection.getLayoutBounds().getMaxX());
        }

        if (startGameObject.getPossitionY() < targetPointY) {
            intersectionPoint.setCoordY(intersection.getLayoutBounds().getMinY());
        } else {
            intersectionPoint.setCoordY(intersection.getLayoutBounds().getMaxY());
        }

        return intersectionPoint;
    }

    private void sortStaticObjectsBasedOnDistanceFromPlayer(GameObject startGameObject) {
        for (int i = 0; i < gameStaticObjectsList.size(); i++) {
            gameStaticObjectsList.get(i).setObjectForComparisonPosX(startGameObject.getPossitionX());
            gameStaticObjectsList.get(i).setObjectForComparisonPosY(startGameObject.getPossitionY());
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
