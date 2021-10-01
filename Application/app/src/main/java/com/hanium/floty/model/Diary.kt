package com.hanium.floty.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Diary (
        var diaryid: String? = "",
        var publisher: String? = "",
        var year: String? = "",
        var month: String? = "",
        var day: String? = "",
        var weather: String? = "",
        var title: String? = "",
        var description: String? = "",
        var image: String? = ""
)