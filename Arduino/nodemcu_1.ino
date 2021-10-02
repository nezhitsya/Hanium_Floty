#include "DHT.h"
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST ""
#define FIREBASE_AUTH ""
#define WIFI_SSID ""
#define WIFI_PASSWORD ""

// #define neo_pin D5
// Adafruit_NeoPixel RGB_LED = Adafruit_NeoPixel(10, 6, NEO_GRB);

#define cds A0

String fireStatus = "";

void setup() {
  Serial.begin(115200);

  pinMode(LED_BUILTIN, OUTPUT);     
  pinMode(cds, INPUT);
 
//  RGB_LED.begin();
//  RGB_LED.setBrightness(20);                  
//  RGB_LED.clear();

  connectWifi();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  Firebase.setBool("/mode/auto_mode", true);
  Firebase.setBool("/mode/manual_mode", false);
  
  Firebase.setString("/temp_hum/fan_onoff", "off");
  Firebase.setString("/soil_temp/pump_onoff", "off");    
  Firebase.setString("/illuminance/led_onoff", "off");     
}

 void loop() {
  // fireStatus = Firebase.getString("LED_STATUS");                                     
 
  int cds = analogRead(A0);
  Serial.print("illuminance: ");     
  Serial.println(cds);
  Firebase.setInt("/illuminance/cds_rate", cds);

//  Firebase.setString("/illuminance/led_onoff", "off");

}


void connectWifi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);                
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());                                               
  }

