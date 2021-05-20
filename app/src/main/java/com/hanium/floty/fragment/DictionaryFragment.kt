package com.hanium.floty.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.hanium.floty.R
import com.hanium.floty.adapter.DictionaryAdapter
import com.hanium.floty.adapter.RetrofitInterface
import com.hanium.floty.model.Plant
import com.hanium.floty.model.PlantInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DictionaryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_dictionary, container, false)

//        var searchView: SearchView = view.findViewById(R.id.search_view)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                // 검색 버튼 누를 때 호출
//
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//
//                // 검색 창에서 글자 변경이 일어날 때마다 호출
//
//                return true
//            }
//        })

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        var linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        loadData()

        return view
    }

    private fun setAdapter(plantList: ArrayList<PlantInfo>){
        val mAdapter = DictionaryAdapter(context!!, plantList)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.nongsaro.go.kr/service/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val retrofitService = retrofit.create(RetrofitInterface::class.java)
        retrofitService.requestAllData().enqueue(object : Callback<Plant> {
            override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        setAdapter(it.plants)
                    }
                }
            }

            override fun onFailure(call: Call<Plant>, t: Throwable) {
                Log.d("error at load data", t.message)
            }
        })
    }

}