package com.hanium.floty.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var nickname: String? = "",
    var profile: String? = "",
    var year: String? = "",
    var month: String? = "",
    var day: String? = ""
)