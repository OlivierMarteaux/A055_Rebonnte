package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import android.util.Log
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

//_ This class requires picocontainer to inject dependency
class SharedButtonSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule

    //_######################
    //_#  FAB BUTTON
    //_######################
    @When("I click on the {string} FAB button")
    fun iClickOnFabButton(fabLabel: String) {

        // Use contentDescription or tag for your FABs
        composeRule.onNodeWithTag(fabLabel).performClick()
    }
    //_######################
    //_#  BUTTON
    //_######################
    @When("I click on the {string} button")
    fun iClickOnButton(text: String) {
        // Use contentDescription or tag for your FABs
        composeRule.onNodeWithText(text = text, useUnmergedTree = true).performClick()
    }

    @When("I click on the button tagged {string}")
    fun iClickOnButtonTagged(tag: String) {
        // Use contentDescription or tag for your FABs
        Log.d("OM_TAG", "I click on the button tagged $tag")
        composeRule.onNodeWithTag(testTag = tag, useUnmergedTree = true).performClick()
    }

    @Then("I cannot click on the {string} button")
    fun iCannotClickButton(buttonLabel:String){
        composeRule.onNode(
            hasText(buttonLabel) and hasClickAction()
        ).assertIsNotEnabled()
    }

    @When("I click on the {string} icon button")
    fun iClickIconButton(label: String){
        composeRule.onNode(
            hasContentDescription(label, substring = true)
        ).performClick()
    }

    //_######################
    //_#  CLICKABLE CARD
    //_######################
    @When("I click on the {string} card")
    fun iClickOnCard(cardText: String) {
        // Use contentDescription or tag for your FABs
        composeRule.onNodeWithText(cardText).performClick()
    }
}