package com.dermaseer.dermaseer.di

import com.dermaseer.dermaseer.data.repository.article.ArticleRepository
import com.dermaseer.dermaseer.data.repository.article.ArticleRepositoryImpl
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
}