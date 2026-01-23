package com.oliviermarteaux.a055_rebonnte.cucumber.steps

//import android.util.Log
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.isDisplayed
//import androidx.compose.ui.test.onNodeWithText
//import com.oliviermarteaux.a055_rebonnte.di.ComposeRuleHolder
//import io.cucumber.java.en.Given
//import io.cucumber.java.en.Then
//
//class LoginSteps(private val composeRuleHolder: ComposeRuleHolder) {
//
//    private val composeRule = composeRuleHolder.composeRule
//
//    @Given("I am on the Splash screen")
//    fun iAmOnTheSplashScreen() {
//        Log.d("OM_TAG", "I am on the Splash screen")
//        composeRule.onNodeWithText("Sign in with Google").assertIsDisplayed()
//        composeRule.onNodeWithText("Sign in with email").assertIsDisplayed()
//    }
//    @Then("I should arrive on the Login Screen")
//    fun iAmOnTheLoginScreen() {
//        composeRule.onNodeWithText("Email").assertIsDisplayed()
//        composeRule.onNodeWithText("Next").assertIsDisplayed()
//    }
//    @Then("I should arrive on the Password screen")
//    fun iAmOnThePasswordScreen() {
//        composeRule.waitUntil(5000) {
//            composeRule.onNodeWithText("Password").isDisplayed()
//            composeRule.onNodeWithText(text = "Sign in", useUnmergedTree = true).isDisplayed()
//        }
//    }
//    @Then("I should arrive on the Home Screen")
//    @Given("I am on the Home screen")
//    fun iAmOnTheHomeScreen() {
//        Log.d("OM_TAG", "I should arrive on the Home screen")
//        // Verify some known events are shown
//        composeRule.waitUntil(20000) {
//            composeRule.onNodeWithText("City Carnival").isDisplayed()
//        }
////        composeRule.waitUntil(5000) {
////            composeRule.onNodeWithTag("home_screen").isDisplayed()
////        }
////        composeRule
////            .onRoot(useUnmergedTree = true)
////            .printToLog("SEMANTICS")
////        assert(true)
////        composeRule.onNodeWithText("City Carnival").assertIsDisplayed()
//    }
//}