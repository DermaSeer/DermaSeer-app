package com.dermaseer.dermaseer.di

import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import com.dermaseer.dermaseer.data.repository.article.ArticleRepositoryImpl
import com.dermaseer.dermaseer.data.repository.predict.HistoryRepository
import com.dermaseer.dermaseer.data.repository.predict.HistoryRepositoryImpl
import com.dermaseer.dermaseer.data.repository.predict.IngredientRepository
import com.dermaseer.dermaseer.data.repository.predict.IngredientRepositoryImpl
import com.dermaseer.dermaseer.data.repository.predict.PredictRepository
import com.dermaseer.dermaseer.data.repository.predict.PredictRepositoryImpl
import com.dermaseer.dermaseer.data.repository.predict.ProductRecommendationRepository
import com.dermaseer.dermaseer.data.repository.predict.ProductRecommendationRepositoryImpl
import com.dermaseer.dermaseer.data.repository.product.ProductRepository
import com.dermaseer.dermaseer.data.repository.product.ProductRepositoryImpl
import com.dermaseer.dermaseer.data.repository.user.UserRepository
import com.dermaseer.dermaseer.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
   @Binds
   @Singleton
   abstract fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository

   @Binds
   @Singleton
   abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

   @Binds
   @Singleton
   abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

   @Binds
   @Singleton
   abstract fun bindHistoryRepository(historyRepositoryImpl: HistoryRepositoryImpl): HistoryRepository

   @Binds
   @Singleton
   abstract fun bindIngredientRepository(ingredientRepositoryImpl: IngredientRepositoryImpl): IngredientRepository

   @Binds
   @Singleton
   abstract fun bindPredictRepository(predictRepositoryImpl: PredictRepositoryImpl): PredictRepository

   @Binds
   @Singleton
   abstract fun bindProductRecommendationRepository(productRecommendationRepositoryImpl: ProductRecommendationRepositoryImpl): ProductRecommendationRepository
}