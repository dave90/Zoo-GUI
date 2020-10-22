/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zoo.zoobe.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
public class ZooController {

    @Autowired
    ZooService service;
    
    
    
    @GetMapping("/greeting")
    public Node greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Node("Nodooo", "VALORE");
    }
    
    @GetMapping("/get_node")
    public Node getNode(@RequestParam(value = "zoo") String zoo,@RequestParam(value = "path") String path,@RequestParam(value = "encode",defaultValue = "1" ) String encode ) {
        boolean encodeAsString = (encode.equals("1"))?true:false;
        return service.getNode(zoo, path,encodeAsString);
    }

    
    @GetMapping("/set_node")
    public String setNode(@RequestParam(value = "zoo") String zoo,@RequestParam(value = "path") String path,@RequestParam(value = "value" ) String value,@RequestParam(value = "type", defaultValue = "None" ) String type ) {
        Node node = new Node(path);
        node.setValue(value);
        node.setType(type.equals("None")?null:type);
        service.setNode(zoo, node);
        return "{\"result\":\"OK\"}";
    }
    
    @PostMapping("/load_xml")
    public String loadXMl(@RequestBody String xml, @RequestParam(value = "zoo") String zoo,@RequestParam(value = "encode",defaultValue = "1" ) String encode ) {
        boolean encodeAsString = (encode.equals("1"))?true:false;
        service.loadXML(zoo,encodeAsString,xml);
        return "{\"result\":\"OK\"}";
    }
    
    @DeleteMapping("/delete_node")
    public String deleteNode(@RequestParam(value = "zoo") String zoo,@RequestParam(value = "path") String path ) {
        service.deleteNode(zoo, path);
        return "{\"result\":\"OK\"}";
    }   
    
    
}
