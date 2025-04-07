package com.example.traderapp.data


import android.content.Context
import dagger.Module
import com.example.traderapp.data.AuthRepository
import com.example.traderapp.data.FirebaseAuthRepository
import com.example.traderapp.data.network.CryptoApi
import com.example.traderapp.data.network.RetrofitInstance
import com.example.traderapp.data.network.UserSession
import com.example.traderapp.data.network.WebSocketClient
import com.example.traderapp.utils.AuthPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository {
        return FirebaseAuthRepository(auth, db)
    }

    @Provides
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences {
        return AuthPreferences(context)
    }

    @Provides
    @Singleton
    fun provideCryptoApi(): CryptoApi {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(): WebSocketClient {
        return WebSocketClient()
    }

}
