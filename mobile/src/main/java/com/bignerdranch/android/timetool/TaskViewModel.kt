package com.bignerdranch.android.timetool

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val TAG: String = TaskViewModel::class.java.simpleName
val NULL_TIMER_DATA = TimerData(startTime = Long.MAX_VALUE)

class TaskViewModel : ViewModel() {

    private val timerDataMutable: MutableMap<Int, MutableLiveData<TimerData>> = hashMapOf()

    init {
        onTimerButtonClick(2)
        Handler(Looper.getMainLooper()).postDelayed({
            onTimerButtonClick(2)
            onTimerButtonClick(1)
        }, 3000)
        Handler(Looper.getMainLooper()).postDelayed({
            onTimerButtonClick(2)
        }, 6000)
        Handler(Looper.getMainLooper()).postDelayed({
            onTimerButtonClick(2)
        }, 9000)
    }

    fun onTimerButtonClick(timerId: Int) {
        val it = getTimerDataMutable(timerId)
        it.value =
            if (it.value === NULL_TIMER_DATA) TimerData(System.currentTimeMillis(), true)
            else it.value?.copy(isStarted = !it.value!!.isStarted) ?: NULL_TIMER_DATA
    }

    override fun onCleared() {
        super.onCleared()
        timerDataMutable.clear()
        Log.e(TAG, "onCleared: this=${hashCode()}")
    }

    private fun getTimerDataMutable(timerId: Int): MutableLiveData<TimerData> {
        return timerDataMutable.getOrPut(timerId, { MutableLiveData(NULL_TIMER_DATA) })
    }

    fun getTimerData(timerId: Int): LiveData<TimerData> {
        return getTimerDataMutable(timerId)
    }
}

data class TimerData(val startTime: Long = 0, val isStarted: Boolean = false)

//interface TimerGroupInterface {
//    fun isStarted(): Boolean
//    fun getStartTime(): Long
//    fun onStart(): Long
//    fun onStop(): Long
//}
//
//private data class TimerGroup(
//    val startTime: ArrayList<Long> = arrayListOf(),
//    val stopTime: ArrayList<Long> = arrayListOf()
//) : TimerGroupInterface {
//    //convertDateToLong("2022.05.08 00:00")
//
//    override fun isStarted(): Boolean = stopTime.size < startTime.size
//
//    override fun getStartTime(): Long =
//        if (startTime.size > 0) startTime[startTime.size - 1] else System.currentTimeMillis()
//
//    override fun onStart(): Long {
//        if (isStarted()) onStop()
//        val timeNow = System.currentTimeMillis()
//        startTime.add(timeNow)
//        assert(stopTime.size == startTime.size - 1)
//        return timeNow
//    }
//
//    override fun onStop(): Long {
//        val timeNow = System.currentTimeMillis()
//        stopTime.add(timeNow)
//        assert(stopTime.size == startTime.size)
//        return timeNow
//    }
//}
//
//private object NullTimerGroup : TimerGroupInterface {
//    override fun isStarted(): Boolean {
//        TODO("Not yet implemented")
//    }
//
//    override fun getStartTime(): Long {
//        TODO("Not yet implemented")
//    }
//
//    override fun onStart(): Long {
//        TODO("Not yet implemented")
//    }
//
//    override fun onStop(): Long {
//        TODO("Not yet implemented")
//    }
//}