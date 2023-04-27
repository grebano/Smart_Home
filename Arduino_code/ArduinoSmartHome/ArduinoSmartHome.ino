#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

#ifndef STASSID
#define STASSID "wifi_name"
#define STAPSK  "wifi_password"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;

ESP8266WebServer server(80);

IPAddress ip(192, 168, 0, 101); //ESP static ip
IPAddress gateway(192, 168, 0, 1);   //IP Address of your WiFi Router (Gateway)
IPAddress subnet(255, 255, 255, 0);  //Subnet mask
IPAddress dns(8, 8, 8, 8);  //DNS
const char* deviceName = "ligth101";

const int relayPin = 0;

void handleOn() {
    digitalWrite(relayPin, LOW);
    setCrossOrigin();
    server.send(200, "text/plain", "Ligth on");
}

void handleOff() {
    digitalWrite(relayPin, HIGH);
    setCrossOrigin();
    server.send(200, "text/plain", "Ligth off");
}

void handlePing() {
    setCrossOrigin();
    server.send(200, "text/plain", "ligth");
}

void configPin(){
    pinMode(relayPin, OUTPUT);
    digitalWrite(relayPin, HIGH);
}

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

void loop() {
    server.handleClient();
}

