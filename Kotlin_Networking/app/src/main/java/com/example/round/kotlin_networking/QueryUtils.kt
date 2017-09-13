package com.example.round.kotlin_networking

import android.app.DownloadManager
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * Created by Round on 2017-09-05.
 */
object QueryUtils{

    private val logTag = QueryUtils::class.java.simpleName

    fun fetchArticleData(requestUrl: String):List<Article>?{

        var url = createUrl(requestUrl)

        var jsonResponse : String? = null

        try{
            jsonResponse = getResponseFromHttpUrl(url)
        }catch (e: IOException){
            Log.i(logTag,"Problem making the HTTP request "+e.toString())
        }

        return extractFeatureFromJson(jsonResponse)
    }

    private fun extractFeatureFromJson(jsonResponse : String?):List<Article>?{

        if(jsonResponse == null || jsonResponse.isEmpty()){
            return null
        }

        var articles = mutableListOf<Article>()

        try{
            val baseJsonResponse = JSONObject(jsonResponse)

            val articleArray = baseJsonResponse.getJSONArray("articles")

            for(i in 0..articleArray.length()){
                val currentArticle = articleArray.getJSONObject(i)

                var author = if(currentArticle.has("author")) currentArticle.getString("author") else "null"

                val title = if(currentArticle.has("title")) currentArticle.getString("title") else "null"

                var description = if(currentArticle.has("description")) currentArticle.getString("description") else "null"

                var url = if(currentArticle.has("url")) currentArticle.getString("url") else "null"

                var urlToImage = if(currentArticle.has("urlToImage")) currentArticle.getString("urlToImage") else "null"

                articles.add(Article(author,title,description,url,urlToImage))
            }
        }catch (e: JSONException){
            e.stackTrace
        }

        return articles
    }

    @Throws(IOException :: class)
    private fun getResponseFromHttpUrl(url: URL?): String? {
        val urlConnection = url!!.openConnection() as HttpURLConnection

        try{
            if(urlConnection.responseCode == HttpURLConnection.HTTP_OK){
                val inputStream = urlConnection.inputStream

                val scanner = Scanner(inputStream)
                scanner.useDelimiter("\\A")

                if(scanner.hasNext()){
                    return scanner.next()
                }
            }else{
                Log.i("QueryUtills","*****************************"+urlConnection!!.responseMessage)
            }
        }finally {
            urlConnection.disconnect()
        }

        return null
    }

    private fun createUrl(requestUrl: String): URL? {
        var url : URL? = null

        try{
            url = URL(requestUrl)
        }catch (e: MalformedURLException){
            e.stackTrace
        }

        return url
    }
}