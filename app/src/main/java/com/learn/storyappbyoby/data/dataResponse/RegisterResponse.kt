package com.learn.storyappbyoby.data.dataResponse

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class RegisterResponse (
    @field:SerializedName("error")
    val error : Boolean,

    @field:SerializedName("message")
    val message : String
)