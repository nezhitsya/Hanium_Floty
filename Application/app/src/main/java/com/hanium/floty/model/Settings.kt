package com.hanium.floty.model

data class Mode (
        var auto_mode: Boolean = false, // 자동모드
        var manual_mode: Boolean = true // 수동모드
)

data class History(
        var cds_rate: Int = 0, // 조도 기록
        var hum_rate: Int = 0, // 습도 기록
        var soil_rate: Int = 0, // 토양 습도 기록
        var temp_rate: Int = 0, // 온도 기록
)

data class Illuminance(
        var cds_auto_set: Int = 0, // led 가동 조건 조도값  -  1 ~ 1023
        var cds_rate: Int = 0, // 현재 조도 값
        var led_brightness: Int = 0, // led 밝기  -  1 ~ 255
        var led_onoff: String = "" // 가동 조건 조도값에 따른 led onoff 여부
)

data class SoilTemp (
        var pump_onoff: String? = "", // 펌프 onoff
        var pump_uptime: Int = 0, // 펌프 가동시간  -  1000 ~ 5000
        var soil_auto_set: Int = 0, // 펌프 가동 조건 습도 값  -  1 ~ 1023
        var soil_rate: Int = 0 // 현재 토양 습도 값
)

data class TempHum(
        var fan_onoff: String? = "", // 팬 가동 여부
        var fan_uptime: Int = 0, // 팬 가동시간
        var hum_rate: Int = 0, // 현재 습도 값
        var temp_auto_set: Int = 0, // 팬 가동 조건 온도 값  -  1 ~ 50
        var temp_rate: Int = 0 // 현재 온도 값
)