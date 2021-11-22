package com.example.tabatatimer.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.tabatatimer.data.Interval
import com.example.tabatatimer.R
import com.example.tabatatimer.data.WorkoutWithIntervals

class IntervalAdapter(
    listener: OnIntervalChangedListener
) : RecyclerView.Adapter<IntervalAdapter.ViewHolder>() {
    private var intervals : MutableList<Interval>? = null
    interface OnIntervalChangedListener {
        fun onIntervalTimeChanged(interval: Interval, newTime: Int)
        fun onDeleteInterval(interval: Interval)
    }

    private val changedIntervalListener: OnIntervalChangedListener = listener

    class ViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)


    fun setData(intervals: List<Interval>)
    {
        this.intervals = intervals.sortedBy {
            it.index
        }.toMutableList()
        notifyDataSetChanged()
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
        val intervalType = holder.view.findViewById<TextView>(R.id.intervalType)
        val incrementButton = holder.view.findViewById<ImageButton>(R.id.time_increment)
        val decrementButton = holder.view.findViewById<ImageButton>(R.id.time_decrement)

        setCardViewBackgroundColor(
        position,
        holder)

        if (intervals != null) {
            val interval = this.intervals!![position]
            intervalName.text = interval.name
            intervalType.text = interval.type
            intervalTime.text = interval.getIntervalDuration()
            incrementButton.setOnClickListener {
                changedIntervalListener.onIntervalTimeChanged(interval, interval.time!! + 1)
            }

            decrementButton.setOnClickListener {
                changedIntervalListener.onIntervalTimeChanged(interval,interval.time!! - 1 )
            }
        }
    }

    override fun getItemCount(): Int = intervals?. size ?: 0

    fun moveItem(from: Int, to: Int) {
        val temp = intervals!![from]
        intervals?.removeAt(from)
        intervals?.add(to, temp)
    }

    private fun setCardViewBackgroundColor(position: Int, holder: ViewHolder)
    {
        val card = holder.view.findViewById<CardView>(R.id.interval_cardview)

        if (intervals!![position].type == Interval.IntervalType.Work.value)
        {
            card.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.colorAccentRed))
        }
        else if (intervals!![position].type == Interval.IntervalType.Prepare.value)
        {
            card.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.celadonGreen))
        }
        else
        {
            card.setCardBackgroundColor(holder.itemView.getResources().getColor(R.color.colorAccentBlue))
        }
    }

    fun insertItem(at: Int, interval: Interval) {
        if (intervals != null && intervals!!.isEmpty()) {
            intervals?.add(interval)
        } else {
            intervals?.add(at, interval)
        }
        notifyDataSetChanged()
    }

    fun getIntervals(): List<Interval> = this.intervals!!


}