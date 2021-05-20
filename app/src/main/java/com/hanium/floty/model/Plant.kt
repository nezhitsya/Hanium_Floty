package com.hanium.floty.model

data class  Plant (
        var plants: ArrayList<PlantInfo>
)

data class PlantInfo (
        var distbNm: String, // 이름
        var imageEvlLinkCours: String, // 이미지 url
        var hdCode: String, // 습도 코드
        var grwhTpCode: String, // 온도 코드
        var watercycleSprngCode: String, // 물 주기 봄
        var watercycleSummerCode: String, // 물 주기 여름
        var watercycleAutumnCode: String, // 물 주기 가을
        var watercycleWinterCode: String // 물 주기 겨울
)