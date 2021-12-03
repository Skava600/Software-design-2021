package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tabatatimer.R
import kotlinx.android.synthetic.main.activity_timer.*


class NotificationButtonReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionIntent = Intent("notification.action.intent")
        actionIntent.action = intent?.action
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(actionIntent);
    }
}

class ActionReceiver(activity: TimerActivity) : BroadcastReceiver() {

    private var activity: TimerActivity? = activity

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        if (action == "REWIND") {
            TimerActivity.forwardBackward(TimerActivity.WorkoutActions.REWIND, activity!!)
            activity!!.playButton.isChecked = false
        } else if (action == "PAUSE") {

            val isChecked = activity!!.playButton.isChecked

            if(isChecked) {
                TimerActivity.pause()
                activity!!.playButton.isChecked = !isChecked
                activity!!.playButton.setBackgroundResource(R.drawable.ic_play_24)
            } else {
                TimerActivity.play(activity!!)
                activity!!.playButton.isChecked = !isChecked
                activity!!.playButton.setBackgroundResource(R.drawable.ic_pause_24)
            }
        }
        else if (action == "FAST FORWARD") {
            TimerActivity.forwardBackward(TimerActivity.WorkoutActions.FAST_FORWARD, activity!!)
            activity!!.playButton.isChecked = false
        }

        TimerActivity.updateNotification(activity!!)
    }
}