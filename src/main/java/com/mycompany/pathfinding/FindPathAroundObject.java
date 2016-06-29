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
    private double goalX;
    private double goalY;
    private GameObject gameObject;
    private GraphicsContext graphicsContext;
    private List<Point> leftListOfPathPointsAroundObject = new ArrayList<Point>();
    private List<Point> rightListOfPathPointsAroundObject = new ArrayList<Point>();
    private List<Point> listOfPathPoints = new ArrayList<Point>();

    public FindPathAroundObject(double currentX, double currentY, double goalX, double goalY, GameObject gameObject, GraphicsContext graphicsContext, List<Point> listOfPathPoints) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.gameObject = gameObject;
        this.graphicsContext = graphicsContext;
        this.listOfPathPoints = listOfPathPoints;
        
        this.listOfPathPoints.add(new Point(currentX, currentY)); // add intersection point with object
    }

    public Point findPathAroundObject() {
        int linesInPolygon = gameObject.getPolygonLineList().size();
        int indexOfCrossedLineInObjectLineList = findCornersOfIntersectedLineOfPolygon(gameObject, new Rectangle(currentX - 1, currentY - 1, 3, 3));
        Line line = gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList);

        rightListOfPathPointsAroundObject.add(new Point(line.getStartX(), line.getStartY()));
        Point pointToTest = new Point(line.getStartX(), line.getStartY());
        if (detectVisibilityOfFinalPointFromPoint(pointToTest)) {
            return pointToTest;
        }

        leftListOfPathPointsAroundObject.add(new Point(line.getEndX(), line.getEndY()));
        pointToTest = new Point(line.getEndX(), line.getEndY());
        if (detectVisibilityOfFinalPointFromPoint(pointToTest)) {
            return pointToTest;
        }

        for (int i = 1; i < gameObject.getPolygonLineList().size() + 1; i++) {

            int indexOfLeftLine = myMod(indexOfCrossedLineInObjectLineList + i, linesInPolygon);
            int indexOfRightLine = myMod(indexOfCrossedLineInObjectLineList - i, linesInPolygon);

            Point currentPoint = detectVisibilityOfFinalPointFromNextLine(gameObject.getPolygonLineList().get(indexOfLeftLine), gameObject.getPolygonLineList().get(myMod(indexOfLeftLine - 1, linesInPolygon)));
            leftListOfPathPointsAroundObject.add(currentPoint);
            if (currentPoint.isLastPointInObject()){
                listOfPathPoints.addAll(leftListOfPathPointsAroundObject);
                return currentPoint;
            }
            currentPoint = detectVisibilityOfFinalPointFromNextLine(gameObject.getPolygonLineList().get(indexOfRightLine), gameObject.getPolygonLineList().get(myMod(indexOfRightLine + 1, linesInPolygon)));
            rightListOfPathPointsAroundObject.add(currentPoint);
            if (currentPoint.isLastPointInObject()){  
                listOfPathPoints.addAll(rightListOfPathPointsAroundObject);
                return currentPoint;
            }
        }

        return null;
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

    private boolean detectVisibilityOfFinalPointFromPoint(Point point) {
        double currentPointX = point.getCoordX();
        double currentPointY = point.getCoordY();

        if (currentPointX < goalX) {
            currentX = currentPointX + 1;
        } else {
            currentX = currentPointX - 1;
        }

        if (currentPointY < goalY) {
            currentY = currentPointY + 1;
        } else {
            currentY = currentPointY - 1;
        }

        graphicsContext.fillOval(currentPointX - 5, currentPointY - 5, 10, 10);

        Line testLine = new Line(currentX, currentY, goalX, goalY);
        Shape intersection = Shape.intersect(testLine, gameObject.getGameObjectPolygon());
        if (intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0) {

            //pointFromWhichTheTargetIsVissible.setCoordX(currentX);
            //pointFromWhichTheTargetIsVissible.setCoordY(currentY);
            //   graphicsContext.strokeLine(currentX, currentY, goalX, goalY);
            return true;
        }

        return false;
    }

    private Point detectVisibilityOfFinalPointFromNextLine(Line currentLine, Line lastLine) {
        double currentLinePointX;
        double currentLinePointY;

        if ((lastLine.getStartX() == currentLine.getStartX() && lastLine.getStartY() == currentLine.getStartY()) || (lastLine.getEndX() == currentLine.getStartX() && lastLine.getEndY() == currentLine.getStartY())) {
            currentLinePointX = currentLine.getEndX();
            currentLinePointY = currentLine.getEndY();
        } else {
            currentLinePointX = currentLine.getStartX();
            currentLinePointY = currentLine.getStartY();
        }

        graphicsContext.fillOval(currentLinePointX - 5, currentLinePointY - 5, 10, 10);

        if (currentLinePointX < goalX) {
            currentX = currentLinePointX + 1;
        } else {
            currentX = currentLinePointX - 1;
        }

        if (currentLinePointY < goalY) {
            currentY = currentLinePointY + 1;
        } else {
            currentY = currentLinePointY - 1;
        }

        Point actualPoint = new Point(currentX, currentY);
        
        Line testLine = new Line(currentX, currentY, goalX, goalY);
        Shape intersection = Shape.intersect(testLine, gameObject.getGameObjectPolygon());
        if (intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0) {
            actualPoint.setLastPointInObject(true);
            return actualPoint;
        }
        return actualPoint;
    }

    private int myMod(int x, int modulo) {
        return ((x % modulo) + modulo) % modulo;
    }

}
