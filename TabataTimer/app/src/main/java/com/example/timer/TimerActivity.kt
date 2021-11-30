package com.example.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.content.res.Resources
import android.provider.Settings
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.navArgs
import com.example.tabatatimer.R
import com.example.tabatatimer.data.Interval
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {

    private val args: TimerActivityArgs by navArgs()
    private var mediaPlayer: MediaPlayer? = null
    private var endOfList = false

    /*
     * Notification builders, intents, and more.
     */
    private val NOTIFICATION_ID = 0
    private lateinit var rewindIntent: Intent
    private lateinit var pauseIntent: Intent
    private lateinit var playIntent: Intent
    private lateinit var fastForwardIntent: Intent
    private lateinit var rewindPendingIntent: PendingIntent
    private lateinit var pausePendingIntent: PendingIntent
    private lateinit var playPendingIntent: PendingIntent
    private lateinit var fastForwardPendingIntent: PendingIntent

    enum class WorkoutActions {
        PAUSE,
        PLAY,
        REWIND,
        FAST_FORWARD
    }

    /** Overrides the onCreate function
     *
     * @param savedInstanceState sets the new view of the layout
     * and assigns listeners for buttons
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timer)


        invalidateOptionsMenu()
        val actionBar = supportActionBar!!
        actionBar.setHomeButtonEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setTitle(R.string.title_activity_timer)

        playButton.setOnClickListener {

            if (playButton.isChecked) {
                playButton.setBackgroundResource(R.drawable.ic_pause_24)
                play(this)
            } else {
                playButton.setBackgroundResource(R.drawable.ic_play_24)
                pause()
            }

        }

        fastForwardButton.setOnClickListener {
            playButton.setBackgroundResource(R.drawable.ic_pause_24)
            forwardBackward(WorkoutActions.FAST_FORWARD, this)
        }

        rewindButton.setOnClickListener {
            endOfList = false
            forwardBackward(WorkoutActions.REWIND, this)
        }

        endWorkoutButton.setOnClickListener {
            intervalCountdownTimer?.cancel()
            mediaPlayer?.release()
            mediaPlayer = null
            notificationManager.cancel(NOTIFICATION_ID)
            finish()
        }

        // Create intents for each button in the notification
        rewindIntent = Intent(this, NotificationButtonReceiver::class.java)
        pauseIntent = Intent(this, NotificationButtonReceiver::class.java)
        playIntent = Intent(this, NotificationButtonReceiver::class.java)
        fastForwardIntent = Intent(this, NotificationButtonReceiver::class.java)

        // Assign an action to each button
        fastForwardIntent.action = getString(R.string.notification_fast_forward_action)
        rewindIntent.action = getString(R.string.notification_rewind_action)
        playIntent.action = "PLAY"
        pauseIntent.action = getString(R.string.notification_pause_action)

        // Create a pending intent for each button, used for receiving a button press
        rewindPendingIntent = PendingIntent.getBroadcast(this, 0, rewindIntent, 0)
        pausePendingIntent = PendingIntent.getBroadcast(this, 1, pauseIntent, 0)
        playPendingIntent = PendingIntent.getBroadcast(this, 2, playIntent, 0)
        fastForwardPendingIntent = PendingIntent.getBroadcast(this, 3, fastForwardIntent, 0)

        // Create the notification manager and builder.
        notificationBuilder = NotificationCompat.Builder(this, getString(R.string.notification_channel))
        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        updateNotification(this)

        // Update the notification with information about the first interval
        notificationBuilder.apply {
            setProgress(100, 0, false)
            setContentTitle(args.workoutList.List.get(0).intervals.get(0).name)
            setContentText(args.workoutList.List.get(0).intervals.get(0).getIntervalDuration())
        }

        // Notify the notification to change
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        // Create a receiver for updating the UI based on notification button presses
        val receiver = ActionReceiver(this)
        val filter = IntentFilter()
        filter.addAction("REWIND")
        filter.addAction("PAUSE")
        filter.addAction("PLAY")
        filter.addAction("FAST FORWARD")
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)

        // Queue the first interval to be displayed
        workoutName.text = args.workoutList.List[0].workout.name
        forwardBackward(WorkoutActions.PAUSE, this)

    }


    override fun onSupportNavigateUp(): Boolean {
        intervalCountdownTimer?.cancel()
        notificationManager.cancel(NOTIFICATION_ID)
        mediaPlayer?.release()
        mediaPlayer = null
        finish()
        return false
    }

    fun resetTimer() {

        // Plays the sound clip once if at the end of the list or if the timer is 0
        if (args.workoutList.List[workoutListIterator].intervals[intervalListIterator].time != null && endOfList == false) {
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
            mediaPlayer?.start()
        }

        if (intervalListIterator != args.workoutList.List[workoutListIterator].intervals.size - 1
            && workoutListIterator != args.workoutList.List.size - 1) {
            forwardBackward(WorkoutActions.FAST_FORWARD, this)
            endOfList = false
        } else {
            endOfList = true
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Workout App"
            val descriptionText = "Notification channel for the workout app."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                getString(R.string.notification_channel),
                name,
                importance
            ).apply {
                description = descriptionText
            }

            channel.setSound(null, null)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {

        var intervalCountdownTimer: CountDownTimer? = null
        var intervalListIterator: Int = 0
        var workoutListIterator: Int = 0
        private var totaltime: Long = 0
        private val countdown: Long = 1000

        private lateinit var notificationBuilder: NotificationCompat.Builder
        private lateinit var notificationManager: NotificationManager

        fun pause() {
            intervalCountdownTimer?.cancel()
        }

        /**
         * Creates a timer and calls the on tick function
         * of the timer.
         */

        fun play(activity: TimerActivity) {

            // Creates a countdown timer object
            intervalCountdownTimer = object : CountDownTimer(totaltime, countdown) {

                /**
                 * Overrides the onTick function of the countdown timer
                 * @param millis the total time of the countdown timer
                 */

                override fun onTick(millis: Long) {
                    totaltime = millis
                    activity.workoutProgressBar.incrementProgressBy(1000)
                    val minutes = (millis / 1000) / 60
                    val seconds = (millis / 1000) % 60
                    val updateStringTimer = String.format("%02d:%02d", minutes, seconds)
                    activity.timerText.text = updateStringTimer
                    updateNotification(activity)
                }

                /**
                 * Overrides the onFinish function to call the resetTimer function
                 */

                override fun onFinish() {
                    activity.resetTimer()
                }
            }
            // Starts the countdown timer
            intervalCountdownTimer?.start()
        }

        fun updateNotification(activity: TimerActivity) {
            notificationBuilder.apply {
                setSmallIcon(R.drawable.ic_stopwatch_24)
                setProgress(totaltime.toInt(), activity.workoutProgressBar.progress, false)
                setContentTitle(activity.workoutName.text)
                setContentText(activity.timerText.text)
                addAction(R.drawable.ic_fast_rewind_24, "Rewind", activity.rewindPendingIntent)
                notificationBuilder.addAction(R.drawable.ic_pause_24, "Play / Pause", activity.pausePendingIntent)
                addAction(R.drawable.ic_fast_forward_24, "Fast Forward", activity.fastForwardPendingIntent)
                setShowWhen(false)
            }

            with(NotificationManagerCompat.from(activity)) {
                notify(activity.NOTIFICATION_ID, notificationBuilder.build())
            }
        }

        fun forwardBackward(nav: WorkoutActions, activity: TimerActivity) {

            val intervalListSize = activity.args.workoutList.List[workoutListIterator].intervals.size
            val workoutListSize = activity.args.workoutList.List.size

            if (workoutListIterator != 0 && intervalListIterator == 0 && nav == WorkoutActions.REWIND) {

                workoutListIterator--
                intervalListIterator = activity.args.workoutList.List[workoutListIterator].intervals.size - 1
                activity.workoutName.text = activity.args.workoutList.List[workoutListIterator].workout.name

            } else if (workoutListIterator != workoutListSize - 1 && intervalListIterator == (intervalListSize - 1) && nav == WorkoutActions.FAST_FORWARD) {
                intervalListIterator = 0
                workoutListIterator++
                activity.workoutName.text = activity.args.workoutList.List[workoutListIterator].workout.name
            } else {
                if(nav == WorkoutActions.FAST_FORWARD) {
                    intervalListIterator++
                }
                else if(nav == WorkoutActions.REWIND){
                    intervalListIterator--
                }
            }

            // Sets the backwards button state
            if (intervalListIterator == 0 && workoutListIterator == 0) {
                activity.rewindButton.setBackgroundResource(R.drawable.ic_fast_rewind_gray)
                activity.rewindButton.isEnabled = false
            } else {
                activity.rewindButton.setBackgroundResource(R.drawable.ic_fast_rewind_24)
                activity.rewindButton.isEnabled = true
            }

            // Sets the forwards button state
            if (intervalListIterator == intervalListSize - 1 && workoutListIterator == workoutListSize - 1) {
                activity.fastForwardButton.setBackgroundResource(R.drawable.ic_fast_forward_gray)
                activity.fastForwardButton.isEnabled = false
            } else {
                activity.fastForwardButton.setBackgroundResource(R.drawable.ic_fast_forward_24)
                activity.fastForwardButton.isEnabled = true
            }

            activity.intervalNameTimer.text = activity.args.workoutList.List[workoutListIterator].intervals[intervalListIterator].type

            // Checks if reps is null
            if (activity.args.workoutList.List[workoutListIterator].intervals[intervalListIterator].reps != null) {
                activity.timerText.text = "Reps: " + activity.args.workoutList.List[workoutListIterator].intervals[intervalListIterator].reps.toString()
                totaltime = 0
            } else {
                var intervalTimeInSeconds: Int?
                intervalTimeInSeconds = activity.args.workoutList.List[workoutListIterator].intervals[intervalListIterator].time

                var minutes = intervalTimeInSeconds!! / 60
                var seconds = intervalTimeInSeconds % 60


                val covertTimeText = String.format("%02d:%02d", minutes, seconds)

                totaltime = (minutes * 60000 + seconds * 1000 + 1000).toLong()

                activity.timerText.text = covertTimeText

                activity.workoutProgressBar.max = totaltime.toInt()
                activity.workoutProgressBar.setProgress(0)
            }

            // Checks if timer is null


            var intervalColorText: Int

            // Checks if no color is selected
            intervalColorText = when (activity.args.workoutList.List[workoutListIterator].intervals[intervalListIterator].type) {
                Interval.IntervalType.Work.value ->
                    activity.resources.getColor(R.color.colorAccentRed)

                Interval.IntervalType.Rest.value ->
                    activity.resources.getColor(R.color.colorAccentBlue)
                else ->
                    activity.resources.getColor(R.color.colorAccentGreen)
            }

            activity.timerText.setTextColor(intervalColorText)
            activity.workoutName.setTextColor(intervalColorText)
            activity.intervalNameTimer.setTextColor(intervalColorText)
            activity.workoutProgressBar.progressTintList = ColorStateList.valueOf(intervalColorText)

            // if hitting the rewind button while the timer is counting down the reset timer will be paused
            if (nav == WorkoutActions.REWIND) {
                pause()
                activity.playButton.setBackgroundResource(R.drawable.ic_play_24)
                activity.workoutProgressBar.setProgress(0)
            }

            // if hitting the forward button while the timer is counting down the timer will move move on to the next one
            if (nav == WorkoutActions.FAST_FORWARD) {
                pause()

                // Checks if the timer is 0
                if (activity.args.workoutList.List[workoutListIterator].intervals[intervalListIterator].time != null) {
                    play(activity)
                    activity.playButton.isChecked = true
                } else {
                    activity.playButton.setBackgroundResource(R.drawable.ic_play_24)
                }

                activity.workoutProgressBar.setProgress(0)
            }

            updateNotification(activity)
        }
    }

}