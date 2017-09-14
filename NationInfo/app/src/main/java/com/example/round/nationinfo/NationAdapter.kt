package com.example.round.nationinfo

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater

/**
 * Created by Round on 2017-09-14.
 */

data class NationData(
    //이미지 경로
    var resId : Int,
    //Nation Name
    var name : String,
    //Nation Capital
    var capital : String
)

class ViewHodler(view: View) : RecyclerView.ViewHolder(view){
    val img_flag : ImageView = view.findViewById(R.id.img_flag) as ImageView
    val txt_name : TextView = view.findViewById(R.id.name) as TextView
    val txt_capital : TextView = view.findViewById(R.id.capital) as TextView
}

class NationAdapter(val context: Context, val items : List<NationData>) : RecyclerView.Adapter<ViewHodler>(){

    private var onItemClick : View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHodler {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val mInflater = LayoutInflater.from(context)

        val mainView : View = mInflater.inflate(R.layout.layout_nation_list_item,parent,false)
        mainView.setOnClickListener(onItemClick)

        return ViewHodler(mainView)
    }

    override fun onBindViewHolder(holder: ViewHodler?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder!!.img_flag.setImageResource(items[position].resId)
        holder!!.txt_name.text = items[position].name
        holder!!.txt_capital.text = items[position].capital
        holder!!.txt_name.tag = position
    }

    override fun getItemCount(): Int = items.size

    fun setOnClickListener(l : View.OnClickListener){
        onItemClick = l
    }
}