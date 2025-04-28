package com.example.period_app_01

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

import com.example.period_app_01.data.Dates
import com.example.period_app_01.data.DatesDao

// update to use viewModel

@Composable
fun EnterDate(messageDate: String, modifier: Modifier = Modifier, datesDao: DatesDao) {
    //
    val textDate = remember { mutableStateOf(TextFieldValue("")) }
    //
    val keyboardController = LocalSoftwareKeyboardController.current
    //
    val focusRequester = remember { FocusRequester() }
    //
    val coroutineScope = rememberCoroutineScope()
    //
    val lastEntry by datesDao.getLastEntry().collectAsState(initial = null)
    //
    val lastIdEntry by datesDao.getLastId().collectAsState(initial = 0)
    //
    val lastPeriodEntry by datesDao.getLastPeriod().collectAsState(initial = 0)
    //
    val lastDateEntry by datesDao.getLastDate().collectAsState(initial = null)
    //
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            // parameters for text field, more can be added, but some of them break this function
            value = textDate.value,
            onValueChange = { textDate.value = it},
            placeholder = { Text(messageDate) },
            // restricting input to a single line
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                // keyboard on screen only has numbers
                keyboardType = KeyboardType.Number,
                // action performed by keyboard, changes icon accordingly
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                // actions performed when pressing the checkmark on the keyboard
                onDone = {
                    // makes the keyboard disappear
                    keyboardController?.hide()
                    // retrieve the value from the TextField
                    val text = textDate.value.text
                    // conditions (need to be improved in order to protect from invalid inputs)
                    // delete previous date if database is not empty
                    if (text == "00-00-0000" && lastEntry != null) {
                        coroutineScope.launch {
                            datesDao.deleteLast()
                        }
                    // for all other inputs, assuming that they are valid
                    } else if (text != "00-00-0000" && lastEntry == null) {
                        //
                        try {
                            val stringToDate: LocalDate? = LocalDate.parse(text, formatter)
                            val newDate = Dates(
                                id = 0,
                                date = stringToDate,
                                period = 0
                            )
                            //
                            coroutineScope.launch {
                                datesDao.insert(newDate)
                            }
                        } catch(exception: DateTimeParseException) {
                            run {}
                        }
                    } else {
                        //
                        try {
                            val stringToDate: LocalDate? = LocalDate.parse(text, formatter)
                            //
                            val newDate = Dates(
                                id = lastIdEntry + 1,
                                date = stringToDate,
                                // between method for Period does not work if exceeding one month, since days reset to 0
                                period = lastPeriodEntry + ChronoUnit.DAYS.between(lastDateEntry, stringToDate)
                            )
                            //
                            coroutineScope.launch {
                                datesDao.insert(newDate)
                            }
                        } catch (exception: DateTimeParseException) {
                            run {}
                        }
                    }
                    // reset the field
                    textDate.value = TextFieldValue("")
                    //
                    keyboardController?.show()
                    //
                    focusRequester.requestFocus()
                }
            ),
            modifier = Modifier.focusRequester(focusRequester)
        )
        //
        lastEntry?.let {
            Text(text = "Last period date: ${it.date}")
        }

        if (lastIdEntry != 0) {
            /*
            * long value, therefore loses precision which affects accuracy of predictions
            * lastPeriodEntry is sum of all periods, lastIdEntry is total rows in table minus 1
            */
            val averagePeriod = lastPeriodEntry / lastIdEntry
            /*
            * using method from time library to add days to most recent period date
            * converting averagePeriod to integers since method does not take long value
            */
            val nextPeriodDate = lastDateEntry?.plus(Period.ofDays(averagePeriod.toInt()))

            Text(text = "Next period date: $nextPeriodDate")
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}