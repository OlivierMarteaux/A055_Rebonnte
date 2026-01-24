package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class HomeSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @Given("I am on the Home screen")
    @Then("I should arrive on the Home Screen")
    fun iAmOnTheHomeScreen() {
        Log.d("OM_TAG", "I should arrive on the Home screen")
        composeRule.waitForIdle()

        // Verify some known aisles are shown
        composeRule.onNodeWithText("Analgesics & Pain Management").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Analgesics & Pain Management").isDisplayed()
//        }
//        Thread.sleep(5_000)
    }

    @Then("All the aisles are displayed and scrollable on the screen")
    fun allTheAislesAreDisplayedAndScrollable() {
        Log.d("OM_TAG", "All the aisles are displayed and scrollable on the screen")

        // Check that the first events are visible
        composeRule.onNodeWithText("Analgesics & Pain Management").assertIsDisplayed()
        composeRule.onNodeWithText("Antibiotics").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Analgesics & Pain Management").isDisplayed()
//            composeRule.onNodeWithText("Antibiotics").isDisplayed()
//        }

        // Perform scroll action to verify it is scrollable
        composeRule.onNode(hasScrollAction()) // ensure scrollable container exists
            .performScrollToIndex(5)           // scroll to a later item, e.g., 5th item

        // Now verify the last event becomes visible
        composeRule.onNodeWithText("Gastrointestinal Medicines").assertIsDisplayed()
    }
}