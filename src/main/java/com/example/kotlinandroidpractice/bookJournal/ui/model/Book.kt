package com.example.kotlinandroidpractice.bookJournal.ui.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class BookClass(val bookName:String,val bookDescription:String, val bookAuthor:String){
   val dateAdded:String
    init{
        val formatter=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.US)
        dateAdded=formatter.format(LocalDateTime.now())
    }
    override fun toString(): String {
        return "bookName: $bookName\n bookDescription: $bookDescription \n dateAdded: $dateAdded"
    }
}