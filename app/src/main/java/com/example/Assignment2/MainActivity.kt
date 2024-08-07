package com.example.myapplication12

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("user_token", "") ?: ""
    val retrofit = getRetrofit(token)
    val apiService = provideApiService(retrofit)

    CompositionLocalProvider(LocalApiService provides apiService) {
        Surface(color = MaterialTheme.colors.background) {
            AppNavigation(navController)
        }
    }
}

fun getRetrofit(token: String): Retrofit {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val client = OkHttpClient.Builder()
        .build()

    return Retrofit.Builder()
        .baseUrl("https://todos.simpleapi.dev")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}
