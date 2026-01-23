package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.Then

class ProfileSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule

    @Then("I should arrive on the Profile Screen for the current user")
    fun iAmOnTheProfileScreenForCurrentUser() {
        // Check that the first events are visible
        composeRule.onNodeWithTag("Profile Screen").assertIsDisplayed()
        composeRule.onNodeWithText("Fievel Farwest").assertIsDisplayed()
    }
}