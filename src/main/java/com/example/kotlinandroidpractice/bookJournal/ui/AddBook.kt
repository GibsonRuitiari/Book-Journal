package com.example.kotlinandroidpractice.bookJournal.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinandroidpractice.R
import com.example.kotlinandroidpractice.bookJournal.ui.model.BookClass
import com.example.kotlinandroidpractice.bookJournal.appData.BookRepository
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_add_book.*
import java.io.*
import java.nio.file.Files
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

class AddBook : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_add_book)
       supportActionBar?.title="Book Journal"
        val addLayout= findViewById<LinearLayout>(R.id.add_layout)
        val view=findViewById<CoordinatorLayout>(R.id.snackbar_parent)
        val viewModel: BookViewModel = ViewModelProvider(this,
            ViewModelFactory(BookRepository())
        ).get(BookViewModel::class.java)
        add_book.setOnClickListener(View.OnClickListener {
            val bookDescription = bookDesc.editText?.text.toString()
            val bookNam= bookName.editText?.text.toString()
            val bokAuthor= bookAuthor.editText?.text.toString()
            if (bookNam.isNotEmpty() && bookNam.isNotBlank() && bookDescription.isNotEmpty()  && bookDescription.isNotBlank() && bokAuthor.isNotEmpty() && bokAuthor.isNotBlank()) {
                val bookTask = viewModel.writeBook(applicationContext,
                    BookClass(
                        bookNam,
                        bookDescription,
                        bokAuthor
                    )
                )

                bookTask.observe(this, Observer {
                    if (it){
                        Snackbar.make(view,"Book was saved successfully",Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show()
                        bookName.editText?.text?.clear()
                        bookDesc.editText?.text?.clear()
                        bookAuthor.editText?.text?.clear()
                    }else{
                        Snackbar.make(view,"Failed to save the book. Try again later",Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show()
                    }
                })
               /* val success = writeBookListToStorage(
                    BookClass(
                        bookNam,
                        bookDescription,bokAuthor))
              ////  myIntent.putExtra(extraData.stringExtra,success)
                if (success) {
                    Snackbar.make(view,"Book was saved successfully",Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show()
                    bookName.editText?.text?.clear()
                    bookDesc.editText?.text?.clear()
                    bookAuthor.editText?.text?.clear()
                    addLayout.visibility=View.VISIBLE

                } else{
                    addLayout.visibility=View.VISIBLE

                    Snackbar.make(view,"Failed to save the book. Try again later",Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show()
                }*/
            } else {
                addLayout.visibility=View.VISIBLE

                Snackbar.make(view,"All fields must be filled!",Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLACK).setTextColor(Color.WHITE).show()
            }
        })

    }

    private fun writeBookListToStorage(book: BookClass): Boolean {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val fileName = File("$storageDir", "appData.json")
return CompletableFuture.supplyAsync(Supplier<Boolean>{
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
       writer= Files.newBufferedWriter(fileName.toPath())
       writer!!.write(gson.toJson(tempList,type))
        writer.close()
       returnValue=true
         }else{
             writer=Files.newBufferedWriter(fileName.toPath())
            writer!!.write(gson.toJson(originalList,type))
           writer.close()
           returnValue=false }
     }catch(ex:IOException){
          ex.printStackTrace()
         returnValue=false
}
return@Supplier returnValue
}).get()
    }
}
