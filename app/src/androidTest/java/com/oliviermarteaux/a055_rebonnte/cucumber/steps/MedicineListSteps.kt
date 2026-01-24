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

class MedicineListSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @Given("I am on the MedicineList screen")
    @Then("I should arrive on the MedicineList Screen")
    fun iAmOnTheMedicineListScreen() {
        Log.d("OM_TAG", "I should arrive on the MedicineList screen")

        // Verify some known medicines are shown
        composeRule.onNodeWithText("Ibuprofen").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Ibuprofen").isDisplayed()
//        }
//        Thread.sleep(5_000)
    }

    @Then("All the medicines are displayed and scrollable on the screen")
    fun allTheMedicinesAreDisplayedAndScrollable() {
        Log.d("OM_TAG", "All the medicines are displayed and scrollable on the screen")

        // Check that the first events are visible
        composeRule.onNodeWithText("Paracetamol").assertIsDisplayed()
        composeRule.onNodeWithText("Ibuprofen").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Paracetamol").isDisplayed()
//            composeRule.onNodeWithText("Ibuprofen").isDisplayed()
//        }

        // Perform scroll action to verify it is scrollable
        composeRule.onNode(hasScrollAction()) // ensure scrollable container exists
            .performScrollToIndex(5)           // scroll to a later item, e.g., 5th item

//        Thread.sleep(5_000)

        // Now verify the last event becomes visible
        composeRule.onNodeWithText("Lisinopril").assertIsDisplayed()
    }
}