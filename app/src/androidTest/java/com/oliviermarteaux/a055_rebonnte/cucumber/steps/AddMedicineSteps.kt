package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then

class AddMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @Given("I am on the AddOrEditMedicine Screen")
    @Then("I should arrive on the AddOrEditMedicine Screen")
    fun iAmOnThAddOrEditMedicineScreen() {

        // Check that the first events are visible
        composeRule.onNodeWithTag("AddOrEditMedicineScreen").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithTag("AddOrEditMedicineScreen").isDisplayed()
//        }
    }

    @And("I select an aisle in the Aisle picker field")
    fun selectAisle(){

        // Click the item field to open PickerDialog
        composeRule.onNodeWithText("Aisle", useUnmergedTree = true).performClick()
        // Click on "OK" to select the first Aisle item
        composeRule.onNodeWithText("OK", useUnmergedTree = true).performClick()
    }

    @And("I select a quantity in the Stock picker field")
    fun selectStock() {

        // Click the item field to open PickerDialog
        composeRule.onNodeWithText("Stock", useUnmergedTree = true).performClick()

        // Perform scroll action
        repeat(2) {
            composeRule.onNodeWithTag("StockPicker")
                .performTouchInput { swipeUp() }
        }
        composeRule.waitForIdle()

        // Click on "OK" to select the first Aisle item
        composeRule.onNodeWithText("OK", useUnmergedTree = true).performClick()
    }
}