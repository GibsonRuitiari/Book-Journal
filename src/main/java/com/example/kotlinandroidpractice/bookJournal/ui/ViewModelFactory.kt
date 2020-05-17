package com.example.kotlinandroidpractice.bookJournal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinandroidpractice.bookJournal.appData.BookRepository

class ViewModelFactory(private val repository: BookRepository?):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            BookRepository::class.java
        ).newInstance(repository)
    }
}