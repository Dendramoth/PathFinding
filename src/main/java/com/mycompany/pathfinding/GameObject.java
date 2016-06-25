/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

    public GameObject(double possitionX, double possitionY, double width, double heigh, GraphicsContext graphicsContext) {
        this.possitionX = possitionX;
        this.possitionY = possitionY;
        this.width = width;
        this.height = heigh;
        this.graphicsContext = graphicsContext;
    }

    public void paintGameObject() {
        graphicsContext.setFill(Color.AQUA);
        graphicsContext.strokeRect(possitionX, possitionY, width, height);
    }

}
