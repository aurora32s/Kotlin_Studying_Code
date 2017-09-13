package com.example.round.kotlin_tutorial3

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import android.widget.ImageButton
import android.widget.ProgressBar
import android.view.ViewGroup





class GameActivity : AppCompatActivity() {

    private var myNum : ArrayList<Int> = ArrayList()
    private var mArray : ArrayList<ScoreInfo> = ArrayList()
    private var strike: Int = 0
    private var ball: Int =0
    private var mAdapter: ScoreAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val intent: Intent = intent

        myNum.add(intent.getIntExtra("num1",0))
        myNum.add(intent.getIntExtra("num2",0))
        myNum.add(intent.getIntExtra("num3",0))

        myNumber.setText("${myNum[0]} ${myNum[1]} ${myNum[2]}")

        mAdapter = ScoreAdapter(this, R.layout.number_list_item)
        numberList.adapter = mAdapter


        click.setOnClickListener{

            var number : String = num.text.toString()

            var num:ArrayList<Int> = ArrayList()

            if(number.length == 3) {
                num.add(number[0].toString().toInt())
                num.add(number[1].toString().toInt())
                num.add(number[2].toString().toInt())

                for(x in 0..myNum.size-1){
                    for(y in 0..num.size - 1){
                        Log.i("Strike","${myNum[x]} / ${num[y]} / $x / $y")
                        when(myNum[x]){
                            num[y] -> if(x == y) strike++ else ball++
                        }
                    }
                }

                addScore(num)
            }
            else{
                Toast.makeText(this,"3 자리 숫자를 입력해주세요.",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addScore(number : ArrayList<Int>):Unit{
        mArray.add(ScoreInfo("${number[0]} ${number[1]} ${number[2]}",strike,ball))
        strike = 0
        ball = 0

        num.setText("")
        
        mAdapter!!.notifyDataSetChanged()
    }

    inner class ScoreInfo (var number : String, var strike : Int, var ball : Int){

        private var score : String? = null

        init {
            when{
                strike == 3 -> score="3 Strike!!! HomeRun 입니다!!!"
                strike == 0 && ball == 0 -> score=" Out 입니다!!!"
                else -> score="$strike Strike $ball Ball 입니다!!!"
            }
        }

        public fun getScore() : String? {return score }
    }

    // 플라워창 리스트 포멧
    internal class ScoreViewHolder {
        var number: TextView? = null
        var score: TextView? = null
    }

    // 플라워 리스트 어뎁터
    inner class ScoreAdapter(context: Context, resource: Int) : ArrayAdapter<ScoreInfo>(context, resource) {
        private var mInflater: LayoutInflater? = null

        init {
            mInflater = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return mArray.size
        }

        override fun getView(position: Int, v: View?, parent: ViewGroup): View? {
            val viewHolder : ScoreViewHolder

            var view : View? = v

            if(v == null){
                view = mInflater!!.inflate(R.layout.number_list_item,parent,false)

                viewHolder = ScoreViewHolder()
                viewHolder.number = view.findViewById(R.id.number) as TextView
                viewHolder.score = view.findViewById(R.id.score) as TextView

                view.setTag(viewHolder)
            }else{
                viewHolder = view!!.getTag() as ScoreViewHolder
            }

            var info : ScoreInfo = mArray.get(position)
            if(info != null){
                viewHolder.number!!.setText(info.number)
                viewHolder.score!!.setText(info.getScore())
            }

            return view
        }

    }
}
