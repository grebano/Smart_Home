# Smart Home Project :house_with_garden:	
An home automation enviroment in wich an Android application interfaces with an Arduino in orded to control lights, security, doors, sensors... .
The idea is to have an appliction that uses http methods to dialogue with esp01 modules, which can turn on and off multiple devices.
Each esp01 controls a relay module and has an ip address so it can be reached through local wifi network.

## :hammer_and_wrench: Hardware
- Android smartphone
- Esp01/Esp01s relay module
- 5v power supply

## :computer: Software
- Arduino IDE (for esp01)
- Android Studio (for Android app)

## :bulb: How it works
The Android application sends http requests to the esp01 modules, which can turn on and off the relay modules. The modules are wired to the devices that we want to actually control (lights, shutters ...). Obviously the esp01 modules are connected to the local wifi network and have an ip address, so they can be reached by the Android application.

## :iphone: Android application
The application is composed by 3 fragments:
- Lights
- Shutters
- Automations

You can also find a settings page where you can set the ip address of the esp01 modules, shutters opening and closing time..., and an info page that briefly explains how the application works.

The application is written in Java and xml and can be installed on any Android smartphone with Android 7.0 or higher. In order to install it you have to download the apk file from the release section of this repository and install it on your smartphone. You can also download the source code and open it with Android Studio.

### Screenshots
![App](/Images/example.png)

## :electric_plug:	 Arduino code
The code is written in C++ and is saved in the file [arduino_code](Arduino_code/ArduinoSmartHome/ArduinoSmartHome.ino). It is commented and is easy to understand, is also possible to program the esp01 modules using PlatformIO adding ```#include <Arduino.h>``` at the beginning of the code and properly configuring the platformio.ini file.

Is clearly necessary to modify the code in order to use it with your esp01 modules. You have to change the ssid and password of your wifi network, you can also change the name of the devices that you want to control.

In order to program the esp01 modules you have to connect them to the computer using an appropriate USB programmer. You can find a lot of tutorials on the internet. You may also need to modifiy the programmer by adding a button to put the esp01 in programming mode.

It should be possible to use an esp32 or other modules with wifi capabilities, but this possibility has not been tested yet. In this case you may have to modify the code in order to use the appropriate libraries.

### Esp01 relay module:

![Esp01](/Images/ESP01.png)
