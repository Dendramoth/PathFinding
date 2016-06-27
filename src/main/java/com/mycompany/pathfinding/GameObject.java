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
public class GameObject {

    private double possitionX = 0;
    private double possitionY = 0;
    private GraphicsContext graphicsContext;
    private Polygon gameObjectPolygon = new Polygon();
    private Color color;
    private double[] xPoints;
    private double[] yPoints;
    private int numberOfPoints;

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

}
