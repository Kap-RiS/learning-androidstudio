package com.example.learn_androidstudio
import androidx.annotation.DrawableRes

//Класс-контейнер для хранения информации о приложениях
data class AppItem(
    val id: Int,                    //Уникальный номер
    val name: String,               //Наименование приложения
    val description: String,        //Описание приложения
    val category: String,           //Категория приложения
    @DrawableRes val iconRes: Int   //Ссылка на иконку в ресурсах
)
