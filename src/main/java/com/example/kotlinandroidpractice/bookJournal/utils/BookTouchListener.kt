package com.example.kotlinandroidpractice.bookJournal.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BookTouchListener(context:Context,recyclerView: RecyclerView,bookInterface: BookInterface):RecyclerView.OnItemTouchListener {
    private val gestureDetector:GestureDetector
    private val bokIntf : BookInterface = bookInterface

    init {
        gestureDetector= GestureDetector(context,object: GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent?): Boolean {

            return super.onSingleTapUp(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            val child:View?= recyclerView.findChildViewUnder(e!!.x,e.y)
            if (child!=null&& bokIntf!=null){
                bokIntf.onLongClick(child,recyclerView.getChildAdapterPosition(child))
            }
            super.onLongPress(e)
        }
    })

}

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
val child: View? = rv.findChildViewUnder(e.x,e.y)
    if (child!=null && bokIntf!=null && gestureDetector.onTouchEvent(e)){
        bokIntf.onClick(child,rv.getChildAdapterPosition(child))
    }
    return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }
    interface BookInterface{
        fun onLongClick(view: View,position:Int )
        fun onClick(view:View,position: Int)
    }
}