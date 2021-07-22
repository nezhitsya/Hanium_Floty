package com.hanium.floty.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Diary (
        var publisher: String? = "",
        var date: String? = "",
        var weather: String? = "",
        var title: String? = "",
        var description: String? = "",
        var image: String? = ""
)