package com.example.round.nationinfo

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var recycleListView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycleListView = findViewById(R.id.nation_list) as RecyclerView
        recycleListView.layoutManager = LinearLayoutManager(this)

        var adapter : NationAdapter = NationAdapter(this, listOf(
                NationData(R.drawable.l_flag_belgium,"벨기에","브뤼셀"),
                NationData(R.drawable.l_flag_argentina,"아르헨티나","부에노스아이레스"),
                NationData(R.drawable.l_flag_brazil,"브라질","브라질리아"),
                NationData(R.drawable.l_flag_canada,"캐나다","오타와"),
                NationData(R.drawable.l_flag_china,"중국","베이징"),
                NationData(R.drawable.l_flag_croatia,"크로아티아","자그레브"),
                NationData(R.drawable.l_flag_czech,"체코","프라하"),
                NationData(R.drawable.l_flag_germany,"독일","베를린"),
                NationData(R.drawable.l_flag_ghana,"가나","아크라"),
                NationData(R.drawable.l_flag_greece,"그리스","아테네"),
                NationData(R.drawable.l_flag_korea,"대한민국","서울"),
                NationData(R.drawable.l_flag_norway,"노르웨이","오슬로"),
                NationData(R.drawable.l_flag_united_states,"미국","워싱턴")
        ))

        adapter.setOnClickListener(this)
        recycleListView.adapter = adapter
    }

    override fun onClick(view: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val textView : TextView = view?.findViewById(R.id.name) as TextView
        val name : String = textView.text.toString()
        val intent : Intent = Intent(this, NationDetailActivity::class.java)

        intent.putExtra(NationDetailActivity.EXTRA_NATION_NAME,name)
        startActivity(intent)
    }
}
