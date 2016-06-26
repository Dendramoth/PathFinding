/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

/**
 *
 * @author Dendra
 */
public class PathPoint {
    private PathPoint precessor;
    private PathPoint succesorLeft;
    private PathPoint succesorRight;
    private double possitionX;
    private double possitionY;

    public PathPoint() {
    }

    public PathPoint(PathPoint precessor, PathPoint succesorRight,PathPoint succesorLeft, double possitionX, double possitionY) {
        this.precessor = precessor;
        this.succesorLeft = succesorLeft;
        this.succesorRight = succesorRight;
        this.possitionX = possitionX;
        this.possitionY = possitionY;
    }

    public PathPoint getPrecessor() {
        return precessor;
    }

    public double getPossitionX() {
        return possitionX;
    }

    public double getPossitionY() {
        return possitionY;
    }

    public PathPoint getSuccesorLeft() {
        return succesorLeft;
    }

    public PathPoint getSuccesorRight() {
        return succesorRight;
    }

    public void setPrecessor(PathPoint precessor) {
        this.precessor = precessor;
    }

    public void setSuccesorLeft(PathPoint succesorLeft) {
        this.succesorLeft = succesorLeft;
    }

    public void setSuccesorRight(PathPoint succesorRight) {
        this.succesorRight = succesorRight;
    }

    public void setPossitionX(double possitionX) {
        this.possitionX = possitionX;
    }

    public void setPossitionY(double possitionY) {
        this.possitionY = possitionY;
    }
    
    
    
}
