package com.bignerdranch.android.timetool

import android.util.Log
import androidx.lifecycle.ViewModel


private val TAG: String = TaskViewModel::class.java.simpleName
class TaskViewModel : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared")
    }
}