package com.dermaseer.dermaseer.utils

import com.dermaseer.dermaseer.BuildConfig

object ApiConstant {
   const val BASE_URL = BuildConfig.BASE_URL
   const val GET_ALL_ARTICLE = "api/article"
   const val GET_PRODUCT_BY_CATEGORY = "api/product"
   const val GET_CURRENT_USER = "api/users/current"
   const val UPDATE_USER = "api/users/current/data"
   const val DELETE_CURRENT_USER = "api/users/current"
   const val PREDICT_MODEL = "api/model/predict"
   const val GET_HISTORY = "api/model/predictions"
   const val INGREDIENT_RECOMMENDATION = "api/model/recomendation"
   const val GET_PRODUCT_RECOMMENDATION = "api/model/product"
}