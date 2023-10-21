package com.learn.storyappbyoby.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.data.UserRepository
import com.learn.storyappbyoby.data.api.ApiConfig
import com.learn.storyappbyoby.data.dataResponse.DetailStoryResponse
import com.learn.storyappbyoby.data.dataResponse.ListStoryItem
import com.learn.storyappbyoby.data.dataResponse.Story
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class DetailViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStoryLiveData = MediatorLiveData<ResultState<DetailStoryResponse>>()
    val detailStoryLiveData: LiveData<ResultState<DetailStoryResponse>> = _detailStoryLiveData

    fun getDetail(token : String,id : String){
        _isLoading.value = true
        val livedata = repository.getDetail(token,id)
        _detailStoryLiveData.addSource(livedata){ result ->
            _detailStoryLiveData.value = result
        }
    }
//    fun getDetail(storyId: String) {
//        viewModelScope.launch {
//            _detailStoryLiveData.value = ResultState.Loading
//            val result = repository.getDetail(storyId)
//            when (result) {
//                is  ResultState.Success -> {
//                    val storyDetail = result.data
//                    _detailStoryLiveData.value = ResultState.Success(storyDetail)
//                }
//                is ResultState.Error -> {
//                    _detailStoryLiveData.value = ResultState.Error(result.error)
//                }
//            }
//        }
//    }

//    private val _detail = MutableLiveData<Story>()
//    val detail: LiveData<Story> = _detail

//    fun getDetail(query: String){
//        _isLoading.value =true
//        val data = repository.getDetail(query)
//        _detail.addSource(data) { result ->
//            _detail.value = result
//        }
//
//    }

//    suspend fun storyDetail(token: String, id: String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService(token = token).getDetail(id = id)
//        client.enqueue(object : Callback<Story> {
//            override fun onResponse(
//                call: Call<Story>,
//                response: Response<Story>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _detail.postValue(response.body())
//                } else {
//                    Log.e(DetailActivity.TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Story>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(DetailActivity.TAG, "onFailure: ${t.message.toString()}")
//            }
//        })
//    }
    /*suspend fun storyDetail(query: String) {
        _isLoading.value = true
        try {
            val response = ApiConfig.getApiService(token = query).getDetail(id = query)
            if (response.isSuccessful) {
                _detail.postValue(response.body())
            } else {
                Log.e(DetailActivity.TAG, "onFailure: ${response.message()}")
            }
        } catch (t: Throwable) {
            _isLoading.value = false
            Log.e(DetailActivity.TAG, "onFailure: ${t.message.toString()}")
        }
    }*/

}