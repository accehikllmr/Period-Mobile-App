package com.example.period_app_01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.period_app_01.ui.theme.Period_app_01Theme
import com.example.period_app_01.data.DatesDatabase
import com.example.period_app_01.data.DatesDao

// PRUNE
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
     * will be passed to EnterDate method below
     * why use laneinit in this context?
     */
    private lateinit var datesDao: DatesDao
    /*
     * overrides onCreate method as defined in ComponentActivity class
     * onCreate initializes an activity (e.g. initializing UI, preparing data, etc.)
     * savedInstanceState parameter does nothing here, UI is primitive and data is in database
     * however, removing the argument causes issues with calling onCreate function, hence it stays
     * what is an Activity?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // within the overriding method, we call the method from the parent class
        super.onCreate(savedInstanceState)
        // hides upper bar with device statuses (e.g. wifi, battery, etc.)
        enableEdgeToEdge()
        // declaring and initializing a value which stores a new database instance
        val database = DatesDatabase.getDatabase(applicationContext)
        /*
         * initializing previously declared variable using above database instance
         * calling method which establishes link between database and DAO
         * variable needed to pass a argument to EnterDate method
         */
        datesDao = database.datesDao()

        /*
         * setContent defines the UI for an activity
         * seems standard across all applications built thus far
         * what is an Activity?
         */
        setContent {
            // provides a coherent theme to the entire application, given resources (eg. colour, type)
            Period_app_01Theme {
                /*
                 * Scaffold provides basic layout structure for screen
                 * not necessary, in fact useless, with this primitive user interface
                 */
                EnterDate(
                    messageDate = resources.getString(R.string.enter_date),
                    datesDao = datesDao
                )
            }
        }
    }
}