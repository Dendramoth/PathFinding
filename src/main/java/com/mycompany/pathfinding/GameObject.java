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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 *
 * @author Dendra
 */
public class GameObject implements Comparable<GameObject> {

    private double possitionX = 0;
    private double possitionY = 0;
    private GraphicsContext graphicsContext;
    private Polygon gameObjectPolygon = new Polygon();
    private Color color;
    private double[] xPoints;
    private double[] yPoints;
    private int numberOfPoints;

    private double objectForComparisonPosX;
    private double objectForComparisonPosY;

    private List<Line> polygonLineList = new ArrayList<Line>();

    public GameObject(List<Point> pointsList, Point centerPoint, GraphicsContext graphicsContext, Color color) {
        this.possitionX = centerPoint.getCoordX();
        this.possitionY = centerPoint.getCoordY();

        this.graphicsContext = graphicsContext;
        this.color = color;
        this.numberOfPoints = pointsList.size();

        xPoints = new double[numberOfPoints];
        yPoints = new double[numberOfPoints];

        createPolygon(pointsList);
        createLinesFromPolygonPoints(pointsList);
    }

    private void createPolygon(List<Point> pointsList) {
        int i = 0;
        for (Point point : pointsList) {
            gameObjectPolygon.getPoints().add(point.getCoordX());
            gameObjectPolygon.getPoints().add(point.getCoordY());

            xPoints[i] = point.getCoordX();
            yPoints[i] = point.getCoordY();
            i++;
        }
    }

    private void createLinesFromPolygonPoints(List<Point> pointsList) {
        Line line;
        for (int i = 0; i < pointsList.size(); i++) {
            if (i < pointsList.size() - 1) {
                line = new Line(pointsList.get(i).getCoordX(), pointsList.get(i).getCoordY(), pointsList.get(i + 1).getCoordX(), pointsList.get(i + 1).getCoordY());
            } else {
                line = new Line(pointsList.get(i).getCoordX(), pointsList.get(i).getCoordY(), pointsList.get(0).getCoordX(), pointsList.get(0).getCoordY());
            }
            polygonLineList.add(line);
        }
    }

    public void paintGameObject() {
        graphicsContext.setStroke(color);
        graphicsContext.strokePolygon(xPoints, yPoints, numberOfPoints);
    }

    public void moveGameObject(Point moveToPoint) {
        double deltaX = moveToPoint.getCoordX() - possitionX;
        double deltaY = moveToPoint.getCoordY() - possitionY;
        double angle = calculateAngleBetweenPoints(deltaX, deltaY);

        possitionX = possitionX - Math.cos(Math.toRadians(angle + 90)) * 1;
        possitionY = possitionY - Math.sin(Math.toRadians(angle + 90)) * 1;

        for (int i = 0; i < numberOfPoints; i++) {
            xPoints[i] = xPoints[i] - Math.cos(Math.toRadians(angle + 90)) * 1;
            yPoints[i] = yPoints[i] - Math.sin(Math.toRadians(angle + 90)) * 1;
        }
    }

    private double calculateAngleBetweenPoints(double x, double y) {
        double angle;
        if (y == 0 && x == 0) {
            angle = 0;
        } else if (y > 0) {
            angle = Math.toDegrees(Math.acos(x / (Math.sqrt(y * y + x * x)))) + 90;
        } else {
            angle = -Math.toDegrees(Math.acos(x / (Math.sqrt(y * y + x * x)))) + 90;
        }
        angle = (angle + 360) % 360;
        return angle;
    }

    public Shape detectIntersection(Shape lineDetection) {
        return Shape.intersect(gameObjectPolygon, lineDetection);
    }

    public double getPossitionX() {
        return possitionX;
    }

    public double getPossitionY() {
        return possitionY;
    }

    public Polygon getGameObjectPolygon() {
        return gameObjectPolygon;
    }

    public List<Line> getPolygonLineList() {
        return polygonLineList;
    }

    public double getObjectForComparisonPosX() {
        return objectForComparisonPosX;
    }

    public void setObjectForComparisonPosX(double objectForComparisonPosX) {
        this.objectForComparisonPosX = objectForComparisonPosX;
    }

    public double getObjectForComparisonPosY() {
        return objectForComparisonPosY;
    }

    public void setObjectForComparisonPosY(double objectForComparisonPosY) {
        this.objectForComparisonPosY = objectForComparisonPosY;
    }

    @Override
    public int compareTo(GameObject o) {
        double myDistance = Math.sqrt((this.getPossitionX() - this.objectForComparisonPosX) * (this.getPossitionX() - this.objectForComparisonPosX) + (this.getPossitionY() - this.objectForComparisonPosY) * (this.getPossitionY() - this.objectForComparisonPosY));
        double otherDistance = Math.sqrt((o.getPossitionX() - o.getObjectForComparisonPosX()) * (o.getPossitionX() - o.getObjectForComparisonPosX()) + (o.getPossitionY() - o.getObjectForComparisonPosY()) * (o.getPossitionY() - o.getObjectForComparisonPosY()));
        if (myDistance < otherDistance) {
            return -1;
        } else {
            return 1;
        }
    }

}
