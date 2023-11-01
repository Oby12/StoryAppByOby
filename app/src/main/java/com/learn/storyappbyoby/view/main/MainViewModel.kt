package com.learn.storyappbyoby.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.data.UserRepository
import com.learn.storyappbyoby.data.dataResponse.ListStoryItem
import com.learn.storyappbyoby.data.dataResponse.LoginResponse
import com.learn.storyappbyoby.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _ListStoryUser = MediatorLiveData<ResultState<List<ListStoryItem>>>()
    val listStoryUser: LiveData<ResultState<List<ListStoryItem>>> = _ListStoryUser


    fun getListStoryUser(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getStories(token).asFlow().collect {
                _ListStoryUser.value = it
            }
        }
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}

