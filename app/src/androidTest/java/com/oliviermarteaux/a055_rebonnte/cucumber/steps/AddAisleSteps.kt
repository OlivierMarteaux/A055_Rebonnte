package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
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
        composeRule.onNodeWithTag("AddAisleScreen").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithTag("AddAisleScreen").isDisplayed()
//        }
    }
}