package com.example.round.kotlin_networking

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by Round on 2017-09-05.
 */
class ArticleAdapter(val mListItemClickListener : ListItemClickListener, val context : Context,
                     val mAricleData : List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(){

    interface ListItemClickListener{
        fun onListItemClick(article : Article)
    }

    inner class ArticleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var author : TextView = itemView.findViewById(R.id.author) as TextView
        var title: TextView = itemView.findViewById(R.id.title) as TextView
        var image : ImageView = itemView.findViewById(R.id.image) as ImageView
        var desc : TextView = itemView.findViewById(R.id.desc) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListItemClickListener.onListItemClick(mAricleData[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ArticleViewHolder {
        var inflater = LayoutInflater.from(parent!!.context)

        return ArticleViewHolder(inflater.inflate(R.layout.list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder?, position: Int) {
        holder!!.author.text = mAricleData[position].author
        holder!!.title.text = mAricleData[position].mTitle
        holder!!.desc.text = mAricleData[position].desc

        Picasso.with(context)
                .load(mAricleData[position].imageUrl)
                .into(holder.image);

    }

    override fun getItemCount(): Int {
        return mAricleData.size
    }
}