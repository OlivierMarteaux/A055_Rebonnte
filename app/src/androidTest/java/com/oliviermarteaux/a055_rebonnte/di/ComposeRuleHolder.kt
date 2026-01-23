package com.oliviermarteaux.a055_rebonnte.di

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import com.oliviermarteaux.a055_rebonnte.MainActivity
import com.oliviermarteaux.a055_rebonnte.RebonnteApplication
import io.cucumber.java.Before
import io.cucumber.junit.WithJunitRule
import org.junit.Rule

@WithJunitRule
class ComposeRuleHolder {

    @Before
    fun setup() {
        val appContext = ApplicationProvider
            .getApplicationContext<RebonnteApplication>()

        appContext.rebonnteContainer = RebonnteTestContainer(appContext)
    }

    // start the Activity / Compose
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()
}