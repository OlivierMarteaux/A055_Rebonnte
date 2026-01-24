package com.oliviermarteaux.a055_rebonnte.cucumber.steps

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
import io.cucumber.java.en.And
import io.cucumber.java.en.When

//_ This class requires picocontainer to inject dependency
class SharedCucumberSteps(private val composeRuleHolder: ComposeRuleHolder) {

    private val composeRule = composeRuleHolder.composeRule

    @When("I enter {string} in the {string} field")
    fun iEnterText(name:String, textFieldLabel: String) {
        composeRule.onNodeWithText(textFieldLabel).performTextInput(name)
    }

    @And("I enter {string} in the field tagged {string}")
    fun iEnterTextInTaggedField(name:String, tag: String) {
        composeRule.onNodeWithTag(tag).performClick().performTextInput(name)
    }
//
//    @And("I should see {string} added at the end of the {string} list")
//    fun iShouldSeeTextInLastListItem(text: String, item: String){
//        composeRule.onNodeWithText(text).assertIsDisplayed()
//        val itemsNodes = composeRule.onAllNodes(hasClickAction())
//        val lastItemNode = itemsNodes[itemsNodes.fetchSemanticsNodes().size - 2]
//        lastItemNode.assertTextContains(text)
//    }
//
    @And("I should see {string} added at the top of the {string} list")
    fun iShouldSeeTextInFirstListItem(text: String, item: String){

        // Assert the post is displayed
        composeRule.onNodeWithText(text).assertIsDisplayed()
//        Thread.sleep(2000)
        val itemsNodes = composeRule.onAllNodes(hasClickAction())
        val lastItemNode: SemanticsNodeInteraction = itemsNodes[0]
        lastItemNode.assertTextContains(text)
    }
//
//    @And("I should see a toast message {string}")
//    fun iShouldSeeText(text: String) {
//        composeRule.onNodeWithText(text).assertIsDisplayed()
//    }
//
//    @Then("I should arrive on the {string} screen for the {string} item named {string}")
//    fun iAmOnTheItemScreen(screen: String, item: String, itemName: String, ) {
//        composeRule.waitUntil(5000) {
//            composeRule.onNodeWithTag(itemName).isDisplayed()
//        }
////        composeRule
////            .onRoot(useUnmergedTree = true)
////            .printToLog("SEMANTICS")
//    }
//    @When("I select {string} in the Date field")
//    fun selectDate(date: String) {
//        // Split date string "dd/MM/yyyy"
//        val (day, month, year) = date.split("/").map { it.toInt() }
//
//        // Click the date field to open DatePickerDialog
//        composeRule.onNodeWithText("Date", useUnmergedTree = true).performClick()
//
//        // Use Espresso to set the date in the dialog
//        onView(withClassName(`is`(DatePicker::class.java.name)))
//            .inRoot(isDialog())
//            .perform(PickerActions.setDate(year, month, day))
//
//        // Click OK button
//        onView(withText("OK"))
//            .inRoot(isDialog())
//            .perform(click())
//    }
//    @When("I select {string} in the Time field")
//    fun selectTime(time: String) {
//        // Split time string "HH:mm"
//        val (hour, minute) = time.split(":").map { it.toInt() }
//
//        // Click the time field to open TimePickerDialog
//        composeRule.onNodeWithText("Time", useUnmergedTree = true).performClick()
//
//        // Use Espresso to set the time
//        onView(withClassName(`is`(TimePicker::class.java.name)))
//            .inRoot(isDialog())
//            .perform(PickerActions.setTime(hour, minute))
//
//        // Click OK button
//        onView(withText("OK"))
//            .inRoot(isDialog())
//            .perform(click())
//    }
//
//    @When("I pick the first photo from the photo library")
//    fun iPickTheFirstPhotoFromLibrary() {
//
//        composeRule.onNodeWithTag("Locale Photo Button").performClick()
//        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
//
//        // Wait for photo picker
//        device.wait(
//            Until.hasObject(By.pkg("com.android.providers.media")),
//            1_000
//        )
//
//        // Find the first photo in the picker. Using a content description is often reliable.
//        val firstPhoto = device.findObject(By.descStartsWith("Photo"))
//        firstPhoto.click()
//    }
}