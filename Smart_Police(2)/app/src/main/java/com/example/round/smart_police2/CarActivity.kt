package com.example.round.smart_police2

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.round.smart_police2.Common.CurrentDate
import com.example.round.smart_police2.Common.CurrentLocation
import com.example.round.smart_police2.Common.WebViewActivity
import com.example.round.smart_police2.Data.Crime
import com.example.round.smart_police2.Data.DataList
import com.example.round.smart_police2.Data.Person
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Round on 2017-09-19.
 */
class CarActivity: AppCompatActivity() {

    private val context: Context = this

    private lateinit var currentLocation : CurrentLocation
    private lateinit var mSectionPagerAdapter : SectionsPagerAdapter
    private lateinit var mViewPager : ViewPager

    //CarActivity Main Layout View
    private lateinit var date : TextView
    private lateinit var where : TextView
    private lateinit var confirm : Button

    private val personInfo : PersonInfo = PersonInfo()
    private val crimeInfo : CrimeInfo = CrimeInfo()

    companion object {

        private val currentDate : CurrentDate = CurrentDate()
        val dataList : DataList = DataList()

        //PersonInfo Layout View
        lateinit var personNo1 : EditText
        lateinit var personNo2 : EditText
        lateinit var personName : EditText
        lateinit var personAddress : TextView
        lateinit var personDetaliAddress : EditText
        lateinit var searchPostCode : ImageButton
        lateinit var phoneNumber1 : Spinner
        lateinit var phoneNumber2 : EditText
        lateinit var phoneNumber3 : EditText
        lateinit var license1 : EditText
        lateinit var license2 : EditText
        lateinit var license3 : EditText
        lateinit var carNo : EditText

        //Crime Info
        private lateinit var firstDate : TextView
        private lateinit var secondDate : TextView
        private lateinit var searchDateButton1 : ImageButton
        private lateinit var searchDateButton2 : ImageButton
        private lateinit var rule : Spinner
        private lateinit var crimAction : EditText
        private lateinit var firstCost : TextView
        private lateinit var secondCost : TextView
        private lateinit var calButton1 : ImageButton
        private lateinit var calButton2 : ImageButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_paper)

        mSectionPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager.adapter = mSectionPagerAdapter

        //CarActivity Main Layout View
        date = findViewById(R.id.date) as TextView
        where = findViewById(R.id.where) as TextView
        confirm = findViewById(R.id.confirm) as Button

        currentLocation = CurrentLocation(where,this)
        currentLocation.getCurrentLocation()
        currentDate.getDate(date)

