/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zoo.zoobe.restservice;

import java.util.ArrayList;
import java.util.List;

public class Node {
    
    private String name;
    private Object value;
    private List<String> child;
    private String type;

    public Node(String name, String value) {
        this.name = name;
        this.value = value;
        this.child = new ArrayList<>();
    }

    public Node(String name) {
        this.name = name;
        this.child = new ArrayList<>();
    }    

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public List<String> getChild() {
        return child;
    }
    
    public void addChild(String s){
        child.add(s);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
    
    
    
    
    
}
