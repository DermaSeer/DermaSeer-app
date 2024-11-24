package com.dermaseer.dermaseer.ui.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dermaseer.dermaseer.utils.ResultState
import com.dermaseer.dermaseer.data.remote.models.ProductResponse
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

class ProductDetailViewModel : ViewModel() {

    private val _productDetails = MutableLiveData<ResultState>()
    val productDetails: LiveData<ResultState> get() = _productDetails

    fun fetchProductDetails(productJson: String) {
        _productDetails.value = ResultState.Loading
        try {
            val product = Gson().fromJson(productJson, ProductResponse.Data::class.java)

            val formattedPrice = product.price?.let {
                NumberFormat.getCurrencyInstance(Locale.getDefault()).format(it)
            }

            val successMessage = "Product loaded successfully"
            _productDetails.value = ResultState.Success(successMessage)

        } catch (e: Exception) {
            _productDetails.value = ResultState.Error("Failed to load product")
        }
    }
}
