package com.learn.storyappbyoby.view.upload_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.data.UserRepository
import com.learn.storyappbyoby.data.dataResponse.UploadResponse
import com.learn.storyappbyoby.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadStory = MediatorLiveData<ResultState<UploadResponse>>()
    val uploadStory: LiveData<ResultState<UploadResponse>> = _uploadStory

    val isLoading : LiveData<Boolean> = repository.isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadStory(imageFile: MultipartBody.Part, description: RequestBody) {
        val liveData = repository.uploadImage(imageFile, description)
        _uploadStory.addSource(liveData) { result ->
            _uploadStory.value = result
        }
    }
}