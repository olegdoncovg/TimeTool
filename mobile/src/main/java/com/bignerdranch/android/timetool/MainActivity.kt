package com.bignerdranch.android.timetool

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.timetool.ui.theme.TimeToolTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Useful links.
 * Timer example: https://www.geeksforgeeks.org/how-to-create-a-timer-using-jetpack-compose-in-android/
 */

private val TAG: String = MainActivity::class.java.simpleName

class MainActivity : ComponentActivity() {
    private val model: TaskViewModel by lazy { ViewModelProvider(this).get(TaskViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: this=${hashCode()}, model=${model.hashCode()}")
        setContent {
            TimeToolTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Timer({
                            model.onTimerButtonClick(1)
                        }, model.getTimerData(1))
                        Timer({
                            model.onTimerButtonClick(2)
                        }, model.getTimerData(2))
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: this=${hashCode()}, model=${model.hashCode()}")
    }
}

@Composable
private fun Timer(
    onClick: () -> Unit,
    liveTimerData: LiveData<TimerData>
) {
    val timerData: TimerData by liveTimerData.observeAsState(NULL_TIMER_DATA)
    var currentTaskTime by remember {
        mutableStateOf(timerData.startTime)
    }
    LaunchedEffect(key1 = currentTaskTime, key2 = timerData) {
        delay(100L)
        if (timerData === NULL_TIMER_DATA) currentTaskTime = 0;
        else if (timerData.isStarted)
            currentTaskTime = System.currentTimeMillis() - timerData.startTime
//        Log.d(TAG, "LaunchedEffect: isStarted=$timerData.isStarted, currentTaskTime=$currentTaskTime, startTimeSupplier.get()=$timerData.startTime");
    }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Button(
            modifier = Modifier.padding(horizontal = 2.dp),
            onClick = { onClick.invoke() }) { Text(text = if (timerData.isStarted) "Stop" else "Start") }
        Text(
            text = convertLongToTimePeriod(currentTaskTime),
            fontSize = 25.sp,
            fontWeight = FontWeight.Normal,
            color = Companion.Black
        )
    }
}


//////////////// Util func /////////////////////////
fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return df.parse(date).time
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return format.format(date)
}

fun convertLongToTimePeriod(time: Long): String {
    val secTotal = time / 1000L
    val sec = secTotal % 60
    val minTotal = secTotal / 60
    val min = minTotal % 60
    val hoursTotal = minTotal / 60
    return "$hoursTotal:$min $sec"
}
///////////////////////////////////////////////////