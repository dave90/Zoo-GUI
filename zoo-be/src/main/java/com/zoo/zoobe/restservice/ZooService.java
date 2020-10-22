/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zoo.zoobe.restservice;

import com.zoo.zoobe.ZooConfigurationManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class ZooService {

    private Map<String, ZooConfigurationManager> confMap;

    public ZooService() {
        confMap = new HashMap<>();
    }

    private ZooConfigurationManager getConf(String conf) {
        if (confMap.containsKey(conf)) {
            return confMap.get(conf);
        }

        ZooConfigurationManager configuration = new ZooConfigurationManager(conf);
        confMap.put(conf, configuration);
        return configuration;
    }

    public void setNode(String zoo, Node node) {
        Logger.getLogger(ZooService.class.getName()).log(Level.INFO, "Set node " + zoo + " " + node.getValue() + " " + node.getType());
        try {
            ZooConfigurationManager configuration = getConf(zoo);
            String path = node.getName();

            if (node.getType() == null) {
                configuration.setEncodedAsString(true);
                Logger.getLogger(ZooService.class.getName()).log(Level.INFO, node.getValue().toString());

                configuration.set(path, node.getValue());
                return;
            }

            Class<?> clazz = Class.forName(node.getType());
            Constructor<?> ctor = clazz.getConstructor(String.class);
            Object object = ctor.newInstance(new Object[]{node.getValue()});

            Logger.getLogger(ZooService.class.getName()).log(Level.INFO, object.toString());
            configuration.setEncodedAsString(false);
            configuration.set(path, object);

        } catch (Exception ex) {
            Logger.getLogger(ZooService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Node getNode(String zoo, String path, boolean encodeAsString) {
        Logger.getLogger(ZooService.class.getName()).log(Level.INFO, "Get node " + zoo + " " + path);

        Node node = new Node(path);
        try {
            ZooConfigurationManager configuration = getConf(zoo);

            int count = configuration.getChildCount(path, null);
            if (count == 0) {
                getValue(zoo, node, encodeAsString);
                return node;
            }

            List<String> childs = configuration.getChildrenList(path);
            for (String c : childs) {
                node.addChild(c);
            }

        } catch (Exception ex) {
            Logger.getLogger(ZooService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return node;
    }

    private void getValue(String zoo, Node node, boolean encodeAsString) {
        try {
            ZooConfigurationManager configuration = getConf(zoo);
            configuration.setEncodedAsString(encodeAsString);

            Object value = configuration.get(node.getName());

            if (!encodeAsString) {
                node.setType(value.getClass().getName());
            }

            node.setValue(value);
        } catch (Exception ex) {
            Logger.getLogger(ZooService.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void loadXML(String zoo, boolean encodeAsString, String xml) {
        Logger.getLogger(ZooService.class.getName()).log(Level.INFO, "Load xml " + zoo + " " + encodeAsString);
        System.out.println(xml);
        String newfileName = null;
        try {
            newfileName = UUID.randomUUID().toString() + "-tmp.xml";
            FileWriter myWriter = new FileWriter(newfileName);
            myWriter.write(xml);
            myWriter.close();

            ZooConfigurationManager configuration = getConf(zoo);
            configuration.setEncodedAsString(encodeAsString);

            FileInputStream in = new FileInputStream(newfileName);
            configuration.load(in);
            in.close();

        } catch (Exception ex) {

            Logger.getLogger(ZooService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (newfileName != null) {
            File newFile = new File(newfileName);
            newFile.delete();
        }

    }
    
    
    
    public void deleteNode(String zoo, String path) {
        Logger.getLogger(ZooService.class.getName()).log(Level.INFO, "Delete node " + zoo + " " + path);

        try {
            ZooConfigurationManager configuration = getConf(zoo);

            configuration.delete(path);


        } catch (Exception ex) {
            Logger.getLogger(ZooService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
