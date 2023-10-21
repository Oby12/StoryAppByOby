package com.learn.storyappbyoby.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.data.UserRepository
import com.learn.storyappbyoby.data.dataResponse.LoginResponse
import com.learn.storyappbyoby.data.dataResponse.RegisterResponse

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _login = MediatorLiveData<ResultState<LoginResponse>>()
    val loginResponse : LiveData<ResultState<LoginResponse>> = _login

    fun login(
        email : String,
        password : String){
        _isLoading.value = true
        val liveData = repository.login(email, password)
        _login.addSource(liveData){ result ->
            _login.value = result
        }
    }
}