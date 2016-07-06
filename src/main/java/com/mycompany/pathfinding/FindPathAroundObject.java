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

    private double targetX;
    private double targetY;
    private double leftCummulatedDistance = 0;
    private double rightCummulatedDistance = 0;

    private boolean pathFoundToTheRight;
    private boolean pathFoundToTheLeft;

    private GameObject gameObject;
    private GraphicsContext graphicsContext;
    private List<Point> leftListOfPathPointsAroundObject = new ArrayList<Point>();
    private List<Point> rightListOfPathPointsAroundObject = new ArrayList<Point>();
    private List<Point> listOfPathPoints = new ArrayList<Point>();

    public FindPathAroundObject(double targetX, double targetY, GameObject gameObject, GraphicsContext graphicsContext, List<Point> listOfPathPoints) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.gameObject = gameObject;
        this.graphicsContext = graphicsContext;
        this.listOfPathPoints = listOfPathPoints;
    }

    public Point findPathAroundObject(Point pointOfCollision) {
        this.leftListOfPathPointsAroundObject.add(new Point(pointOfCollision.getCoordX(), pointOfCollision.getCoordY()));
        this.rightListOfPathPointsAroundObject.add(new Point(pointOfCollision.getCoordX(), pointOfCollision.getCoordY()));
        
        int leftIteration = 0;
        int rightIteration = 0;

        int linesInPolygon = gameObject.getPolygonLineList().size();
        int indexOfCrossedLineInObjectLineList = findCornersOfIntersectedLineOfPolygon(gameObject, new Rectangle(pointOfCollision.getCoordX() - 1, pointOfCollision.getCoordY() - 1, 3, 3));

        detectVisibilityOfFinalPointFromCornersOfFirstLineWithCollision(indexOfCrossedLineInObjectLineList, pointOfCollision);

        for (int i = 1; i < gameObject.getPolygonLineList().size() * 2; i++) {
            if (leftCummulatedDistance < rightCummulatedDistance) {
                leftIteration++;
                Point currentPoint = detectVisibilityFromLeftLine(indexOfCrossedLineInObjectLineList, leftIteration, linesInPolygon);
                leftCummulatedDistance = leftCummulatedDistance + distanceBetweenPoints(currentPoint, leftListOfPathPointsAroundObject.get(leftListOfPathPointsAroundObject.size() - 1));
                leftListOfPathPointsAroundObject.add(currentPoint);
                if (pathFoundToTheLeft) {
                    listOfPathPoints.addAll(leftListOfPathPointsAroundObject);
                    return leftListOfPathPointsAroundObject.get(leftListOfPathPointsAroundObject.size() - 1);
                }
            } else {
                rightIteration++;
                Point currentPoint = detectVisibilityFromRightLine(indexOfCrossedLineInObjectLineList, rightIteration, linesInPolygon);
                rightCummulatedDistance = rightCummulatedDistance + distanceBetweenPoints(currentPoint, rightListOfPathPointsAroundObject.get(rightListOfPathPointsAroundObject.size() - 1));
                rightListOfPathPointsAroundObject.add(currentPoint);
                if (pathFoundToTheRight) {
                    listOfPathPoints.addAll(rightListOfPathPointsAroundObject);
                    return rightListOfPathPointsAroundObject.get(rightListOfPathPointsAroundObject.size() - 1);
                }
            }
        }
        return null;
    }

    private void detectVisibilityOfFinalPointFromCornersOfFirstLineWithCollision(int indexOfCrossedLineInObjectLineList, Point pointOfCollision) {
        Line line = gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList); //get line of object crossed by path to final destination

        Point crossingPoint = new Point(pointOfCollision.getCoordX(), pointOfCollision.getCoordY());
        Point cornerPoint = new Point(line.getStartX(), line.getStartY());
        rightListOfPathPointsAroundObject.add(cornerPoint);
        rightCummulatedDistance = distanceBetweenPoints(crossingPoint, cornerPoint);
        if (detectVisibilityOfFinalPointFromPoint(cornerPoint)) {
            pathFoundToTheRight = true;
        }

        cornerPoint = new Point(line.getEndX(), line.getEndY());
        leftListOfPathPointsAroundObject.add(cornerPoint);
        leftCummulatedDistance = distanceBetweenPoints(crossingPoint, cornerPoint);
        if (detectVisibilityOfFinalPointFromPoint(cornerPoint)) {
            pathFoundToTheLeft = true;
        }
    }

    private Point detectVisibilityFromRightLine(int indexOfCrossedLineInObjectLineList, int iteration, int linesInPolygon) {
        Point currentPoint;
        int indexOfRightLine = myMod(indexOfCrossedLineInObjectLineList - iteration, linesInPolygon);
        currentPoint = detectVisibilityOfFinalPointFromLine(gameObject.getPolygonLineList().get(indexOfRightLine), gameObject.getPolygonLineList().get(myMod(indexOfRightLine + 1, linesInPolygon)));

        if (currentPoint.isLastPointInObject()) {
            pathFoundToTheRight = true;
        }
        return currentPoint;
    }

    private Point detectVisibilityFromLeftLine(int indexOfCrossedLineInObjectLineList, int iteration, int linesInPolygon) {
        Point currentPoint;
        int indexOfLeftLine = myMod(indexOfCrossedLineInObjectLineList + iteration, linesInPolygon);
        currentPoint = detectVisibilityOfFinalPointFromLine(gameObject.getPolygonLineList().get(indexOfLeftLine), gameObject.getPolygonLineList().get(myMod(indexOfLeftLine - 1, linesInPolygon)));

        if (currentPoint.isLastPointInObject()) {
            pathFoundToTheLeft = true;
        }
        return currentPoint;
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
        double coordXForLineTest = 0;
        double coordYForLineTest = 0;
        
        if (point.getCoordX() < targetX) {
            coordXForLineTest = point.getCoordX() + 1;
        } else {
            coordXForLineTest = point.getCoordX() - 1;
        }

        if (point.getCoordY() < targetY) {
            coordYForLineTest = point.getCoordY() + 1;
        } else {
            coordYForLineTest = point.getCoordY() - 1;
        }

        graphicsContext.fillOval(point.getCoordX() - 5, point.getCoordY() - 5, 10, 10);

        Line testLine = new Line(coordXForLineTest, coordYForLineTest, targetX, targetY);
        Shape intersection = Shape.intersect(testLine, gameObject.getGameObjectPolygon());
        if (intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0) {
            return true;
        }

        return false;
    }

    private int myMod(int x, int modulo) {
        return ((x % modulo) + modulo) % modulo;
    }

    private double distanceBetweenPoints(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p2.getCoordX() - p1.getCoordX()), 2) + Math.pow((p2.getCoordY() - p1.getCoordY()), 2));
    }

}
