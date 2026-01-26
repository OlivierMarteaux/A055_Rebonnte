package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import android.util.Log
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.text.input.ImeAction
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.Then

class SearchMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @And("I click the Search button in the keyboard")
    fun iClickSearchInKeyboard() {
        composeRule
            .onNode(hasImeAction(ImeAction.Search))
            .performImeAction()
    }

    @Then("I should see only the searched medicine in the medicine list")
    fun onlySearchedMedicineIsDisplayed() {
        Log.d("OM_TAG", "I should see only the searched medicine in the medicine list")

        // Check that the first events are visible
        composeRule.onNodeWithText("Paracetamol").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Paracetamol").isDisplayed()
//        }
        composeRule
            .onAllNodes(hasTestTag("MedicineItem"))
            .assertCountEquals(1)

        //reset the list after search
        composeRule.onNode(
            hasContentDescription("Search", substring = true)
        ).performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("SearchBarClearIcon").performClick()
        composeRule.waitForIdle()
    }
}