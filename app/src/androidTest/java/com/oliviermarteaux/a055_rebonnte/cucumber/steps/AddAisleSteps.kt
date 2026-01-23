package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class AddAisleSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @Given("I am on the AddAisle Screen")
    @Then("I should arrive on the AddAisle Screen")
    fun iAmOnTheAddAisleScreen() {

        // Check that the first events are visible
        composeRule.waitUntil(timeout) {
            composeRule.onNodeWithTag("AddAisleScreen").isDisplayed()
        }
    }

    @Then("I cannot click on the {string} button")
    fun iCannotClickButton(buttonLabel:String){

        composeRule.onNodeWithText(text = buttonLabel, useUnmergedTree = true).performClick()

        // Assert that user does not go back to home screen
        composeRule.onNodeWithTag("AddAisleScreen").isDisplayed()
        composeRule.onNodeWithTag("Analgesics & Pain Management").assertIsNotDisplayed()
    }
}