package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class AisleDetailSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @Given("I am on the AisleDetail screen")
    @Then("I should arrive on the AisleDetail Screen")
    fun iAmOnTheAisleDetailScreen() {
        Log.d("OM_TAG", "I should arrive on the AisleDetail screen")

        composeRule.waitForIdle()
        composeRule.onNodeWithTag("AisleDetailScreen").assertIsDisplayed()
    }
    @And("I can see and scroll the contained medicines")
    fun iCanSeeAndScrollAisleMedicines() {
        Log.d("OM_TAG", "I can see and scroll the contained medicines")

        composeRule.onNodeWithText("Paracetamol").assertIsDisplayed()
        composeRule.onNodeWithText("Ibuprofen").assertIsDisplayed()
        composeRule.onNodeWithText("Aspirin").assertIsDisplayed()

        composeRule.onNode(hasScrollAction()).performScrollToIndex(5)
        composeRule.onNodeWithText("Lisinopril").assertIsDisplayed()
    }
}