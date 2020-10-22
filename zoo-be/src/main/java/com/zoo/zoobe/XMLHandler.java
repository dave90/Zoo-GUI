/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zoo.zoobe;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

    ZooConfigurationManager conf = null;
    Stack<String> nodeStack = new Stack();
    boolean idPresente = false;
    String currentType = null;

    public XMLHandler(ZooConfigurationManager conf) {
        this.conf = conf;
    }

    private String getPath() {
        String path = "";
        for (String s : nodeStack) {
            path = path + "/" + s;
        }
        return path;
    }

    public void startElement(String uri,
            String localName,
            String qName,
            Attributes attributes)
            throws SAXException {
        nodeStack.push(qName);

        String path = getPath();

        String id = attributes.getValue("id");
        if (id != null) {
            nodeStack.push(id);
            idPresente = true;
        }

        String type = attributes.getValue("type");
        if (type != null) {
            currentType = type;
        }

        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals("id") || attributes.getLocalName(i).equals("type")) {
                continue;
            }

            try {
                conf.set(path + "/" + attributes.getLocalName(i), attributes.getValue(i));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void endElement(String uri,
            String localName,
            String qName)
            throws SAXException {
        String top = nodeStack.pop();
        while (!top.equals(qName)) {
            top = nodeStack.pop();
        }

        currentType = null;
    }

    public void characters(char[] ch,
            int start,
            int length)
            throws SAXException {
        String val = new String(ch);
        try {
            conf.set(getPath(), TypeMapper.getValue(currentType, val.substring(start, start + length)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
