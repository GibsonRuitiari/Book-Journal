package com.example.kotlinandroidpractice.bookJournal.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinandroidpractice.R
import com.example.kotlinandroidpractice.bookJournal.adapter.BookAdapter
import com.example.kotlinandroidpractice.bookJournal.ui.model.BookClass
import com.example.kotlinandroidpractice.bookJournal.utils.BookTouchListener
import com.example.kotlinandroidpractice.bookJournal.appData.BookRepository
import com.example.kotlinandroidpractice.databinding.MainActivityBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val permissionCode:Int=127;
    private lateinit var binding: MainActivityBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    private val bookRepository= BookRepository()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var bookList = ArrayList<BookClass> ()
    private var actionMode:ActionMode? = null
    private var clickedPosition:Int? = null

    private val actionModeCallback= object: ActionMode.Callback{
        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            p0!!.title="Book Journal"
            p0.subtitle="delete a book item"
            val inflater:MenuInflater= p0.menuInflater
            inflater.inflate(R.menu.menu_actions,p1)
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
return false
        }

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            return when(p1!!.itemId){
                R.id.delete ->{
                    deleteItem(clickedPosition)
                    p0?.finish()
                    true
                }
                else-> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode=null
        }
    }
 private lateinit var viewModel : BookViewModel
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        supportActionBar?.title="Book Journal"



        recyclerView = binding.booksRv
        linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = linearLayoutManager
        bookAdapter = BookAdapter(
            this,
            object :
                BookAdapter.BookRecyclerClickListener {
                override fun onClick(position: Int) {
                    val bookClass = bookList[position]
                    Snackbar.make(
                        binding.mainActivityCoordinator,
                        bookClass.bookName,
                        Snackbar.LENGTH_LONG
                    ).setTextColor(Color.WHITE).show()
                }
            },
            bookList
        )

        recyclerView.addItemDecoration(GreyRvDivider(recyclerView.context))
      /* recyclerView.addItemDecoration(
            RvDividerItemDecoration(
                LinearLayoutManager.VERTICAL,
                this,
                16
            )
        )*/
        recyclerView.adapter = bookAdapter


        addBtn.setOnClickListener {
            if(isPermissionGranted()){
                Intent(applicationContext, AddBook::class.java).also{
                        intent-> startActivity(intent)
                }
            }
        }
        recyclerView.addOnItemTouchListener(
            BookTouchListener(
                this,
                recyclerView,
                object :
                    BookTouchListener.BookInterface {
                    override fun onLongClick(view: View, position: Int) {
                        clickedPosition = position
                        when (actionMode) {
                            null -> {
                                actionMode = startActionMode(actionModeCallback)
                                view.isSelected = true

                            }
                        }
                    }

                    override fun onClick(view: View, position: Int) {
                       /* val bookClass = bookList[position]
                        Snackbar.make(
                            binding.mainActivityCoordinator,
                            bookClass.bookName,
                            Snackbar.LENGTH_LONG
                        ).setTextColor(Color.WHITE).show()*/
                    }
                })
        )
        viewModel = ViewModelProvider(this,
            ViewModelFactory(bookRepository)
        ).get(BookViewModel::class.java)
        viewModel.readBooks(this).observe(this, Observer {
            if (it.size > 0) {
                bookList.clear()
                bookList.addAll(it)
                bookAdapter.notifyDataSetChanged()
            }
        })
    
    }

    private fun deleteItem(clickedPosition: Int?) {
        if (clickedPosition != null) {
            bookAdapter.removeBook(clickedPosition)
            bookAdapter.notifyDataSetChanged()
            viewModel.deleteBook(this,bookList[clickedPosition]).observe(this, Observer {
                if (it){
                    Snackbar.make(binding.mainActivityCoordinator,"Item removed successfully!",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE)
                        .show()
                }else  Snackbar.make(binding.mainActivityCoordinator,"Failed to delete item try again later!",Snackbar.LENGTH_LONG).setTextColor(Color.WHITE)
                    .show()
            })

        }
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode==permissionCode && requestCode== Activity.RESULT_OK){
            binding.addBtn.setOnClickListener{
                Intent(applicationContext, AddBook::class.java).also {
                        intent -> startActivity(intent)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun isPermissionGranted():Boolean{
        return if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            true
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),permissionCode)
            false
        }
    }



}
