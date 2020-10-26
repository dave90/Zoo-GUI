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
