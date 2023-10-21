package com.learn.storyappbyoby.data.api

import com.learn.storyappbyoby.data.dataResponse.DetailStoryResponse
import com.learn.storyappbyoby.data.dataResponse.ListStoryResponse
import com.learn.storyappbyoby.data.dataResponse.LoginResponse
import com.learn.storyappbyoby.data.dataResponse.RegisterResponse
import com.learn.storyappbyoby.data.dataResponse.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @GET("stories")
    suspend fun getStories(
    ) : ListStoryResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Header("Authorization") token: String,
        @Path("id") id:String
    ) : DetailStoryResponse


        @Multipart
        @POST("stories")
       suspend fun uploadImage(
            //@Header("Authorization") token: String,
            @Part file: MultipartBody.Part,
            @Part("description") description: RequestBody,
        ): UploadResponse


}