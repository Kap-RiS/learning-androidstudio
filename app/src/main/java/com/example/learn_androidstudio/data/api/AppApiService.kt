package com.example.learn_androidstudio.data.api

import com.example.learn_androidstudio.data.dto.AppItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AppApiService {

    @GET("catalog")
    suspend fun getCatalog(): List<AppItemDto>

    @GET("{id}")
    suspend fun getAppDetail(
        @Path("id") id: String
    ): AppItemDto
}