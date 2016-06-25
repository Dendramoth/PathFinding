/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
    
    private double[][] pointsToReachEndPolygon;

    public GameObject(double possitionX, double possitionY, double width, double heigh, GraphicsContext graphicsContext, Color color) {
        this.possitionX = possitionX;
        this.possitionY = possitionY;
        this.width = width;
        this.height = heigh;
        this.graphicsContext = graphicsContext;
        this.color = color;
        
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
    
    

}
