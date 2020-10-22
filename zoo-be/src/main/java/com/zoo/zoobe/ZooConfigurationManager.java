package com.zoo.zoobe;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.xml.sax.SAXException;
import org.apache.zookeeper.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class ZooConfigurationManager implements Watcher {

    ZooKeeper zooKeeper;
    private boolean encodeAsString;

    public static byte[] toByteArray(Object obj) throws IOException {
        if (obj == null) {
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        byte[] b = bos.toByteArray();
        oos.close();
        bos.close();
        return b;
    }

    public static byte[] stringToByteArray(String value) throws IOException {
        if (value == null) {
            return new byte[0];
        }

        return value.getBytes();
    }

    private static Object toObject(byte[] b) throws IOException, ClassNotFoundException {
        //   System.out.println("to object: " + b);
        if (b == null || b.length == 0) {
            return null;
        }

        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (StreamCorruptedException ex) {
            // l'array di byte non corrisponde ad un oggetto Java serializzato
            obj = "<dati binari>";
        }
        return obj;
    }

    public ZooConfigurationManager(String host) {
        this(false, host);
    }

    public ZooConfigurationManager(boolean encodeAsString, String host) {
        this.encodeAsString = encodeAsString;

        try {
            zooKeeper = new ZooKeeper(host, 10000, this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createPath(String path) throws InterruptedException, KeeperException {
        int pos = 0;

        while ((pos = path.indexOf('/', pos + 1)) != -1) {
            String p = path.substring(0, pos);
            try {
                zooKeeper.create(p, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException.NodeExistsException ex) {
                // ignore
            }

        }
        try {
            zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException ex) {
            // ignore
        }
    }

    public final void load(InputStream in) throws IOException, InterruptedException, KeeperException, ParserConfigurationException, SAXException, ClassNotFoundException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        SAXParser parser = factory.newSAXParser();
        parser.parse(in, new XMLHandler(this));
    }


    public void setEncodedAsString(boolean encodeAsString) {
        this.encodeAsString = encodeAsString;
    }

    public void set(String key, Object value) throws IOException,
            KeeperException,
            InterruptedException {
        //     key = CONF_PATH + key;
        createPath(key);
        // zooKeeper.create(key, toByteArray(value), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        if (encodeAsString) {
            zooKeeper.setData(key, stringToByteArray(value.toString()), -1);
        } else {
            zooKeeper.setData(key, toByteArray(value), -1);
        }
    }

    public void delete(String path) throws InterruptedException, KeeperException, IOException, ClassNotFoundException {
        try {

            List<String> children = zooKeeper.getChildren(path, false);

            for (String c : children) {
                delete(path + "/" + c);
            }
            zooKeeper.delete(path, -1);

        } catch (KeeperException.NoNodeException ex) {
            
        }
    }

    public Object get(String key) throws KeeperException,
            InterruptedException,
            IOException,
            ClassNotFoundException {
        Stat stat = new Stat();
        byte[] b = zooKeeper.getData(key, false, stat);
        if (encodeAsString) {
            return new String(b);
        } else {
            return toObject(b);
        }
    }


    public int getChildCount(String path, String element) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, false);
        int count = 0;
        for (String el : children) {
            if (element == null || el.equals(element)) {
                count++;
            }
        }
        return count;
    }

    public List<String> getChildrenList(String path) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, false);
        return children;
    }


    @Override
    public void process(WatchedEvent we) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
