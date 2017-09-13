package com.example.round.kotlin_networking

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , LoaderManager.LoaderCallbacks<List<Article>>
    , ArticleAdapter.ListItemClickListener{

    private val mArticleLoaderId = 1
    private val mNewsEndPointUrl = "https://newsapi.org/v1/articles"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_articles.layoutManager = LinearLayoutManager(this)
        rv_articles.setHasFixedSize(true)

        runLoaders()
    }

    private fun runLoaders(){

        progress_bar.visibility = View.VISIBLE

        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = connManager.activeNetworkInfo

        if(networkInfo != null && networkInfo.isConnected){
            loaderManager.initLoader(mArticleLoaderId, null,this)
        }else{
            progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this,"No internet connection",Toast.LENGTH_LONG).show()
        }
    }

    override fun onLoadFinished(loader: Loader<List<Article>>?, data: List<Article>?) {

        progress_bar.visibility = View.INVISIBLE

        if(!data!!.isEmpty() && data != null){
            rv_articles.adapter = ArticleAdapter(this,this,data)
        }
    }

    override fun onLoaderReset(p0: Loader<List<Article>>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<List<Article>> {

        val baseUri : Uri = Uri.parse(mNewsEndPointUrl)
        var urlBuilder : Uri.Builder = baseUri.buildUpon()

        urlBuilder.appendQueryParameter("source","ars-technica")
        urlBuilder.appendQueryParameter("apiKey",getString(R.string.news_api_key))
        return object:AsyncTaskLoader<List<Article>>(this){
            override fun onStartLoading() {
                forceLoad()
            }

            override fun loadInBackground(): List<Article>? {
                return QueryUtils.fetchArticleData(urlBuilder.toString())
            }
        }
    }

    override fun onListItemClick(article: Article) {
        Log.i("onListItemClick","********************Gone")

        var articleUrl = Uri.parse(article.mUrl)
        val browerIntent = Intent(Intent.ACTION_VIEW,articleUrl)
        startActivity(browerIntent)
    }

}