        confirm.setOnClickListener{
            var citizenNo : ArrayList<Int> = ArrayList()
            citizenNo.add(0, personNo1.text.toString().toInt())
            citizenNo.add(1, personNo2.text.toString().toInt())

            var phoneNumber : ArrayList<Int> = ArrayList()
            phoneNumber.add(0, phoneNumber1.selectedItem.toString().toInt())
            phoneNumber.add(1, phoneNumber2.text.toString().toInt())
            phoneNumber.add(2, phoneNumber3.text.toString().toInt())

            var licenseNumber : ArrayList<Int> = ArrayList()
            licenseNumber.add(0, license1.text.toString().toInt())
            licenseNumber.add(1, license2.text.toString().toInt())
            licenseNumber.add(2, license3.text.toString().toInt())

            var person : Person = Person(citizenNo,personName.text.toString(), personAddress.text.toString(), personDetaliAddress.text.toString(),phoneNumber,licenseNumber,carNo.text.toString())

            var id : Int = 0
            //후에 id 구별
            var crime : Crime = Crime(0,person)
            crime.setLocation(where.text.toString())
            crime.setDate(date.text.toString(), currentDate.firstYear, currentDate.firstMonth, currentDate.firstDay, currentDate.secondYear, currentDate.secondMonth, currentDate.secondDay)
            crime.setCost(firstCost.text.toString().toInt(), secondCost.text.toString().toInt())
            crime.setCrime(rule.selectedItem.toString(), crimAction.text.toString())


            if(dataList.isCrime(id)) dataList.update(id, crime)
            else    dataList.addCrime(crime)

            var intent : Intent = Intent(context, CarPaperConfirm::class.java)
            intent.putExtra("id",crime.id)
            startActivity(intent)
        }
    }

    inner class SectionsPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){

        override fun getItem(position: Int): Fragment {
            if(position == 0) return personInfo
            else return crimeInfo
        }

        override fun getCount(): Int{ return 2 }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

    class PersonInfo : Fragment(){

        //Search Address
        private val SEARCH_ADDRESS_ACTIVITY : Int = 10000

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            var view : View = inflater!!.inflate(R.layout.car_paper_first,container,false);

            personNo1 = view.findViewById(R.id.personNo_n) as EditText
            personNo2 = view.findViewById(R.id.personNo_t) as EditText
            personName = view.findViewById(R.id.personName) as EditText
            personAddress = view.findViewById(R.id.personAddress) as TextView
            personDetaliAddress = view.findViewById(R.id.personDetailAddress) as EditText
            searchPostCode = view.findViewById(R.id.addressSearch) as ImageButton
            phoneNumber1 = view.findViewById(R.id.phone_n) as Spinner
            phoneNumber2 = view.findViewById(R.id.phone_t) as EditText
            phoneNumber3 = view.findViewById(R.id.phone_h) as EditText
            license1 = view.findViewById(R.id.license_n) as EditText
            license2 = view.findViewById(R.id.license_t) as EditText
            license3 = view.findViewById(R.id.license_h) as EditText
            carNo = view.findViewById(R.id.carNo) as EditText

            setPhoneSpinner()

            searchPostCode.setOnClickListener{
                var intent : Intent? = Intent(context, WebViewActivity::class.java)
                startActivityForResult(intent!!, SEARCH_ADDRESS_ACTIVITY)
            }

            return view
        }


        private fun setPhoneSpinner() {

            var str = resources.getStringArray(R.array.phone)
            var adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, str)
            phoneNumber1.adapter = adapter
            phoneNumber1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { crimAction.requestFocus() }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            Log.i("processDATA",requestCode.toString())
            when(requestCode){

                SEARCH_ADDRESS_ACTIVITY->{
                    if(resultCode == RESULT_OK){
                        var data : String = data!!.getStringExtra("data")

                        Log.i("processDATA",resultCode.toString())
                        if(data!=null) personAddress.setText(data)
                    }
                }
            }
        }

    }

    class CrimeInfo : Fragment(), View.OnClickListener {

        private val DIALOG_ID : Int = 0

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            var view: View = inflater!!.inflate(R.layout.car_paper_second, container, false);

            firstDate = view.findViewById(R.id.firstDate) as TextView
            secondDate = view.findViewById(R.id.secondDate) as TextView
            searchDateButton1 = view.findViewById(R.id.pickDate_n) as ImageButton
            searchDateButton2 = view.findViewById(R.id.pickDate_t) as ImageButton
            rule = view.findViewById(R.id.rule) as Spinner
            crimAction = view.findViewById(R.id.crimeCase) as EditText
            firstCost = view.findViewById(R.id.firstCost) as TextView
            secondCost = view.findViewById(R.id.secondCost) as TextView
            calButton1 = view.findViewById(R.id.pickDate_n) as ImageButton
            calButton2 = view.findViewById(R.id.pickDate_t) as ImageButton

            currentDate.setFirstDate(firstDate)
            currentDate.setSecondDate(secondDate)

            calButton1.setOnClickListener(this)
            calButton2.setOnClickListener(this)

            //적용법조 & 범칙금(1차) & 범칙금(2차) 임시로 setting : 자치경찰단 측에서 정보 받아오면 setting
            setRuleSpinner()
            firstCost.setText("30000")
            secondCost.setText("36000")

            return view
        }

        private fun setRuleSpinner() {

            var str = arrayOf("도로교통법 제 1조 제 10항")
            var adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, str)
            rule.adapter = adapter
            rule.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            }
        }

        override fun onClick(p0: View?) {

        }
    }
}