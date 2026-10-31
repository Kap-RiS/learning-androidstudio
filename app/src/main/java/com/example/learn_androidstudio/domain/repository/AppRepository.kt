package com.example.learn_androidstudio.domain.repository

import com.example.learn_androidstudio.domain.model.AppItem

interface AppRepository {
    fun getApps(): List<AppItem>
    fun getAppById(id: Int): AppItem?
}