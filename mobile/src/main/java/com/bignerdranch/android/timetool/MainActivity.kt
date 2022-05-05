package com.bignerdranch.android.timetool

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeToolTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(convertDateToLong("2022.05.05 00:00")) {
                        Toast.makeText(
                            this,
                            "OnClick!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

@Composable
private fun Greeting(
    initTime: Long,
    onClick: () -> Unit
) {
    var startTime by remember {
        mutableStateOf(initTime)
    }
    var currentTime by remember {
        mutableStateOf(initTime)
    }
    LaunchedEffect(key1 = currentTime, key2 = startTime) {
        delay(100L)
        currentTime = System.currentTimeMillis() - startTime
    }
    Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = convertLongToTimePeriod(currentTime),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Companion.Black
            )
            Button(onClick = onClick) {
                Text(text = "Work")
            }
            Button(onClick = { startTime = System.currentTimeMillis() }) {
                Text(text = "Rest")
            }
        }
    }
}


//////////////// Util func /////////////////////////
private fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return df.parse(date).time
}

private fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return format.format(date)
}

private fun convertLongToTimePeriod(time: Long): String {
    val secTotal = time / 1000L
    val sec = secTotal % 60
    val minTotal = secTotal / 60
    val min = minTotal % 60
    val hoursTotal = minTotal / 60
    return "$hoursTotal:$min $sec"
}
///////////////////////////////////////////////////