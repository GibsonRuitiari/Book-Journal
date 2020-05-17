package com.example.kotlinandroidpractice.bookJournal.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinandroidpractice.bookJournal.ui.model.BookClass
import com.example.kotlinandroidpractice.bookJournal.appData.BookRepository

class BookViewModel(private var repository: BookRepository):ViewModel() {
 private var bookList:MutableLiveData<ArrayList<BookClass>> = MutableLiveData()
 private var successLiveData:MutableLiveData<Boolean> = MutableLiveData()
    private var deleteLiveData:MutableLiveData<Boolean> = MutableLiveData()

fun readBooks(context: Context): LiveData<ArrayList<BookClass>> {
    val list=repository.readBookListFromStorage(context)
    if (list!!.isNotEmpty()) bookList.value= list
    return bookList
}
    fun writeBook(context: Context,bookClass: BookClass):LiveData<Boolean>{
        val success=repository.writeBookListToStorage(context,bookClass)
       successLiveData.value=success
        return successLiveData
    }
    fun deleteBook(context: Context,bookClass: BookClass):LiveData<Boolean>{
        val deletedLiveData= repository.deleteBookFromStorage(context,bookClass)
        deleteLiveData.value=deletedLiveData
        return deleteLiveData
    }
}


