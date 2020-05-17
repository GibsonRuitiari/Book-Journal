package com.example.kotlinandroidpractice.bookJournal.appData

import android.content.Context
import android.os.Environment
import com.example.kotlinandroidpractice.bookJournal.ui.model.BookClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.nio.file.Files
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class BookRepository {

     fun readBookListFromStorage( context: Context): ArrayList<BookClass>? {
        val storageDir =context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val fileName= File("$storageDir","appData.json")
        val gson= Gson()
        var reader: Reader?
        val type= object: TypeToken<List<BookClass>>() {}.type
        val bookList: ArrayList<BookClass>? = ArrayList()
        val readerTask = CompletableFuture.supplyAsync(Supplier<ArrayList<BookClass>>{
            if (Files.exists(fileName.toPath())){
                reader = Files.newBufferedReader(fileName.toPath())
                val currentList=gson.fromJson<List<BookClass>>(reader,type)
                if (currentList.isNotEmpty()) bookList?.addAll(currentList)
                (reader as BufferedReader?)!!.close()
            }
            return@Supplier bookList!!
        })

        return readerTask.get()
    }
    fun deleteBookFromStorage(context: Context,book:BookClass):Boolean{
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val fileName = File("$storageDir", "appData.json")
        val deleteTask= CompletableFuture.supplyAsync(Supplier<Boolean>{
            var returnValue:Boolean?= null
            val gson=Gson()
            val reader:Reader?
            val writer:Writer?
            val type = object : TypeToken<List<BookClass>>() {
            }.type
            val tempList:ArrayList<BookClass>?= ArrayList()
            try{
                if (Files.exists(fileName.toPath())){
                    reader= Files.newBufferedReader(fileName.toPath())
                    val bookList= gson.fromJson<List<BookClass>>(reader!!,type)
                    tempList?.addAll(bookList)
                    tempList?.removeIf {
                        it.bookName==book.bookName
                    }
                    writer= Files.newBufferedWriter(fileName.toPath())
                    writer!!.write(gson.toJson(tempList,type))
                    writer.flush()
                    writer.close()
                    returnValue=true
                }
            }catch (e:IOException){
                e.printStackTrace()
                returnValue=false
            }
            return@Supplier returnValue!!
        })
        return deleteTask.get()
    }
     fun writeBookListToStorage(context: Context,book: BookClass): Boolean {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val fileName = File("$storageDir", "appData.json")
        val writeTask= CompletableFuture.supplyAsync(Supplier<Boolean>{
            var returnValue:Boolean
            val gson = Gson()
            val writer: Writer?
            val reader: Reader?
            val type = object : TypeToken<List<BookClass>>() {
            }.type
            val tempList: ArrayList<BookClass>? =  ArrayList()
            val originalList= object: ArrayList<BookClass>() {init {
                add(book) }}
            try{
                if(Files.exists(fileName.toPath())){
                    reader= Files.newBufferedReader(fileName.toPath())
                    val currentList= gson.fromJson<List<BookClass>>(reader!!,type)
                    tempList?.addAll(currentList)
                    tempList?.removeIf{
                        it.bookName == book.bookName
                    }
                    tempList?.add(book)
                    /* if you use FileOutputStream you might have
                    to force a fsync yourself so fos.pd.fsync()
                     */
                    writer= Files.newBufferedWriter(fileName.toPath())
                    writer!!.write(gson.toJson(tempList,type))
                    writer.flush()
                    writer.close()
                    returnValue=true
                }else{
                    writer=Files.newBufferedWriter(fileName.toPath())
                    writer!!.write(gson.toJson(originalList,type))
                    writer.close()
                    returnValue=false }
            }catch(ex: IOException){
                ex.printStackTrace()
                returnValue=false
            }
            return@Supplier returnValue
        })
       return writeTask.get()
    }

}