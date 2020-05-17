package com.example.kotlinandroidpractice.bookJournal.adapter

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinandroidpractice.R
import com.example.kotlinandroidpractice.bookJournal.ui.model.BookClass
import com.example.kotlinandroidpractice.databinding.LayoutBookBinding
import com.example.kotlinandroidpractice.databinding.NothingYetBinding
import java.lang.ref.WeakReference

class BookAdapter(val context: Context, private val lst: BookRecyclerClickListener, private var bookList:List<BookClass>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
     if (holder is BookHolder){
         holder.bindTo(bookList[position])
     }else if (holder is EmptyView && getItemViewType(position)==emptyView){
         holder.bindEmptyView()
     }
    }

    private val emptyView:Int= 1
    override fun getItemCount(): Int {
     return   bookList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context.applicationContext)
        if (viewType == emptyView) {
            val emptyViewBinding: NothingYetBinding =
                NothingYetBinding.inflate(layoutInflater, parent, false)
            return EmptyView(emptyViewBinding)
        } else {
            val bookLayoutBinding: LayoutBookBinding = LayoutBookBinding.inflate(layoutInflater, parent, false)
             return BookHolder(context,bookLayoutBinding,lst)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (bookList.isEmpty()){
            return emptyView
        }
        return super.getItemViewType(position)
    }
    class EmptyView(binding:NothingYetBinding):RecyclerView.ViewHolder(binding.root){
             private var bind:NothingYetBinding= binding

        fun bindEmptyView() {
            bind.nothingYet.text = "No Notes Yet!"

        }
    }
    class BookHolder(val context: Context, binding:LayoutBookBinding,  listener: BookRecyclerClickListener) : RecyclerView.ViewHolder(binding.root),View.OnClickListener{
    private val weakReference:WeakReference<BookRecyclerClickListener> = WeakReference(listener)
        private  var bind: LayoutBookBinding = binding

        override fun onClick(p0: View?) {
            weakReference.get()?.onClick(adapterPosition)
        }
        init{

            bind.root.setOnClickListener(this)
        }
        fun bindTo(bookClass: BookClass){
            bind.dot.text=Html.fromHtml("&#8226")
            bind.dot.setTextColor(getRandomColor())
            bind.bookauthor.text=bookClass.bookAuthor
            bind.bookname.text=bookClass.bookName
            bind.dateadded.text=bookClass.dateAdded
        }

          private fun  getRandomColor():Int{
            var returnColor= Color.GRAY
            val arrayId= context.resources.getIdentifier("mdcolor_400",
                "array",context.packageName)
            if (arrayId!=0){
                val typedArray= context.resources.obtainTypedArray(arrayId)
                val index:Int= ((Math.random() * typedArray.length()).toInt())
                returnColor= typedArray.getColor(index,Color.GRAY)
                typedArray.recycle()
            }
            return returnColor
        }

    }
    fun removeBook(bookPosition:Int){
        val tempList= bookList.toMutableList()
        tempList.removeAt(bookPosition)
        bookList= tempList
        notifyItemRemoved(bookPosition)
        notifyDataSetChanged()
    }
    interface BookRecyclerClickListener{
        fun onClick(position: Int)

    }


}