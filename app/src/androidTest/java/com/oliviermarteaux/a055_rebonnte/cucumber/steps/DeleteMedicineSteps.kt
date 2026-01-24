package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And

class DeleteMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @And("the deleted medicine should have been removed from the top of the list")
    fun deletedMedicineRemovedFromTheList() {
//        Thread.sleep(3000)

        // Check that the first events are visible
            composeRule.onNodeWithText("Paracetamol").assertIsNotDisplayed()
    }
}