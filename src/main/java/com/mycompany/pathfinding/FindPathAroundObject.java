/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Dendra
 */
public class FindPathAroundObject {

    private double currentX;
    private double currentY;
    private double targetX;
    private double targetY;
    private double lengthOfGoingAroundTheObjectFromLeft = 0;
    private double lengthOfGoingAroundTheObjectFromRight = 0;

    private boolean pathFoundToTheRight;
    private boolean pathFoundToTheLeft;

    private GameObject gameObject;
    private GraphicsContext graphicsContext;
    private List<Point> leftListOfPathPointsAroundObject = new ArrayList<Point>();
    private List<Point> rightListOfPathPointsAroundObject = new ArrayList<Point>();
    private List<Point> listOfPathPoints = new ArrayList<Point>();

    public FindPathAroundObject(double currentX, double currentY, double targetX, double targetY, GameObject gameObject, GraphicsContext graphicsContext, List<Point> listOfPathPoints) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.gameObject = gameObject;
        this.graphicsContext = graphicsContext;
        this.listOfPathPoints = listOfPathPoints;

        this.listOfPathPoints.add(new Point(currentX, currentY)); // add intersection point with object
    }

    public Point findPathAroundObject() {
        int linesInPolygon = gameObject.getPolygonLineList().size();
        int indexOfCrossedLineInObjectLineList = findCornersOfIntersectedLineOfPolygon(gameObject, new Rectangle(currentX - 1, currentY - 1, 3, 3));
        
        detectVisibilityOfFinalPointFromCornersOfFirstLineWithCollision(indexOfCrossedLineInObjectLineList);

        for (int i = 1; i < gameObject.getPolygonLineList().size() + 1; i++) {
            if (!pathFoundToTheLeft) {
                detectVisibilityFromLeftLine(indexOfCrossedLineInObjectLineList, i, linesInPolygon);
            }
            if (!pathFoundToTheRight) {
                detectVisibilityFromRightLine(indexOfCrossedLineInObjectLineList, i, linesInPolygon);
            }

            if (pathFoundToTheLeft && pathFoundToTheRight) {
                if (leftListOfPathPointsAroundObject.size() < rightListOfPathPointsAroundObject.size()) {
                    listOfPathPoints.addAll(leftListOfPathPointsAroundObject);
                    return leftListOfPathPointsAroundObject.get(leftListOfPathPointsAroundObject.size() - 1);
                } else {
                    listOfPathPoints.addAll(rightListOfPathPointsAroundObject);
                    return rightListOfPathPointsAroundObject.get(rightListOfPathPointsAroundObject.size() - 1);
                }
            }

        }
        return null;
    }

    private void detectVisibilityOfFinalPointFromCornersOfFirstLineWithCollision(int indexOfCrossedLineInObjectLineList) {
        Line line = gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList); //get line of object crossed by path to final destination

        rightListOfPathPointsAroundObject.add(new Point(line.getStartX(), line.getStartY()));
        Point pointToTest = new Point(line.getStartX(), line.getStartY());
        if (detectVisibilityOfFinalPointFromPoint(pointToTest)) {
            pathFoundToTheRight = true;
        }

        leftListOfPathPointsAroundObject.add(new Point(line.getEndX(), line.getEndY()));
        pointToTest = new Point(line.getEndX(), line.getEndY());
        if (detectVisibilityOfFinalPointFromPoint(pointToTest)) {
            pathFoundToTheLeft = true;
        }
    }

    private void detectVisibilityFromRightLine(int indexOfCrossedLineInObjectLineList, int iteration, int linesInPolygon) {
        Point currentPoint;
        int indexOfRightLine = myMod(indexOfCrossedLineInObjectLineList - iteration, linesInPolygon);
        currentPoint = detectVisibilityOfFinalPointFromLine(gameObject.getPolygonLineList().get(indexOfRightLine), gameObject.getPolygonLineList().get(myMod(indexOfRightLine + 1, linesInPolygon)));
        rightListOfPathPointsAroundObject.add(currentPoint);
        if (currentPoint.isLastPointInObject()) {
            pathFoundToTheRight = true;
        }
    }

    private void detectVisibilityFromLeftLine(int indexOfCrossedLineInObjectLineList, int iteration, int linesInPolygon) {
        Point currentPoint;
        int indexOfLeftLine = myMod(indexOfCrossedLineInObjectLineList + iteration, linesInPolygon);
        currentPoint = detectVisibilityOfFinalPointFromLine(gameObject.getPolygonLineList().get(indexOfLeftLine), gameObject.getPolygonLineList().get(myMod(indexOfLeftLine - 1, linesInPolygon)));
        leftListOfPathPointsAroundObject.add(currentPoint);
        if (currentPoint.isLastPointInObject()) {
            pathFoundToTheLeft = true;
        }
    }

    private int findCornersOfIntersectedLineOfPolygon(GameObject gameObject, Shape shape) {

        for (int i = 0; i < gameObject.getPolygonLineList().size(); i++) {
            Line currentLine = gameObject.getPolygonLineList().get(i);
            Shape intersection = Shape.intersect(currentLine, shape);
            if (!(intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0)) {
                graphicsContext.fillOval(currentLine.getEndX() - 5, currentLine.getEndY() - 5, 10, 10);
                graphicsContext.fillOval(currentLine.getStartX() - 5, currentLine.getStartY() - 5, 10, 10);
                return i;
            }
        }
        return 0;
    }

    private Point detectVisibilityOfFinalPointFromLine(Line currentLine, Line lastLine) {
        Point currentPoint = new Point(0, 0);

        if ((lastLine.getStartX() == currentLine.getStartX() && lastLine.getStartY() == currentLine.getStartY()) || (lastLine.getEndX() == currentLine.getStartX() && lastLine.getEndY() == currentLine.getStartY())) {
            currentPoint.setCoordX(currentLine.getEndX());
            currentPoint.setCoordY(currentLine.getEndY());
        } else {
            currentPoint.setCoordX(currentLine.getStartX());
            currentPoint.setCoordY(currentLine.getStartY());
        }

        graphicsContext.fillOval(currentPoint.getCoordX() - 5, currentPoint.getCoordY() - 5, 10, 10);

        if (detectVisibilityOfFinalPointFromPoint(currentPoint)) {
            currentPoint.setLastPointInObject(true);
            return currentPoint;
        }

        return currentPoint;
    }

    private boolean detectVisibilityOfFinalPointFromPoint(Point point) {

        if (point.getCoordX() < targetX) {
            currentX = point.getCoordX() + 1;
        } else {
            currentX = point.getCoordX() - 1;
        }

        if (point.getCoordY() < targetY) {
            currentY = point.getCoordY() + 1;
        } else {
            currentY = point.getCoordY() - 1;
        }

        graphicsContext.fillOval(point.getCoordX() - 5, point.getCoordY() - 5, 10, 10);

        Line testLine = new Line(currentX, currentY, targetX, targetY);
        Shape intersection = Shape.intersect(testLine, gameObject.getGameObjectPolygon());
        if (intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0) {
            return true;
        }

        return false;
    }

    private int myMod(int x, int modulo) {
        return ((x % modulo) + modulo) % modulo;
    }

}
