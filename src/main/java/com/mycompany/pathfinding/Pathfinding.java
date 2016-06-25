/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dendra
 */
public class Pathfinding {
    private List<GameObject> gameStaticObjectsList = new ArrayList<GameObject>();
    private GameObject startGameObject;
    private GameObject endGameObject;

    public Pathfinding(List<GameObject> gameStaticObjectsList, GameObject startGameObject, GameObject endGameObject) {
        this.gameStaticObjectsList = gameStaticObjectsList;
        this.startGameObject = startGameObject;
        this.endGameObject = endGameObject;
    }
    
    public void createPath(){
        
    }
    
    
    
}
