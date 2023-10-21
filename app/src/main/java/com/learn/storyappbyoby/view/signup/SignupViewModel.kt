package com.learn.storyappbyoby.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learn.storyappbyoby.data.ResultState
import com.learn.storyappbyoby.data.UserRepository
import com.learn.storyappbyoby.data.dataResponse.RegisterResponse

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _signUp = MediatorLiveData<ResultState<RegisterResponse>>()
    val signupResponse : LiveData<ResultState<RegisterResponse>> = _signUp

    fun signup(
        name:String,
        email : String,
        password : String){
        _isLoading.value = true
        val liveData = repository.signup(name, email, password)
        _signUp.addSource(liveData){ result ->
            _signUp.value = result
        }
    }

    //        viewModelScope.launch { repository.signup(name, email, password) }

    //fun signupRepo(name: String,email: String,password: String) = repository.signup(name, email, password)

//    companion object {
//        private val TAG = SignupViewModel::class.java.simpleName
//    }

}