package com.dermaseer.dermaseer.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dermaseer.dermaseer.data.remote.services.ArticleService
import com.dermaseer.dermaseer.data.remote.services.HistoryService
import com.dermaseer.dermaseer.data.remote.services.IngredientService
import com.dermaseer.dermaseer.data.remote.services.PredictService
import com.dermaseer.dermaseer.data.remote.services.ProductRecommendationService
import com.dermaseer.dermaseer.data.remote.services.ProductService
import com.dermaseer.dermaseer.data.remote.services.UserService
import com.dermaseer.dermaseer.utils.ApiConstant
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

   @Provides
   @Singleton
   fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
      return ChuckerInterceptor.Builder(context).build()
   }

   @Provides
   @Singleton
   fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
      return OkHttpClient.Builder()
         .addInterceptor(chuckerInterceptor)
         .connectTimeout(1, TimeUnit.MINUTES)
         .build()
   }

   @Provides
   @Singleton
   fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
      return Retrofit.Builder()
         .baseUrl(ApiConstant.BASE_URL)
         .client(okHttpClient)
         .addConverterFactory(GsonConverterFactory.create())
         .build()
   }

   @Provides
   @Singleton
   fun provideArticleService(retrofit: Retrofit): ArticleService {
      return retrofit.create(ArticleService::class.java)
   }

   @Provides
   @Singleton
   fun provideProductService(retrofit: Retrofit): ProductService {
      return retrofit.create(ProductService::class.java)
   }

   @Provides
   @Singleton
   fun provideUserService(retrofit: Retrofit): UserService {
      return retrofit.create(UserService::class.java)
   }

   @Provides
   @Singleton
   fun provideHistoryService(retrofit: Retrofit): HistoryService {
      return retrofit.create(HistoryService::class.java)
   }

   @Provides
   @Singleton
   fun provideIngredientService(retrofit: Retrofit): IngredientService {
      return retrofit.create(IngredientService::class.java)
   }

   @Provides
   @Singleton
   fun providePredictService(retrofit: Retrofit): PredictService {
      return retrofit.create(PredictService::class.java)
   }

   @Provides
   @Singleton
   fun provideProductRecommendationService(retrofit: Retrofit): ProductRecommendationService {
      return retrofit.create(ProductRecommendationService::class.java)
   }

   @Provides
   @Singleton
   fun provideFirebaseAuth(): FirebaseAuth {
      return FirebaseAuth.getInstance()
   }

   @Provides
   fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
      return CredentialManager.create(context)
   }

   @Provides
   fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
      val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
      return GoogleSignIn.getClient(context, options)
   }
}