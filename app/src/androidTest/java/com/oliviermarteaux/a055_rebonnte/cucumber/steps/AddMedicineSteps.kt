package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class AddMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @Given("I am on the AddOrEditMedicine Screen")
    @Then("I should arrive on the AddOrEditMedicine Screen")
    fun iAmOnThAddOrEditMedicineScreen() {

        // Check that the first events are visible
        composeRule.waitUntil(timeout) {
            composeRule.onNodeWithTag("AddOrEditMedicineScreen").isDisplayed()
        }
    }

    @When("I select an aisle in the Aisle picker field")
    @And("I select an aisle in the Aisle picker field")
    fun selectAisle(aisle: String){

        // Click the item field to open PickerDialog
        composeRule.onNodeWithText("Aisle", useUnmergedTree = true).performClick()
        // Click on "OK" to select the first Aisle item
        composeRule.onNodeWithText("OK", useUnmergedTree = true).performClick()
    }

    @When("I select a quantity in the Stock picker field")
    @And("I select a quantity in the Stock picker field")
    fun selectStock(){

        // Click the item field to open PickerDialog
        composeRule.onNodeWithText("Stock", useUnmergedTree = true).performClick()

        // Perform scroll action to verify it is scrollable
        composeRule.onNode(hasScrollAction()) // ensure scrollable container exists
            .performScrollToIndex(10)           // scroll to a later item, e.g., 5th item

        // Click on "OK" to select the first Aisle item
        composeRule.onNodeWithText("OK", useUnmergedTree = true).performClick()
    }

    @Then("I cannot click on the {string} button")
    fun iCannotClickButton(buttonLabel:String){
        composeRule.onNodeWithText(text = buttonLabel, useUnmergedTree = true).performClick()
        // Assert that user does not go back to home screen
        composeRule.onNodeWithTag("AddOrEditMedicineScreen").isDisplayed()
        composeRule.onNodeWithText("Paracetamol").isNotDisplayed()
    }
}