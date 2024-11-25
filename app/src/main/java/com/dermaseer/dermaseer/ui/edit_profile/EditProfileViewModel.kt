package com.dermaseer.dermaseer.ui.edit_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermaseer.dermaseer.data.remote.models.UserResponse
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
   private val userRepository: UserRepository
): ViewModel() {
   private var _userData = MutableLiveData<UserResponse>()
   val userData: LiveData<UserResponse> = _userData

   private var _newUserData = MutableLiveData<UserResponse>()
   val newUserData: LiveData<UserResponse> = _newUserData

   init {
      getUserData()
   }

   private fun getUserData() {
      viewModelScope.launch {
         try {
            _userData.value = userRepository.getCurrentUser()
         } catch (e: HttpException) {
            Log.e("GetUser", "Error")
         }
      }
   }

   fun updateUserData(
      name: RequestBody,
      birthday: RequestBody,
      gender: RequestBody,
      profilePicture: RequestBody
   ) {
      viewModelScope.launch {
         try {
            _newUserData.value = userRepository.updateUser(name, birthday, gender, profilePicture)
         } catch (e: Exception) {
            Log.e("EditUserData", "${e.message}")
         }
      }
   }
}