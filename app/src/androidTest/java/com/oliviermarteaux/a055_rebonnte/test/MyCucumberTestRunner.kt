package com.oliviermarteaux.a055_rebonnte.test

import android.os.Bundle
import com.oliviermarteaux.shared.utils.TestConfig
import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

/**
 * MyCucumberTestRunner class should stay empty and we don’t need any implementation inside.
 * This is just placeholder class where CucumberOptions annotation lives.
 * In addition, you may also noticed @SuppressWarnings(“unused”) , which indicates that we are not
 * contemplating to use this class at all.
 *
 * The CucumberOptions annotation is mandatory and relevant only for a particular class amongst all
 * in the test project — the first annotated class.
 *
 * _ FEATURE SETTING:
 * Although CucumberOptions annotation has a lot of different optional settings, I consider above
 * all else to specify at least the features path.
 * In our example this is done by features = “features”.
 * Basically, what the setting says is “Features files must be placed inside assets/features/ of the
 * test project”.
 *
 * _ GLUE SETTING:
 * The glue setting in the CucumberOptions annotation is about Cucumber path for the Step
 * implementations source code. This option is not mandatory, I would not regard this indispensable
 * as it rather helps Cucumber and people to understand where the Espresso source code is.
 * In our case we proceed by creating in androidTest folder new package, named cucumber and then
 * another new one, named steps in cucumber folder.
 */
@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["features"],   // ✅ IMPORTANT
    glue = [
        "com.oliviermarteaux.a055_rebonnte.cucumber.steps",
        "com.oliviermarteaux.a055_rebonnte.di",
    ],
    tags = "",
    plugin = [
        "pretty",                               // console output
        "summary",                              // summary at the end
        "html:/data/data/com.oliviermarteaux.a055_rebonnte/files/cucumber-report.html",  // HTML file on device
        "json:/data/data/com.oliviermarteaux.a055_rebonnte/files/cucumber-report.json"   // JSON for CI
    ]
)
@SuppressWarnings("unused")
class MyCucumberTestRunner : CucumberAndroidJUnitRunner() {
    override fun onCreate(bundle: Bundle?) {
        TestConfig.isTest = true
        super.onCreate(bundle)
    }
}