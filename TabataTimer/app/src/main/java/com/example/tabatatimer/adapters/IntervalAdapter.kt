package com.example.tabatatimer.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.data.Interval
import com.example.tabatatimer.R

class IntervalAdapter(
    private var intervals: List<Interval>? = null
) : RecyclerView.Adapter<IntervalAdapter.ViewHolder>() {

    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)


    fun setIntervals(intervals: List<Interval>)
    {
        this.intervals = intervals
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.interval_list_item,
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val intervalName = holder.view.findViewById<TextView>(R.id.intervalName)
        val intervalTime = holder.view.findViewById<TextView>(R.id.intervalTime)


        if (intervals != null) {
            val interval = this.intervals!![position]
            intervalName.text = interval.name
            intervalTime.text = interval.getIntervalDuration()
        }
    }

    override fun getItemCount(): Int = intervals?. size ?: 0




}