#include "DHT.h"
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST ""
#define FIREBASE_AUTH ""
#define WIFI_SSID ""
#define WIFI_PASSWORD ""

#define DHTPIN D11                             
#define DHTTYPE DHT11
DHT dht(DHTPIN,DHTTYPE);     

#define Fan D9
#define pump_motor D4

String fireStatus = "";   
boolean auto_mode;
boolean manual_mode;

String pump_move = "";
String fan_move = "";      
String led_move = "";      

int cds_status = 0;
int led_brightness = 0;
int fan_uptime = 0;
int pump_uptime = 0;


// auto mode setting 
int cds_auto_set = 0;
int soil_auto_set = 0;
int temp_auto_set = 0;

void setup() {
  Serial.begin(115200);
  pinMode(pump_motor, OUTPUT);
  pinMode(Fan, OUTPUT);    
  dht.begin();   
  
  //wifi connection
  connectWifi();
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
  // start in auto mode                                  
  Firebase.setBool("/mode/auto_mode", true);
  Firebase.setBool("/mode/manual_mode", false);
  
  Firebase.setString("/temp_hum/fan_onoff", "off");
  Firebase.setString("/soil_temp/pump_onoff", "off");    
  Firebase.setString("/illuminance/led_onoff", "off");                                     

}

 void loop() {
  auto_mode = Firebase.getBool("/mode/auto_mode");
  manual_mode = Firebase.getBool("/mode/manual_mode");
  
  // auto mode setting value
  cds_auto_set = Firebase.getInt("/illuminance/cds_auto_set");
  soil_auto_set = Firebase.getInt("/soil_temp/soil_auto_set"); 
  temp_auto_set = Firebase.getInt("/temp_hum/temp_auto_set");


  // Auto mode-------------------
  if (auto_mode == true && manual_mode == false) {
    Serial.println("--Auto Mode--");
 
    
  // illuminance - Led
  cds_status = Firebase.getInt("/illuminance/cds_rate");
  if (cds_status <= cds_auto_set) { 
    Firebase.setString("/illuminance/led_onoff", "on");
    Serial.println("LED ON");
    }  
  else {
    Firebase.setString("/illuminance/led_onoff", "off");
    Serial.println("LED OFF");
    }

    
  // temperature - Fan
  int temp = Temperature(); 
  fan_move = Firebase.getString("/temp_hum/fan_onoff");

  if (temp >= temp_auto_set) {
    Firebase.setString("/temp_hum/fan_onoff", "on");
    Serial.println("Fan ON -");
    }
    
  else {
    Firebase.setString("/temp_hum/fan_onoff", "off");
    Serial.println("Fan OFF -");
    }
  
    
  // soil humidity - pump 
  int soil = Soil_moisture();
  pump_move = Firebase.getString("/soil_temp/pump_onoff");

  if (soil <= soil_auto_set) {
    Firebase.setString("/soil_temp/pump_onoff", "on");
    Serial.println("Pump ON"); 
    }
    
  else {
    Firebase.setString("/soil_temp/pump_onoff", "off");
    Serial.println("Pump OFF");
    }
    
    
  // Fan 
  fan_move = Firebase.getString("/temp_hum/fan_onoff");
  
   if(fan_move == "on" || fan_move == "ON"){
      digitalWrite(Fan, LOW);
      Serial.println("Fan ON");
    }
    
   else if(fan_move == "off" || fan_move == "OFF") {
     digitalWrite(Fan, HIGH);
     Serial.println("Fan OFF");
    }
    
   else {
     Serial.println("Please send ON/OFF");
    }


  // Pump Motor 
   pump_move = Firebase.getString("/soil_temp/pump_onoff");

   if(pump_move == "on" || pump_move == "ON"){
      digitalWrite(pump_motor, LOW);           
      Serial.println("pump ON");
    }
    
   else if(pump_move == "off" || pump_move == "OFF") {
     digitalWrite(pump_motor, HIGH);           
     Serial.println("pump OFF");
    }
    
   else {
     Serial.println("Please send ON/OFF");
    }

  }


  // Manual mode------------------
  else if(auto_mode == false && manual_mode == true) {
    Serial.println("--Manual Mode--");

   cds_status = Firebase.getInt("/illuminance/cds_rate");
   led_brightness = Firebase.getInt("/illuminance/led_brightness");
   led_move = Firebase.getString("/illuminance/led_onoff");
   
    
   if(led_brightness != 0 && (led_move == "on" || led_move == "ON")){
   // brightness range - 1 ~ 255   
   // led strip ON 
   // RGB_LED.setBrightness(led_brightness);
   // RGB_Color(RGB_LED.Color(0, 0, led_brightness)); //blue
   }

   else if(led_move == "off" || led_move == "OFF"){
   // led strip OFF 
   // RGB_LED.setBrightness(0);
   // RGB_Color(RGB_LED.Color(0, 0, 0)); 
    }
    
   else {
    }

   
   fan_uptime = Firebase.getInt("/temp_hum/fan_uptime");
   fan_move = Firebase.getString("/temp_hum/fan_onoff");
   
   if(fan_uptime != 0 && (fan_move == "on" || fan_move == "ON")){
      digitalWrite(Fan, LOW); 
      Serial.println("Fan ON"); 
      delay(fan_uptime); 
     
      digitalWrite(Fan, HIGH); 
      Serial.println("Fan OFF"); 
      Firebase.setString("/temp_hum/fan_onoff", "off");
   }
    
   else if(fan_move == "off" || fan_move == "OFF"){
     digitalWrite(Fan, HIGH);
     Serial.println("Fan OFF");  
    }
   else {
    }


    pump_uptime = Firebase.getInt("/soil_temp/pump_uptime");
    pump_move = Firebase.getString("/soil_temp/pump_onoff");

    if(pump_uptime != 0 && (pump_move == "on" || pump_move == "ON")){
      digitalWrite(pump_motor, LOW);           
      Serial.println("pump ON");
      delay(pump_uptime); // pump_uptime range : 1000ms ~ 5000ms
     
      digitalWrite(pump_motor, HIGH);           
      Serial.println("pump OFF");
      Firebase.setString("/soil_temp/pump_onoff", "off");
      }
    
    else if(pump_move == "off" || pump_move == "OFF") {
     digitalWrite(pump_motor, HIGH);           
     Serial.println("pump OFF");
      }
    
    else {
      }
      
    }  
  
  else {
    Serial.println("Choose a mode");
    }  


  // Soil Humidity
  int soil = Soil_moisture();
  Firebase.setInt("/soil_temp/soil_rate", soil);     
   
  // Soil Humidity -> history
  Firebase.setInt("/history/soil_rate", soil);     

  // Temperature + Humidity
  int temp = Temperature();
  int hum = Humidity();
  Firebase.setInt("/temp_hum/hum_rate", hum);
  Firebase.setInt("/temp_hum/temp_rate", temp);
   
  // temp+humi -> history
  Firebase.setInt("/history/hum_rate", hum);
  Firebase.setInt("/history/temp_rate", temp);
    
}

 // Sensing

 // Temperature measurement
 int Temperature() {
  float temp = dht.readTemperature();        
  Serial.print("온도: ");  
  Serial.print((int)temp);  
  Serial.print(" *C, "); 
  return (int)temp;
  }
  
 // Humidity measurement
 int Humidity() {
  float hum = dht.readHumidity();            
  Serial.print("습도: ");   
  Serial.print((int)hum);   
  Serial.println(" %");
  return (int)hum;
  }

 // Soil humidity measurement
 int Soil_moisture() {
  int soil = analogRead(A0);  
  Serial.print("토양 습도: ");     
  Serial.println(soil);
  return (int)soil;      
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


