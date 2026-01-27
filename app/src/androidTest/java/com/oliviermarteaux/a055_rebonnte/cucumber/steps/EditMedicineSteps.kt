package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.printToLog
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And

class EditMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000

    @And("I should see the edited medicine with the updated stock")
    fun iShouldSeeMedicineWithUpdatedStock() {
        //Thread.sleep(2000)

        // Check that the first events are visible
        composeRule.onNodeWithText("Paracetamol").assertIsDisplayed()
//        composeRule.onNodeWithText("Stock: 27").assertIsDisplayed()
        composeRule
            .onNode(
                hasText("Paracetamol") and hasText("Stock:", substring = true)
            )
            .assert(SemanticsMatcher("Stock not 10") { node ->
                val texts = node.config.getOrNull(SemanticsProperties.Text)
                if (texts == null || texts.size < 2) return@SemanticsMatcher false

                val stockText = texts[1].text // second text
                println("OM_TAG >>> StockText = $stockText") // visible in Gradle
                !stockText.contains("10")
            })
//        composeRule.waitUntil(timeout) {
//            composeRule.onNodeWithText("Paracetamol").isDisplayed()
//            composeRule.onNodeWithText("Stock: 27").isDisplayed()
//        }
    }
}