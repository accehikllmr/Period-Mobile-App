package com.example.period_app_01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
import com.example.period_app_01.ui.theme.Period_app_01Theme
import com.example.period_app_01.data.DatesDatabase
import com.example.period_app_01.data.DatesDao

// ADD COMMENTS TO NEW FUNCTIONALITY
// DEPLOY APP TO MOTOROLA
// SETUP UNIT TESTS
// REFACTOR ENTERDATE.KT (too much going on in single class)

/*
 * main class, extends ComponentActivity class
 * what is ComponentActivity class?
 * what is an Activity?
 */
class MainActivity : ComponentActivity() {
    /*
     * declaration of variable, DatesDao object, implements methods which allow access to data
     * lateinit allows declaration of non-nullable property without performing null check (i.e. == null)
     * why use laneinit in this context?
     */
    private lateinit var datesDao: DatesDao
    /*
     * overrides onCreate method as defined in ComponentActivity class
     * onCreate initializes an activity (e.g. initializing UI, preparing data, etc.)
     * 
     * what is an Activity?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //
        val database = DatesDatabase.getDatabase(applicationContext)
        //
        datesDao = database.datesDao()

        setContent {
            Period_app_01Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EnterDate(
                        messageDate = resources.getString(R.string.enter_date),
                        modifier = Modifier.padding(innerPadding),
                        datesDao = datesDao
                    )
                }
            }
        }
    }
}

/* below is preview, will use when working on more sophisticated UI
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Period_app_01Theme {
        Greeting("Android")
    }
}
*/