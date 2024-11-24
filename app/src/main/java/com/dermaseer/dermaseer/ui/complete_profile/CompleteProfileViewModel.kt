package com.dermaseer.dermaseer.ui.complete_profile

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
import javax.inject.Inject

@HiltViewModel
class CompleteProfileViewModel @Inject constructor(
   private val userRepository: UserRepository
): ViewModel() {
   private var _userData = MutableLiveData<UserResponse>()
   val userData: LiveData<UserResponse> = _userData

   fun completeUserData(
      name: RequestBody,
      birthday: RequestBody,
      gender: RequestBody,
      profilePicture: RequestBody
   ) {
      viewModelScope.launch {
         try {
            Log.d("CheckPhotoVM", "$profilePicture")
            _userData.value = userRepository.updateUser(name, birthday, gender, profilePicture)
         } catch (e: Exception) {
            Log.e("CompleteUserData", "${e.message}")
         }
      }
   }
}