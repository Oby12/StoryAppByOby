package com.learn.storyappbyoby.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.learn.storyappbyoby.data.api.ApiConfig
import com.learn.storyappbyoby.data.api.ApiService
import com.learn.storyappbyoby.data.dataResponse.DetailStoryResponse
import com.learn.storyappbyoby.data.dataResponse.ListStoryItem
import com.learn.storyappbyoby.data.dataResponse.ListStoryResponse
import com.learn.storyappbyoby.data.dataResponse.LoginResponse
import com.learn.storyappbyoby.data.dataResponse.RegisterResponse
import com.learn.storyappbyoby.data.dataResponse.Story
import com.learn.storyappbyoby.data.dataResponse.UploadResponse
import com.learn.storyappbyoby.data.pref.UserModel
import com.learn.storyappbyoby.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private suspend fun saveSession(user: UserModel) = userPreference.saveSession(user)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun signup(name : String,email : String, password : String) : LiveData<ResultState<RegisterResponse>> =
        liveData(Dispatchers.IO) {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email : String, password : String) : LiveData<ResultState<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(ResultState.Loading)
            try {
                val successResponse = apiService.login(email, password)
                val tokenUser = successResponse.loginResult.token
                saveSession(UserModel(email, tokenUser))
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            }
        }

    fun getStories(token : String) : LiveData<ResultState<List<ListStoryItem>>> =
        liveData(Dispatchers.IO) {
            emit(ResultState.Loading)
            try {

                val successResponse = apiService.getStories("Bearer $token")
                val ListStory = successResponse.listStory
                emit(ResultState.Success(ListStory))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ListStoryResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            }
        }

    fun getDetail(token : String,id : String) :LiveData<ResultState<DetailStoryResponse>> =
        liveData(Dispatchers.IO){
            emit(ResultState.Loading)
            try {
                val successResponse = apiService.getDetail(token,id)
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            }
        }

    fun uploadImage(token: String,imageFile: MultipartBody.Part, description: RequestBody) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.uploadImage("Bearer $token",imageFile,description)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

       fun getInstance(
           apiService: ApiService,
           userPreference: UserPreference
       ) : UserRepository =
           instance ?: synchronized(this) {
               instance ?: UserRepository(apiService,userPreference)
           }.also { instance = it }
    }
}