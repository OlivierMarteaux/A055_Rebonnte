package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And

class EditMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @And("I should see the edited medicine with the updated stock")
    fun iShouldSeeMedicineWithUpdatedStock() {
        Thread.sleep(2000)

        // Check that the first events are visible
        composeRule.onNodeWithText("Paracetamol").assertIsDisplayed()
        composeRule.onNodeWithText("Stock: 27").assertIsDisplayed()
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Paracetamol").isDisplayed()
//            composeRule.onNodeWithText("Stock: 27").isDisplayed()
//        }
    }
}