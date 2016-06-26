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
    private double width = 0;
    private double height = 0;
    private GraphicsContext graphicsContext;
    private Polygon gameObjectPolygon = new Polygon();
    private Color color;
    
    private double[] polygonPoints;
    
    private List<Line> polygonLineList = new ArrayList<Line>();

    public GameObject(double possitionX, double possitionY, double width, double heigh, GraphicsContext graphicsContext, Color color) {
        this.possitionX = possitionX;
        this.possitionY = possitionY;
        this.width = width;
        this.height = heigh;
        this.graphicsContext = graphicsContext;
        this.color = color;
        
        Line line0 = new Line(possitionX, possitionY, possitionX + this.width, possitionY);
        Line line1 = new Line(possitionX + this.width, possitionY, possitionX + this.width, possitionY + height);
        Line line2 = new Line(possitionX + this.width, possitionY + height, possitionX, possitionY + height);
        Line line3 = new Line(possitionX, possitionY + height, possitionX, possitionY);
        
        polygonLineList.add(line0);
        polygonLineList.add(line1);
        polygonLineList.add(line2);
        polygonLineList.add(line3);
        
        gameObjectPolygon.getPoints().addAll(new Double[]{
            possitionX, possitionY,
            possitionX + this.width, possitionY,
            possitionX + this.width, possitionY + height,
            possitionX, possitionY + height,
            possitionX, possitionY});
    }

    public void paintGameObject() {
        graphicsContext.setStroke(color);
        graphicsContext.strokeRect(possitionX, possitionY, width, height);
    }
    
    public Shape detectIntersection(Shape lineDetection){
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
