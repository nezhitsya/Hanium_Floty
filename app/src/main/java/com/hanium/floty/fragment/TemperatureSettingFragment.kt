package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hanium.floty.R

class TemperatureSettingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =  inflater.inflate(R.layout.fragment_temperature_setting, container, false)

        return view
    }

}
