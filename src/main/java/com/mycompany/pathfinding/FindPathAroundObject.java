/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

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
    private Point pointFromWhichTheTargetIsVissible = new Point(0, 0);

    public FindPathAroundObject(double currentX, double currentY, double goalX, double goalY, GameObject gameObject, GraphicsContext graphicsContext) {
        this.currentX = currentX;
        this.currentY = currentY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.gameObject = gameObject;
        this.graphicsContext = graphicsContext;

    }

    public Point findPathAroundObject() {
        int indexOfCrossedLineInObjectLineList = findCornersOfIntersectedLineOfPolygon(gameObject, new Rectangle(currentX - 1, currentY - 1, 3, 3));
        if (detectVisibilityOfFinalPointFromNextLine(gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList), gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList))){
            return pointFromWhichTheTargetIsVissible;
        }

        int indexOfFirstLeftLine = (indexOfCrossedLineInObjectLineList + 1) % gameObject.getPolygonLineList().size();
        int indexOfFirstRightLine = (indexOfCrossedLineInObjectLineList - 1) % gameObject.getPolygonLineList().size();
        
        if (detectVisibilityOfFinalPointFromNextLine(gameObject.getPolygonLineList().get(indexOfFirstLeftLine), gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList))){
            return pointFromWhichTheTargetIsVissible;
        }else{
            detectVisibilityOfFinalPointFromNextLine(gameObject.getPolygonLineList().get(indexOfFirstRightLine), gameObject.getPolygonLineList().get(indexOfCrossedLineInObjectLineList));
            return pointFromWhichTheTargetIsVissible;
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
    
    private void detectVisibilityOfFinalPointFromPoint(){
        
    }

    private boolean detectVisibilityOfFinalPointFromNextLine(Line currentLine, Line lastLine) {
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

        Line testLine = new Line(currentX, currentY, goalX, goalY);
        Shape intersection = Shape.intersect(testLine, gameObject.getGameObjectPolygon());
        if (intersection.getLayoutBounds().getHeight() <= 0 || intersection.getLayoutBounds().getWidth() <= 0) {
            
            pointFromWhichTheTargetIsVissible.setCoordX(currentX);
            pointFromWhichTheTargetIsVissible.setCoordY(currentY);
         //   graphicsContext.strokeLine(currentX, currentY, goalX, goalY);
            return true;
        }

        return false;
    }

}
