package com.example.fundo.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.fundo.R
import com.example.fundo.model.NoteNotificationReceiver
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class NoteReminderFragment : DialogFragment() {

    private lateinit var tvChooseDate: TextView
    private lateinit var tvChooseTime: TextView

    private lateinit var datePicker: MaterialDatePicker<Long>
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var btnSetReminder: ToggleButton
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    private lateinit var date: Date

    private val calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_reminder, container, false)

        tvChooseDate = view.findViewById(R.id.tvChooseDate)
        tvChooseDate.setOnClickListener {

            datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            datePicker.show(requireActivity().supportFragmentManager, "Date Picker")

            datePicker.addOnPositiveButtonClickListener {
                calendar.timeInMillis = it
                date = Date(it)
                val dateString =
                    android.text.format.DateFormat.format("dd/MM/yyyy", date).toString()
                tvChooseDate.text = "Date: $dateString"
            }
        }

        tvChooseTime = view.findViewById(R.id.tvChooseTime)
        tvChooseTime.setOnClickListener {
            timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time").build()

            timePicker.show(childFragmentManager, "Time Picker")

            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute
                tvChooseTime.text = "Time: $hour:$minute"
            }
        }
        alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        btnSetReminder = view.findViewById(R.id.btnSetReminder)

        createNotificationChannel()
        btnSetReminder.setOnClickListener {
            if ((it as ToggleButton).isChecked) {
                setAlarm()
            }
            else {
                alarmManager.cancel(pendingIntent)
                Toast.makeText(context, "Alarm off", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "FunDo App"
            val description = "FunDo makes easy note creation."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("FunDo", name, importance)
            channel.description = description
            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun setAlarm() {

        Toast.makeText(context, "Alarm on", Toast.LENGTH_SHORT).show()
        calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
        calendar[Calendar.MINUTE] = timePicker.minute

        val intent = Intent(context, NoteNotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

}