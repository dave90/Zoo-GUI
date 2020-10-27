# Zoo-GUI

Web application for viewing and writing configuration in zookeeper. The application is splitted in angular front-end and spring back-end.

![Screenshot](https://github.com/dave90/Zoo-GUI/blob/main/img/screen1.png)

## Prerequisites

- **Java**
- **Angular**
- **Node JS**

## Build
You can find the builded application in the releases section but If you want to build the web application run the following instructions:

- Under *zoo-be* folder run the command: **mvn install** and in the directory *target* you can find the jar back-end;
- Under *zoo-gui* folder run the command: **ng build --prod** and in the directory *dist* you can find the builded front-end;

## Run
Download the releases or build the the back-end and front-end. After running first the back-end and after the front-end.

### Back-End
Under the directory *zoo-be/target* run the jar *zoo-be.jar*

- **java -jar zoo-be.jar**

### Front-End
Under the directory *zoo-gui/dist/zoo-gui* run the command:

- **npm start**

## Usages

- [Loading XML Configuration](#loading-xml-configuration)
- [Reading and Writing Nodes](#reading-and-writing-nodes)
- [Request Node as Java Objects](#request-node-as-java-objects)
- [Deleting Node](#deleting-node)
- [Options](#options)


### Loading XML Configuration

You can load zookeeper configuration using xml file selecting in the menu: *Load XML*.

![Screenshot](https://github.com/dave90/Zoo-GUI/blob/main/img/screen2.png)

Wait untile the file is loaded and after complete close the dialog.

Example of xml file:
```xml
<Tests>
  
  <test id="1">
    <BootstrapServer type="CHAR">10.39.92.71:9092</BootstrapServer>
    <GatewayIP type="CHAR">10.39.92.71:9092</GatewayIP>
  </test>
  
</Tests>  
```    


### Reading and Writing Nodes

In the *Zoo Nodes* card you can find the tree stored in zookeeper. Expand the tree node load the child nodes and and shows them. If the node is a leaf then you can click the link node and display and edit the related information in *Node Info* card. To save changes remember to click the **Save** button.  

![Screenshot](https://github.com/dave90/Zoo-GUI/blob/main/img/screen3.png)

### Request Node as Java Objects

It's possible to save the value of a node as Java object. The current supported type are:

- *java.lang.Integer*
- *java.lang.Float*
- *java.lang.String*
- *java.lang.Double*

If the type is None then the value is stored as a plain string.

This option can be enable or disabled in the menu of the card *Zoo Nodes*.

![Screenshot](https://github.com/dave90/Zoo-GUI/blob/main/img/screen4.png)

### Deleting Node

To delete a node stored in zookeeper click on circled minus near to the node.

Remember that **cancellation is not reversible**

### Options

It's possible to redefine the zookeeper server IP address and the IP address of back-end that as default is setted to localhost.

![Screenshot](https://github.com/dave90/Zoo-GUI/blob/main/img/screen5.png)



