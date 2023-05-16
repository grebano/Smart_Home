#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

// Insert right ssid and password
#ifndef STASSID
#define STASSID "wifi_name"
#define STAPSK  "wifi_password"
#endif


const char* ssid = STASSID;
const char* password = STAPSK;

// Set port 
ESP8266WebServer server(80);

// Set connection parameters
IPAddress ip(192, 168, 0, 101); //ESP static ip
IPAddress gateway(192, 168, 0, 1);   //IP Address of your WiFi Router (Gateway)
IPAddress subnet(255, 255, 255, 0);  //Subnet mask
IPAddress dns(8, 8, 8, 8);  //DNS

// Device name
const char* deviceName = "ligth101";

// Esp01 GPIO pin that drives the relay
const int relayPin = 0;

// Handling on command
void handleOn() {
    digitalWrite(relayPin, LOW);
    setCrossOrigin();
    server.send(200, "text/plain", "Ligth on");
}

// Handling off command
void handleOff() {
    digitalWrite(relayPin, HIGH);
    setCrossOrigin();
    server.send(200, "text/plain", "Ligth off");
}

// Handling ping command
void handlePing() {
    setCrossOrigin();
    server.send(200, "text/plain", "ligth");
}

// Pin configuration
void configPin(){
    pinMode(relayPin, OUTPUT);
    digitalWrite(relayPin, HIGH);
}

// Wifi configuration and parameters setting
void configWifi(){
    WiFi.setAutoConnect(false);
    WiFi.disconnect();
    WiFi.hostname(deviceName);
    WiFi.config(ip, subnet, gateway, dns);
    WiFi.begin(ssid, password);    
    while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    }
}


void setCrossOrigin(){
    server.sendHeader(F("Access-Control-Allow-Origin"), F("*"));
    server.sendHeader(F("Access-Control-Max-Age"), F("600"));
    server.sendHeader(F("Access-Control-Allow-Methods"), F("PUT,POST,GET,OPTIONS"));
    server.sendHeader(F("Access-Control-Allow-Headers"), F("*"));
}

void sendCrossOriginHeader(){
    setCrossOrigin();
    server.send(204);
}

// Path used to manage resources
void configServerPaths(){
    server.on(F("/ping"), HTTP_OPTIONS, sendCrossOriginHeader);
    server.on(F("/ping"), HTTP_GET, handlePing);

    server.on(F("/on"), HTTP_OPTIONS, sendCrossOriginHeader);
    server.on(F("/on"), HTTP_GET, handleOn);

    server.on(F("/off"), HTTP_OPTIONS, sendCrossOriginHeader);
    server.on(F("/off"), HTTP_GET, handleOff);
    
    server.begin();
}

void setup() {
    configPin();    
    configWifi();
    configServerPaths();
}

// Handle connections
void loop() {
    server.handleClient();
}

