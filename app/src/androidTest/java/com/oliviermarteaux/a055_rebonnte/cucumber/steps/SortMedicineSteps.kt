package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import android.util.Log
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.text.input.ImeAction
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeMedicineList
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.Assert.assertEquals

class SortMedicineSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule
    val timeout: Long = 5_000
    @Then("I should see the medicines sorted by {string} {string}")
    fun iShouldSeeMedicinesSorted(sortDirection:String, sortField:String) {
        Log.d("OM_TAG", "I should see the medicines sorted by $sortDirection $sortField")

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("MedicineLazyList")
            .performScrollToIndex(0)

        composeRule.waitForIdle()

        val nodes = composeRule.onAllNodes(hasTestTag("MedicineItem"))
            .fetchSemanticsNodes()

        val actualList: List<String> = nodes.map {
//            it.config.getOrNull(SemanticsProperties.Text)?.joinToString("") ?: ""
            it.config.getOrNull(SemanticsProperties.Text)?.get(0).toString()
        }

        val defaultOrder = listOf("Paracetamol", "Ibuprofen", "Aspirin", "Amoxicillin", "Azithromycin",
            "Ciprofloxacin", "Metformin", "Insulin Glargine")

        when (sortField) {
            "name" -> {
                val expectedList = fakeMedicineList.sortedBy{it.name}.take(8).map{it.name}
                for (i in 0..7) Log.d("OM_TAG", "SortMedicineSteps::expectedList: ${expectedList[i]} - actualList: ${actualList[i]}")
                assertEquals(expectedList, actualList)
            }
            "stock" -> {
                when(sortDirection){
                    "ascending" -> {
                        val expectedList = fakeMedicineList.sortedBy{it.stock}.take(8).map{it.name}
                        for (i in 0..7) Log.d("OM_TAG", "SortMedicineSteps::expectedList: ${expectedList[i]} - actualList: ${actualList[i]}")
                        assertEquals(expectedList, actualList)
                    }
                    "descending" -> {
                        val expectedList = fakeMedicineList.sortedByDescending{it.stock}.take(8).map{it.name}
                        for (i in 0..7) Log.d("OM_TAG", "SortMedicineSteps::expectedList: ${expectedList[i]} - actualList: ${actualList[i]}")
                        assertEquals(expectedList, actualList)
                    }
                }
            }
        }
    }
}