package com.example.kotlinandroidpractice.bookJournal.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinandroidpractice.R

class GreyRvDivider (context:Context): RecyclerView.ItemDecoration() {
    private val mContext:Context
    private val divider:Drawable
    init {
        mContext=context

        divider= ContextCompat.getDrawable(mContext, R.drawable.grey_divider)!!

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left:Int= parent.paddingLeft
        val right= parent.width- parent.paddingRight
        val childCount= parent.childCount
        for (i in 0 until childCount){
            val child=parent.getChildAt(i)
            val params= child.layoutParams as RecyclerView.LayoutParams
            val top= child.bottom + params.bottomMargin
            val bottom= top+ divider.intrinsicHeight
            divider.setBounds(left,top,right,bottom)
            if ((parent.getChildAdapterPosition(child)== parent.adapter?.itemCount!!.minus(1)) &&  parent.bottom>bottom){
                parent.setPadding(parent.paddingLeft,parent.paddingTop,parent.paddingRight,bottom- parent.bottom)
            }
            divider.draw(c)
        }
        super.onDrawOver(c, parent, state)
    }
}